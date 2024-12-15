// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.intake.Intake;

@Logged
public class RobotContainer {
  @Logged(name = "Intake")
  public final Intake intake = Intake.disable();
  @Logged(name = "Arm")
  public final Arm arm = Arm.create(); 
  
  private final CommandXboxController driver = new CommandXboxController(0);
  private final CommandXboxController manipulator = new CommandXboxController(1);

  public RobotContainer() {
    arm.setDefaultCommand(
      arm.goToAngle(20)
      // arm.tune()
      ); 
    intake.setDefaultCommand(intake.stop());
    configureBindings();
  }

  private void configureBindings() {
    driver.a().whileTrue(arm.goToAngle(80)); 
    manipulator
      .rightBumper()
      .whileTrue(intake.runRollers()); // notice here there's an implicit end condition: right bumper is released

    manipulator.rightTrigger()
      .and(intake.hasNote.negate()) // negate() is a fancy way to say !hasNote
      // easily using subsystem Triggers to bind commands that run based on robot state is one of the nicest reasons for Triggers
      // combining Triggers to track robot state will be used more extensively with superstructure work to come
      .whileTrue(intake.tune());
  }

  public Command getAutonomousCommand() {
    // return Commands.none(); 
    return intake.runRollers()
      .until(intake.hasNote); // notice the trigger is used to provide an end condition for the auto to continue
  }
}
