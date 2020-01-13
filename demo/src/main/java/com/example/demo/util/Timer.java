package com.example.demo.util;

public class Timer {
    private static Timer timer = null;
    private double startTime;
    private double endTime;

    private Timer() {};

    public static Timer getTimer() {
        if(timer == null) {
            synchronized (Timer.class) {
                timer = new Timer();
            }
        }
        return timer;
    }

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop(String message) {
        endTime = System.nanoTime();
        double elapsedTime = Math.abs(endTime) - Math.abs(startTime);
        if(message != null) {
            System.out.print(message);
        }
        System.out.print(" " + elapsedTime/1000000000 + " seconds\n");
        reset();
    }

    private void reset() {
        startTime = 0;
        endTime = 0;
    }
}
