package duke.util;

import duke.command.Command;
import duke.command.AddCommand;
import duke.command.DoneCommand;
import duke.command.RemoveCommand;
import duke.command.ListCommand;
import duke.command.FindCommand;
import duke.command.ExitCommand;
import duke.command.InvalidCommand;
import duke.command.HelpCommand;
import duke.command.SortCommand;
import duke.command.StartTaskCommand;

import java.util.Arrays;

/**
 * The Parser is in charge of parsing raw strings of user inputs
 * and generating the appropriate response to the inputs. For example,
 * when the user sends "exit", the parser will return an ExitCommand
 * to be executed by the program.
 *
 * All methods of this class are static as this class is meant to be used functionally.
 */
public class Parser {

    /**
     * Splits the raw string using single spaces as delimiters to generate
     * tokenized strings. This is required for the program to parse commands.
     * Example:
     *
     * "a user input" -> ["a", "user", "input"]
     *
     * @param raw the raw string input.
     * @return the split string.
     */
    private static String[] format(String raw) {
        return raw.split(" ");
    }

    /**
     * This method is used when the user's command involves
     * task creation. The parser extracts the description of the task.
     *
     * "todo sleep for 20 hrs"          -> "sleep for 20 hrs"
     * "event lecture /at today 10:00"  -> "lecture /at today 10:00"
     *
     * The output string will be passed onto the respective task constructors.
     * @param raw the raw string input.
     * @return the task description.
     */
    public static String getTaskDescription(String[] raw) {
        String[] temp = Arrays.copyOfRange(raw, 1, raw.length);
        return String.join(" ", temp);
    }

    /**
     * Generates the appropriate command object based on the user input.
     * This requires a series of string analysis such as checking of the
     * command denoted as the first word of the user's message, and further
     * parsing of the rest of the message accordingly.
     * @param input the raw string input.
     * @return the appropriate Command object in response to the user input.
     * @throws DukeException when task creation fails or task indices are incorrect.
     */
    public static Command parse(String input) throws DukeException {
        String[] parsed = Parser.format(input);
        String command = parsed[0].toLowerCase();

        switch (command) {
        case "bye":
        case "quit":
        case "exit":
            return new ExitCommand();
        case "todo":
        case "event":
        case "deadline":
        case "fixed":
            String description = Parser.getTaskDescription(parsed);
            return new AddCommand(command, description);
        case "done":
            try {
                String taskSelection = parsed[1];
                if (taskSelection.equals("all")) {
                    return new DoneCommand();
                }
                int taskNumber = Integer.parseInt(taskSelection);
                return new DoneCommand(taskNumber);
            } catch (NumberFormatException nfe) {
                throw new DukeException("Invalid task number!");
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                return new InvalidCommand("You can mark tasks as done!\nE.g. 'done 1' or 'done all'");
            }
        case "list":
            return new ListCommand();
        case "delete":
        case "remove":
            try {
                String taskSelection = parsed[1];
                if (taskSelection.equals("all")) {
                    return new RemoveCommand();
                }
                int taskNumber = Integer.parseInt(taskSelection);
                return new RemoveCommand(taskNumber);
            } catch (NumberFormatException nfe) {
                throw new DukeException("Invalid task number!");
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                return new InvalidCommand("You can remove tasks from the list!\nE.g. 'remove 1' or 'remove all'");
            }
        case "find":
            try {
                return new FindCommand(parsed[1]);
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                return new InvalidCommand("You can filter your list using keywords!\nE.g. 'find assignment'");
            }
        case "help":
            return new HelpCommand();
        case "sort":
            try {
                return new SortCommand(parsed[1]);
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                return new InvalidCommand("You can sort by: name, type, datetime\nE.g. 'sort name'");
            }
        case "start":
            try {
                int taskNumber = Integer.parseInt(parsed[1]);
                return new StartTaskCommand(taskNumber, parsed[2] + " " + parsed[3]);
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                return new InvalidCommand("You can set a fixed task's date time!\nE.g. 'start 1 today 23:00'");
            } catch (NumberFormatException nfe) {
                throw new DukeException("Invalid fixed task number!");
            }
        default:
            return new InvalidCommand();
        }
    }
}
