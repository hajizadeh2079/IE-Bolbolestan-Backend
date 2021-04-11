package com.example.model;

public class MinJob implements Runnable {

    @Override
    public void run() {
        UnitSelectionSystem.getInstance().waitListToFinalizedCourse();
    }
}