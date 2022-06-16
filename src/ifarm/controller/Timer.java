
package ifarm.controller;

public class Timer {

    private double startTime;
    private double endTime;

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime() {
        this.startTime = System.nanoTime();;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime() {
        this.endTime =  System.nanoTime();
    }

    public double calcDuration() {
        return (endTime - startTime) / 1000000000.0;
    }
}
