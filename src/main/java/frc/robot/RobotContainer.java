// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.arm.Arm;
import frc.robot.subsystems.intake.Intake;

public class RobotContainer {
  private final Intake intake = Intake.create();
  private final Arm arm = Arm.create(); 

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
    manipulator
      .rightBumper()
      .whileTrue(intake.runRollers()); // notice here there's an implicit end condition: right bumper is released
  }

  public Command getAutonomousCommand() {
    return intake.runRollers()
      .until(intake.hasNote); // notice the trigger is used to provide an end condition for the auto to continue
  }
}
