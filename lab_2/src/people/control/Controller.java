package src.people.control;

import src.people.util.JsonUtil;

import java.nio.file.*;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

public class Controller implements Runnable {
    private final Path commandsDir;
    private final BlockingQueue<Command> commandQueue;
    private volatile boolean running = true;
    private static final Pattern CMD_PATTERN = Pattern.compile("cmd-\\d+-\\w+\\.json");

    public Controller(String commandsDir, BlockingQueue<Command> commandQueue) {
        this.commandsDir = Paths.get(commandsDir);
        this.commandQueue = commandQueue;
    }

    @Override
    public void run() {
        try {
            Files.createDirectories(commandsDir);
        } catch (IOException e) {
            System.err.println("Could not create directory: " + commandsDir.toString());
            return;
        }

        while (running) {
            try {
                scanCommandsFolder();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Controller thread interrupted");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("Controller exception " + e.getMessage());
            }
        }
    }

    private void scanCommandsFolder() {
        try {
            Files.list(commandsDir)
                    .filter(path -> CMD_PATTERN.matcher(path.getFileName().toString()).matches())
                    .forEach(this::processCommandFile);
        } catch (IOException e) {
            System.err.println("Folder scanning error: " + e.getMessage());
        }
    }

    private void processCommandFile(Path filePath) {
        try {
            System.out.println("Command file: " + filePath.getFileName());

            String json = Files.readString(filePath);

            Command command = JsonUtil.fromJson(json, Command.class);

            if (isValidCommand(command)) {
                commandQueue.put(command);
                System.out.println("Command is added to the queue: " + command.getOp());

                Files.delete(filePath);
                System.out.println("File is deleted: " + filePath.getFileName());
            } else {
                System.err.println("Invalid command in file: " + filePath.getFileName());
            }

        } catch (Exception e) {
            System.err.println("File processing error " + filePath.getFileName() + ": " + e.getMessage());
        }
    }

    private boolean isValidCommand(Command command) {
        if (command == null || command.getOp() == null || command.getPayload() == null) {
            return false;
        }

        String op = command.getOp();
        return op.equals("CreateStudent") || op.equals("CreateTeacher") ||
                op.equals("UpdateStudent") || op.equals("UpdateTeacher") ||
                op.equals("Delete") || op.equals("Get");
    }

    public void stop() {
        running = false;
    }
}