package com.geekhome.automationmodule.automation;

public class TimeLeftFormatter {
    public static String formatTimeLeft(long seconds) {
        long sec = seconds % 60;
        long min = (seconds - sec) / 60;
        if (min > 60) {
            long hours = min / 60;
            return String.format("~ %dh", hours);
        } else if (min >0) {
            return String.format("~ %dm", min);
        } else {
            return String.format("%ds", sec);
        }
    }
}