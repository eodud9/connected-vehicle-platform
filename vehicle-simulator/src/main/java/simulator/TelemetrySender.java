package simulator;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TelemetrySender {

    private final String backendBaseUrl;
    private final HttpClient client;

    public void send(Long vehicleId, String apiKey, VehicleState state){
        try{
            LocalDateTime now = LocalDateTime.now();

            String json = """
                                {
                                "latitude": %f,
                                "longitude": %f,
                                "speed": %f,
                                "batteryLevel": %f,
                                "temperature": %f,
                                "ignitionStatus": %b,
                                "recordedAt": "%s"
                                }
                                """.formatted(
                    state.getLatitude(),
                    state.getLongitude(),
                    state.getSpeed(),
                    state.getBatteryLevel(),
                    state.getTemperature(),
                    state.isIgnitionStatus(),
                    now);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(backendBaseUrl + "/api/vehicles/" + vehicleId + "/telemetry"))
                    .header("Content-Type", "application/json")
                    .header("X-Vehicle-Api-Key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =  client.send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();

            if(statusCode == 201){
                System.out.println("Telemetry sent successfully. status="+ response.statusCode());
            }
            else {
                System.err.println("Telemetry failed: " + statusCode + "/body=" + response.body());
            }
        }
        catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Telemetry transmission interrupted.");
            throw new IllegalStateException("Simulator task interrupted.", e);
        }
        catch (IOException e){
            System.err.println("Telemetry transmission failed: " + e.getMessage());
        }
    }
}
