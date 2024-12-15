package frc.robot.util;

import edu.wpi.first.networktables.DoubleEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Tuning {
    public static DoubleEntry entry(String name, double value) {
        var entry = NetworkTableInstance.getDefault().getDoubleTopic(name).getEntry(value);
        entry.set(value);
        return entry;
    }
}