package connected_vehicle.domain.telemetry;

import connected_vehicle.domain.drivingSession.DrivingSession;
import connected_vehicle.domain.drivingSession.DrivingSessionRepository;
import connected_vehicle.domain.telemetry.dto.TelemetryCreateRequest;
import connected_vehicle.domain.vehicle.Vehicle;
import connected_vehicle.domain.vehicle.VehicleService;
import connected_vehicle.domain.vehicleEvent.VehicleEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelemetryService {

    private final TelemetryRepository telemetryRepository;
    private final VehicleService vehicleService;
    private final DrivingSessionRepository drivingSessionRepository;
    private final VehicleEventService vehicleEventService;

    @Transactional
    public void createTelemetry(Long vehicleId, String apiKey, TelemetryCreateRequest request){

        Vehicle vehicle = vehicleService.authenticateVehicle(vehicleId, apiKey);

        Telemetry prevTelemetry = telemetryRepository.findFirstByVehicle_IdOrderByRecordedAtDesc(vehicleId)
                .orElse(null);

        Telemetry telemetry = new Telemetry(
                vehicle,
                request.latitude(),
                request.longitude(),
                request.speed(),
                request.batteryLevel(),
                request.temperature(),
                request.ignitionStatus(),
                request.recordedAt());

        DrivingSession sessionToEnd = null;

        if(prevTelemetry == null){
            if(telemetry.isIgnitionStatus()){
                DrivingSession drivingSession = new DrivingSession(
                        vehicle,
                        telemetry.getRecordedAt() ,
                        telemetry.getBatteryLevel());

                drivingSessionRepository.save(drivingSession);
                telemetry.assignDrivingSession(drivingSession);

            }
        } else {

            if(!prevTelemetry.isIgnitionStatus() && telemetry.isIgnitionStatus()){
                DrivingSession drivingSession = new DrivingSession(
                        vehicle,
                        telemetry.getRecordedAt(),
                        telemetry.getBatteryLevel());

                drivingSessionRepository.save(drivingSession);
                telemetry.assignDrivingSession(drivingSession);
            }

            else if(prevTelemetry.isIgnitionStatus() && telemetry.isIgnitionStatus()){
                telemetry.assignDrivingSession(prevTelemetry.getDrivingSession());
            }

            else if(prevTelemetry.isIgnitionStatus() && !telemetry.isIgnitionStatus()){

                DrivingSession session = prevTelemetry.getDrivingSession();

                telemetry.assignDrivingSession(session);

                sessionToEnd = session;

            }

        }

        telemetryRepository.save(telemetry);

        vehicleEventService.detectEvents(prevTelemetry, telemetry);

        if(sessionToEnd != null){

            List<Telemetry> telemetries = telemetryRepository.findAllByDrivingSession_IdOrderByRecordedAtAsc(sessionToEnd.getId());

            double totalDistance = 0;

            for (int i = 1; i < telemetries.size(); i++){

                Telemetry prev = telemetries.get(i - 1);
                Telemetry current = telemetries.get(i);

                totalDistance += calculateDistance(prev, current);
            }

            Duration duration = Duration.between(
                    sessionToEnd.getStartedAt(), telemetry.getRecordedAt()
            );

            double durationHours = duration.toSeconds() / 3600.0;



            double averageSpeed = durationHours == 0 ? 0 : totalDistance / durationHours;


            sessionToEnd.end(telemetry.getRecordedAt(), telemetry.getBatteryLevel(), totalDistance, averageSpeed);

        }

    }

    public double calculateDistance(Telemetry prev, Telemetry current){

        double prevLat = Math.toRadians(prev.getLatitude());
        double prevLong = Math.toRadians(prev.getLongitude());

        double currentLat = Math.toRadians(current.getLatitude());
        double currentLong = Math.toRadians(current.getLongitude());

        double deltaLat = currentLat - prevLat;
        double deltaLong = currentLong - prevLong;

        double a =
                Math.pow(Math.sin(deltaLat / 2), 2)
                        + Math.cos(prevLat)
                        * Math.cos(currentLat)
                        * Math.pow(Math.sin(deltaLong / 2), 2);

        double c = 2 * Math.atan2(
                Math.sqrt(a),
                Math.sqrt(1 - a)
        );

        double earthRadius = 6371.0; // km

        return earthRadius * c;
    }
}
