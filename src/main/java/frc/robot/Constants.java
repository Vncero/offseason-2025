package frc.robot;

import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.RobotController;


public class Constants {
    // This means the Constants class should be loaded at the start of runtime---probably log it in RobotContainer constructor?
    public static final double kRobotInitialVoltage = RobotController.getBatteryVoltage();
}
