package frc.robot.subsystems.arm;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase {

    private ArmIO io; 
    private ArmIO.Inputs inputs; 

    private PIDController armController;
    private ArmFeedforward armFeedforward; 

    // TODO: should we put these in a constants file?
    public static final double GEARING = 3 * 4 * 5; 
    public static final double MOI = 100000; 

    public static Arm create() {
        return RobotBase.isReal() ? 
            new Arm(new ArmIOReal()) : 
            new Arm(new ArmIOSim()); 
    }

    
    public static Arm disable() {
        return new Arm(new ArmIOInert()); 
    }


    public Arm(ArmIO io) {
        this.armController = new PIDController(io.getConfig().kP(), io.getConfig().kI(), io.getConfig().kD()); 
        this.armFeedforward = new ArmFeedforward(0, io.getConfig().kG(), 0); 

        inputs = new ArmIO.Inputs(); 
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
    }

    // TODO: I'm leaning towards not using the Trigger structure since we can wrap around it anytime we want? idk I prefer using raw values instead. 4/10 leaning towards not using Triggers. 
    public boolean atPosition() {
        return this.armController.atSetpoint(); 
    }

    public Command goToAngle(double target) {
        // TODO: should this end when we're at the angle? or do that in an outside implementation
        return run(() -> {
            double pidOutput = armController.calculate(target, inputs.absoluteAngle) + this.armFeedforward.calculate(target, 0); 
            io.set(pidOutput);
        })
        // .until(this::atPosition) // ??
        ;
    }

    public Command runPower(double power) {
        return run(() -> {
            io.set(power);
        });
    }

    public Command tune() {
        return 
            Commands.deferredProxy(() -> {
                return goToAngle(0); 
            }).repeatedly(); 
    }

}
