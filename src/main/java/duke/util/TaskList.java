package duke.util;

import duke.task.Task;
import duke.task.Todo;
import duke.task.Event;
import duke.task.Deadline;

import java.util.List;
import java.util.ArrayList;

/**
 * The TaskList encapsulates the list of tasks and is in charge of
 * creating tasks, adding or removing tasks from the list, marking
 * tasks as completed, and generating a print message of the list.
 */
public class TaskList {

    /** The list containing Tasks objects. */
    private final List<Task> list;

    /**
     * Constructor for TaskList. This constructor is usually
     * used when loading a list of tasks from the txt file.
     * @param list the list of tasks.
     */
    public TaskList(List<Task> list) {
        this.list = list;
    }

    /**
     * Constructor for TaskList to create an empty list.
     */
    public TaskList() {
        this.list = new ArrayList<>();
    }

    /**
     * Returns the size of the list.
     * @return the size of the list.
     */
    public int size() {
        return list.size();
    }

    /**
     * Returns the task list object.
     * @return the task list object.
     */
    public List<Task> getList() {
        return list;
    }

    /**
     * The static factory method to create the subtypes of Tasks:
     *
     *     - Todo: A simple task with a description only
     *     - Event: A task requiring attendance at a date and time
     *     - Deadline: A task requiring completion by a date and time
     *
     * Creation of these tasks are based on the type input. After which,
     * the description of the task will be parsed and handled individually
     * by the appropriate subclasses.
     * @param type the subtype of the Task to be created.
     * @param description the raw description of the task.
     * @return the created Task object.
     * @throws DukeException when creation fails usually due to improper format description.
     */
    public static Task createTask(String type, String description) throws DukeException {
        Task task = new Todo("");
        String[] split;
        String desc;
        description = description.trim();
        if (description.isEmpty()) {
            throw new DukeException("Description cannot be empty!");
        }
        switch (type) {
        case "todo":
            task = new Todo(description);
            break;
        case "deadline":
            try {
                split = description.split(" /by ");
                desc = split[0];
                String by = split[1];
                task = new Deadline(desc, by);
            } catch (IndexOutOfBoundsException ioobe) {
                throw new DukeException("Invalid " + type + " description!");
            }
            break;
        case "event":
            try {
                split = description.split(" /at ");
                desc = split[0];
                String at = split[1];
                task = new Event(desc, at);
            } catch (IndexOutOfBoundsException ioobe) {
                throw new DukeException("Invalid " + type + " description!");
            }
            break;
        }
        return task;
    }

    public void add(Task task) {
        list.add(task);
    }

    public Task get(int i) {
        return list.get(i);
    }

    /**
     * Removes the task of the given task number. The removed task
     * is then returned for the application to print or handle if required.
     * Important to note that task numbers are based on their indices in the list,
     * and tasks after a deleted task will have their indices shifted down by one value.
     * @param i the index of the task to be removed.
     * @return the removed task.
     * @throws DukeException when the index does not contain a task.
     */
    public Task remove(int i) throws DukeException {
        try {
            i--;
            return list.remove(i);
        } catch (IndexOutOfBoundsException ioobe) {
            throw new DukeException("Invalid task number!");
        }
    }

    public void removeAll() {
        list.clear();
    }

    /**
     * Marks a task as completed and returns the same task for the application
     * to print of handle if required. Indices are based on the objects' indices
     * in the list.
     * @param i the index of the task.
     * @return the task.
     * @throws DukeException when the index does not contain a task.
     */
    public Task markDone(int i) throws DukeException {
        try {
            i--;
            Task task = list.get(i);
            if (task.getDone()) {
                throw new DukeException("Task is already done!");
            } else {
                task.markAsDone();
            }
            return task;
        } catch (IndexOutOfBoundsException ioobe) {
            throw new DukeException("Invalid task number!");
        }
    }

    /**
     * Generates a print message containing the list of tasks. The message
     * will go through Ui formatting and be printed out. Example:
     *
     * >> list
     *     --------------------------------------------------------
     *     Here are the tasks in your list:
     *     1. [T][✘] Mop the floor
     *     2. [D][✘] assignment (by: Aug 26 2020, 11:59 pm)
     *     3. [E][✘] future date (at: Feb 14 2021, 07:00 pm)
     *     --------------------------------------------------------
     *
     * @return the formatted print message of the list.
     */
    public String getPrintMessage() {
        if (list.isEmpty()) {
            return "Your list is empty!";
        }
        int num = 1;
        StringBuilder msg = new StringBuilder((list.size() > 1)
                ? "Here are the tasks in your list:\n"
                : "Here's your one and only task:\n");
        for (Task task: list) {
            msg.append(String.format("%d. %s", num, task));
            if (num < list.size()) {
                msg.append("\n");
            }
            num++;
            }
        return msg.toString();
    }

    /**
     * Generates a print message containing the list of tasks with the
     * input indices. This method is used to filter out tasks to be displayed
     * to the user.
     * @param indices the list of task numbers.
     * @return the formatted print message of the filtered list.
     */
    public String getQueryResultMessage(List<Integer> indices) {
        if (indices.isEmpty()) {
            return "Search result is empty!";
        }
        StringBuilder msg = new StringBuilder();
        for (int i: indices) {
            msg.append(String.format("%d. %s", i + 1, list.get(i)));
            msg.append("\n");
        }
        return msg.toString().strip();
    }
}