package people.app;

import src.people.dao.*;
import src.people.service.PeopleService;
import src.people.control.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class ConsoleApp {
    public static void main(String[] args) {
        try {
            String storageDir = "./storage";
            String commandsDir = "./commands";
            boolean useCache = true;

            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-storage": storageDir = args[++i]; break;
                    case "-commands": commandsDir = args[++i]; break;
                    case "-cache": useCache = args[++i].equals("on"); break;
                }
            }

            System.out.println("Starting system...");
            System.out.println("Storage: " + storageDir);
            System.out.println("Commands: " + commandsDir);
            System.out.println("Cache: " + (useCache ? "enabled" : "disabled"));

            PeopleDao fileDao = new FilePeopleDao(storageDir);
            PeopleDao dao = useCache ? new CachedPeopleDao(fileDao) : fileDao;
            PeopleService service = new PeopleService(dao);

            BlockingQueue<Command> commandQueue = new ArrayBlockingQueue<>(100);

            Controller controller = new Controller(commandsDir, commandQueue);
            Dispatcher dispatcher = new Dispatcher(commandQueue, service);

            Thread controllerThread = new Thread(controller, "Controller");
            Thread dispatcherThread = new Thread(dispatcher, "Dispatcher");

            controllerThread.start();
            dispatcherThread.start();

            System.out.println("System started! Waiting for commands in folder: " + commandsDir);
            System.out.println("Press Ctrl+C to stop");

            controllerThread.join();
            dispatcherThread.join();

        } catch (Exception e) {
            System.err.println("Critical error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}