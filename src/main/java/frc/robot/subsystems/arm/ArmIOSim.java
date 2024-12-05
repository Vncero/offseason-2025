package frc.robot.subsystems.arm;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import frc.robot.subsystems.arm.Arm.ArmConfig;

public class ArmIOSim implements ArmIO {

    public static final ArmConfig config = new ArmConfig(0.02, 0, 0, 0); 

    private DCMotorSim pivot;

    public ArmIOSim() {
        pivot = new DCMotorSim(
            LinearSystemId.createDCMotorSystem(DCMotor.getNEO(2), Arm.MOI, Arm.GEARING), 
            DCMotor.getNEO(2)
        ); 
    }

    @Override
    public void updateInputs(ArmInputs inputs) {
        pivot.update(0.02);

        inputs.absoluteAngle = Rotation2d.fromRadians(pivot.getAngularPositionRad());
        inputs.velocityMetersPerSecond = Math.toDegrees(pivot.getAngularVelocityRadPerSec()); 
    }

    @Override
    public void setVoltage(double volts) {
        pivot.setInput(volts);
    }
}
