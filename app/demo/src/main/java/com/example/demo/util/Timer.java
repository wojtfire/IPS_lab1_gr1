package com.example.demo.util;

import com.example.demo.web.dto.BenchmarkDto;

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

    public BenchmarkDto stop(String message) {
        endTime = System.nanoTime();
        double elapsedTime = Math.abs(endTime) - Math.abs(startTime);
        if(message != null) {
            System.out.print(message);
        }
        double parsedElapsedTime = elapsedTime/1000000000;
        System.out.print(" " + parsedElapsedTime + " seconds\n");
        reset();
        BenchmarkDto dto = new BenchmarkDto();
        dto.setElapsedTime(String.valueOf(parsedElapsedTime));
        return dto;
    }

    private void reset() {
        startTime = 0;
        endTime = 0;
    }
}
