package frc.robot.subsystems.arm;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class ArmIOSim implements ArmIO {

    private DCMotorSim pivot;

    public ArmIO.Config getConfig() {
        return new ArmIO.Config(0, 0, 0, 0);
    }

    public ArmIOSim() {
        pivot = new DCMotorSim(DCMotor.getNEO(2), Arm.GEARING, Arm.MOI); 
    }

    @Override
    public void updateInputs(Inputs inputs) {
        // TODO: mech advantage did this. I'll do it like this too. lmk your thoughts. Are there any "periodic" operations that aren't "updating inputs"?
        pivot.update(0.02);

        inputs.absoluteAngle = pivot.getAngularPositionRad();
        // TODO: thoughts on ways to deal with this? We don't have an "absolute" or "relative" angle in simulation but I'd still like to log both. We could just keep it like this 
        inputs.relativeAngleLeft = pivot.getAngularPositionRad(); 
        inputs.relativeAngleRight = pivot.getAngularPositionRad(); 
    }

    @Override
    public void set(double power) {
        pivot.setInput(power);
    }
}
