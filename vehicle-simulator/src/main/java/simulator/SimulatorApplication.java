package simulator;

import java.net.http.HttpClient;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimulatorApplication {

    public static void main(String[] args) {
        System.out.println("Vehicle Simulator started");

        HttpClient client = HttpClient.newHttpClient();

        Long vehicleId = 5L;
        String apiKey = System.getenv("VEHICLE_API_KEY");

        if(apiKey == null || apiKey.isBlank()){
            throw new IllegalStateException("VEHICLE_API_KEY is not configured.");
        }

        String backendBaseUrl = "http://localhost:8080";

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        TelemetrySender sender = new TelemetrySender(backendBaseUrl, client);

        VehicleState state = new VehicleState(
                37.3947,
                127.1112,
                0.0,
                80.0,
                30.0,
                false,
                0
        );

        List<SimulatedVehicle> vehicles = List.of(new SimulatedVehicle(vehicleId, apiKey, state));

        scheduler.scheduleAtFixedRate(
                () -> {
                    for (SimulatedVehicle vehicle : vehicles) {
                        vehicle.getState().update();
                        sender.send(vehicle.getVehicleId(), vehicle.getApiKey(), vehicle.getState());
                    }
                },
                0,
                1,
                TimeUnit.SECONDS
        );

    }





}
