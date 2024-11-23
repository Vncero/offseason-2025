package frc.robot.subsystems.arm;

// TODO: thoughts on naming? :)
public class ArmIOInert implements ArmIO {

    public ArmIOInert() {}

    @Override
    public void updateInputs(ArmIO.ArmInputs inputs) {
        inputs.velocity = 0; 
        inputs.absoluteAngle = 0; 
        inputs.relativeAngleLeft = 0; 
        inputs.relativeAngleRight = 0; 
    }

    @Override
    public Config getConfig() {
        return new Config(0, 0, 0, 0); 
    }

    @Override
    public void set(double power) {

    }
}
