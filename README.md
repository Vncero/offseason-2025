# New code structure changes for RoboLancers 2024-25

## Motivations

Last year, there were numerous inconsistencies and issues with the way the robot code was structured. There were also features like simulation or logging that were hard to implement. This new code structure intends to make these features easier and generally make programming smoother.

## Where to look

[Intake](./src/main/java/frc/robot/subsystems/intake/Intake.java) and [IntakeIO](./src/main/java/frc/robot/subsystems/intake/IntakeIO.java) are fully documented with comments that motivate aspects of the structure, explain such aspects, and describe their intent in future robot projects.

[RobotContainer](./src/main/java/frc/robot/RobotContainer.java) shows roughly how the new structure ends up looking for robot controls.
