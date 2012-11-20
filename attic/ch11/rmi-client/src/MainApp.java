import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.grailsinaction.RemotePostService;

public class MainApp {
    public static void main(String[] args) {
        // Check that we have at least one argument.
        if (args.length == 0 || args[0].trim().length() == 0) {
            System.out.println("You must provide the message as the first argument.");
            System.exit(1);
        }

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1199);
            RemotePostService postService = (RemotePostService) registry.lookup("PostService");
            long postId = postService.createPost("peter", args[0]);

            System.out.println("Created new post with ID '" + postId + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
