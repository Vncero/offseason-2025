package frc.robot.auto;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

import java.util.Set;

import frc.robot.auto.Response.YesNo;
import frc.robot.auto.Response.FirstNote;

import static frc.robot.auto.Question.poseQuestion;

/*
   * auto:
   * - a segment (command)
   * - another segment
   * - question-based segment <- supplied by the questionnaire
   * - a segment (command)
   * - another segment
   * - question-based segment <- supplied by the questionnaire
   * 
   * questionnaire --> making the auto command
   * question -> provides mapping answer to command
   * 
   * plugging in
   * -  deferredcommand
   * 
   */
public class AutoDealer {
    public interface AutoRoutine {
        Supplier<Command> init();
    }

    private Supplier<Command> leDeal;

    //pre-made playstyles / unique questions
    public static final AutoRoutine kOffense = () -> {
        final var yesNoQuestion = poseQuestion("Answer the yes or no question, Raphael Hoang", YesNo.NO, (YesNo choice) -> switch (choice) {
                case YES -> Commands.runOnce(() -> {});
                case NO  -> Commands.none();
            });
        
        return () -> Commands.sequence(
            Commands.waitSeconds(1),
            yesNoQuestion.resolve(),
            Commands.none()
        );
    };

    public static final AutoRoutine kDefense = () -> {
        final var firstNote = poseQuestion("What's the first note?", FirstNote.TOP, (FirstNote choice) -> switch (choice) {
            case TOP -> Commands.print("this is a top auto");
            case MID -> Commands.none();
            case BOT -> Commands.print("hi");
        });

        return () -> Commands.sequence(
            Commands.waitSeconds(2),
            firstNote.resolve(),
            Commands.waitSeconds(1)
        );
    };
        
    /**
     * Presents auto with questionnaire
     * @param autoStyle supplier that returns a sequence of commands with questions to resolve
     * @return a command that resolves at runtime with the question responses, requiring all subsystems (since it's auto)
     */
    public void request(AutoRoutine routine) {
        leDeal = routine.init();
    }

    public Command getAutoRoutine(){
        return Commands.defer(leDeal, Set.of()); // TODO: add all the subsystems here
    }
}
