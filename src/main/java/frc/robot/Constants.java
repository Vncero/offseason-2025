package frc.robot;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.units.Measure;
import edu.wpi.first.units.Voltage;
import edu.wpi.first.wpilibj.RobotController;

public class Constants {
    // This means the Constants class should be loaded at the start of runtime---probably log it in RobotContainer constructor?
    public static final Measure<Voltage> kRobotInitialVoltage = Volts.of(RobotController.getBatteryVoltage());
}
