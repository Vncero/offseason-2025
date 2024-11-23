package frc.robot.subsystems.arm;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

public class ArmIOReal implements ArmIO {
    public ArmIO.Config getConfig() {
        return new ArmIO.Config(0, 0, 0, 0);
    }

    private CANSparkMax leftPivot = new CANSparkMax(0, MotorType.kBrushless); 
    private CANSparkMax rightPivot = new CANSparkMax(0, MotorType.kBrushless); 
    private AbsoluteEncoder armAbsoluteEncoder = leftPivot.getAbsoluteEncoder(); 

    private RelativeEncoder relEncoderLeft = leftPivot.getEncoder();
    private RelativeEncoder relEncoderRight = leftPivot.getEncoder(); 


    public ArmIOReal() {
        setupMotors();
        setupEncoders();
    }

    private void setupMotors() {

        // TODO: where to put constants? Does the Constants file work for this type of structure?
        rightPivot.setIdleMode(IdleMode.kBrake);
        leftPivot.setIdleMode(IdleMode.kBrake);

        leftPivot.setInverted(false);
        // rightPivot.setInverted(true); 
        
        rightPivot.setSmartCurrentLimit(40);
        leftPivot.setSmartCurrentLimit(40);
        
        // right arm motor would follow left arm motor's voltage 
        rightPivot.follow(leftPivot, true);

        leftPivot.burnFlash(); 
        rightPivot.burnFlash();
    }

    public void setupEncoders() {
        // conversion factors
        armAbsoluteEncoder.setPositionConversionFactor(360);
        armAbsoluteEncoder.setVelocityConversionFactor(360 / 60.0); 
    }


    @Override
    public void updateInputs(ArmIO.Inputs inputs) {
        inputs.absoluteAngle = armAbsoluteEncoder.getPosition();

        inputs.relativeAngleLeft = relEncoderLeft.getPosition();
        inputs.relativeAngleRight = relEncoderRight.getPosition();
    }

    @Override
    public void set(double power) {
        leftPivot.set(power);
        rightPivot.set(power);
    }
}
