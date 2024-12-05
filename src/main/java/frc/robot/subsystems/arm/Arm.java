package frc.robot.subsystems.arm;

import java.util.function.DoubleSupplier;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

@Logged
public class Arm extends SubsystemBase {

    public record ArmConfig(double kP, double kI, double kD, double kG) {}    

    private ArmIO io; 
    private ArmInputs inputs; 

    private PIDController armController;
    private ArmFeedforward armFeedforward; 

    public static final double GEARING = 3 * 4 * 5; 
    public static final double MOI = 10; 

    public static Arm create() {
        return RobotBase.isReal() ? 
            new Arm(new ArmIOReal(), ArmIOReal.config) : 
            new Arm(new ArmIOReal(), ArmIOSim.config); 
    }

    
    public static Arm disable() {
        return new Arm(new ArmIOIdeal(), ArmIOIdeal.config); 
    }


    public Arm(ArmIO io, ArmConfig config) {
        this.io = io; 
        this.inputs = new ArmInputs();
        
        this.armController = new PIDController(config.kP, config.kI, config.kD); 
        this.armFeedforward = new ArmFeedforward(0, config.kG, 0); 
        
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
        return goToAngle(() -> target); 
    }

    public Command goToAngle(DoubleSupplier angleSupp) {
        return run(() -> {
            double pidOutput = armController.calculate(inputs.absoluteAngle.getDegrees(), angleSupp.getAsDouble()) + this.armFeedforward.calculate(angleSupp.getAsDouble(), 0); 
            io.setVoltage(pidOutput);
        });
    }

    public Command runVolts(double volts) {
        return run(() -> {
            io.setVoltage(volts);
        });
    }

    public Command tune() {
        // TODO: redo this with the implementation-independent config
        // LoggedDashboardNumber kP = new LoggedDashboardNumber("Arm/kP", io.getConfig().kP()); 
        // LoggedDashboardNumber kI = new LoggedDashboardNumber("Arm/kI", io.getConfig().kI()); 
        // LoggedDashboardNumber kD = new LoggedDashboardNumber("Arm/kD", io.getConfig().kD());
        // LoggedDashboardNumber kG = new LoggedDashboardNumber("Arm/kG", io.getConfig().kG());
        // TODO: is this a "full test"?
        // return runOnce(() -> {
        //     armController.setPID(kP.get(), kI.get(), kD.get());
        //     armFeedforward = new ArmFeedforward(0, kG.get(), 0); 
        // })
        // .andThen(goToAngle(20).until(this::atPosition))
        // .andThen(goToAngle(80).until(this::atPosition))
        // .andThen(goToAngle(40).until(this::atPosition)); 
        return Commands.none(); 
    }

}
