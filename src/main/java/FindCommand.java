import java.util.List;
import java.util.LinkedList;

/**
 * The find command allows user to search for tasks with descriptions that
 * contain the query string. Currently, the implementation allows for all
 * containment including substrings. For example, the query "end" will bring
 * up descriptions like "friend" and "enderman" instead of matching full strings.
 *
 * Future implementation can force descriptions to be split and match exact elements.
 *
 */
public class FindCommand implements Command {

    private final String query;

    public FindCommand(String query) {
        this.query = query;
    }

    @Override
    public boolean isExit() {
        return false;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Integer> indices = new LinkedList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            if (t.getDescription().contains(query)) {
                indices.add(i);
            }
        }
        String msg = tasks.getQueryResultMessage(indices);
        ui.sendMessage(msg);
    }
}
