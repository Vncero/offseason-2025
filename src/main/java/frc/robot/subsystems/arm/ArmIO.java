package frc.robot.subsystems.arm;

public interface ArmIO {
    public class Inputs {
        public double velocity; // TODO: we are not using units, unless you have a genuine reason to want to use it. 8/10 leaning towards not using it. 
        public double absoluteAngle; // TODO: degrees or radians? Or Rotation2d?
        public double relativeAngleLeft; 
        public double relativeAngleRight; 
    }

    // TODO: thoughts on this or a similar structure? makes getting config a bit easier and implemention-independent. I'm generally fine with either tbh. 2/10 leaning towards making the config implementation-independent. 
    public record Config(double kP, double kI, double kD, double kG) {}

    Config getConfig(); 

    // TODO: removed default keyword to ensure all implementations explicitly implement it. 1/10 leaning towards not using default?
    void updateInputs(Inputs inputs);

    // TODO: should we use set(double power) or setVoltage(double volts)? We've historically just set power but ik SysID operates on PID with an output in volts. Also meh on either option, but we should probably standardize. 0/10, not leaning towards either option. 
    void set(double power);


}
