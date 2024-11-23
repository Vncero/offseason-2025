package frc.robot.subsystems.arm;

import java.util.function.DoubleSupplier;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardNumber;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase {

    private ArmIO io; 
    // TODO: (akit) they generate a ghost "__AutoLogged" class that implements the logic for logging; this means we'll name it "<Subsystem>Inputs" instead of "Inputs"
    private ArmInputsAutoLogged inputs; 

    private PIDController armController;
    private ArmFeedforward armFeedforward; 

    // TODO: this is one way to log setpoint outputs. is this good?
    @AutoLogOutput
    private double setpoint; 

    // TODO: should we put these in a constants file?
    public static final double GEARING = 3 * 4 * 5; 
    public static final double MOI = 10; 

    public static Arm create() {
        return RobotBase.isReal() ? 
            new Arm(new ArmIOReal()) : 
            new Arm(new ArmIOSim()); 
    }

    
    public static Arm disable() {
        return new Arm(new ArmIOInert()); 
    }


    public Arm(ArmIO io) {
        this.io = io; 
        this.inputs = new ArmInputsAutoLogged();
        
        this.armController = new PIDController(io.getConfig().kP(), io.getConfig().kI(), io.getConfig().kD()); 
        this.armFeedforward = new ArmFeedforward(0, io.getConfig().kG(), 0); 
        
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("ArmSubsystem", inputs);
    }

    // TODO: I'm leaning towards not using the Trigger structure since we can wrap around it anytime we want? idk I prefer using raw values instead. 4/10 leaning towards not using Triggers. 
    public boolean atPosition() {
        return this.armController.atSetpoint(); 
    }

    public Command goToAngle(double target) {
        return goToAngle(() -> target); 
    }

    public Command goToAngle(DoubleSupplier angleSupp) {
        // TODO: should this end when we're at the angle? or do that in an outside implementation
        // TODO: more: how should we log the setpoint? it's not a direct "input" from a physical system but is pretty important. One way might be to just log the current command running and its information but idk how that implementation will work
        return run(() -> {
            this.setpoint = angleSupp.getAsDouble(); 
            double pidOutput = armController.calculate(inputs.absoluteAngle, angleSupp.getAsDouble()) + this.armFeedforward.calculate(angleSupp.getAsDouble(), 0); 
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
        // TODO: is this good? any better way?
        LoggedDashboardNumber kP = new LoggedDashboardNumber("Arm/kP", io.getConfig().kP()); 
        LoggedDashboardNumber kI = new LoggedDashboardNumber("Arm/kI", io.getConfig().kI()); 
        LoggedDashboardNumber kD = new LoggedDashboardNumber("Arm/kD", io.getConfig().kD());
        LoggedDashboardNumber kG = new LoggedDashboardNumber("Arm/kG", io.getConfig().kG());
        LoggedDashboardNumber setpoint = new LoggedDashboardNumber("Arm/tuningSetpoint");  
        return 
        goToAngle(() -> setpoint.get())
        // TODO: ik this is bad convention. find a better way if you want to. 
        .alongWith(Commands.run(() -> {
            armController.setPID(kP.get(), kI.get(), kD.get());
            armFeedforward = new ArmFeedforward(0, kG.get(), 0); 
        }));
    }

}
