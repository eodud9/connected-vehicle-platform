package simulator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VehicleState {
    private double latitude;
    private double longitude;
    private double speed;
    private double batteryLevel;
    private double temperature;
    private boolean ignitionStatus;

    private int elapsedSeconds;

    public void update(){

        int cycleSecond = elapsedSeconds % 30;

        if(cycleSecond < 5){
            this.ignitionStatus = false;
            this.speed = 0;
        }
        else {
            this.ignitionStatus = true;
            this.speed = Math.min(60, this.speed + 5);
            double distanceMeters = this.speed / 3.6;
            this.latitude += distanceMeters / 111_000.0;
            this.batteryLevel = Math.max(0, this.batteryLevel - 0.01);
            this.temperature = Math.min(50, this.temperature + 0.02);
        }

       this.elapsedSeconds++;
    }
}
