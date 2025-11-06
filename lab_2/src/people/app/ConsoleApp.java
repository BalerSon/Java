package people.app;

import people.dao.*;
import people.service.PeopleService;
import people.control.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class ConsoleApp {
    public static void main(String[] args) {
        try {
            // Парсим аргументы командной строки
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

            System.out.println("🚀 Запуск системы...");
            System.out.println("📁 Хранилище: " + storageDir);
            System.out.println("📁 Команды: " + commandsDir);
            System.out.println("⚡ Кэш: " + (useCache ? "включен" : "выключен"));

            // Инициализируем систему
            PeopleDao fileDao = new FilePeopleDao(storageDir);
            PeopleDao dao = useCache ? new CachedPeopleDao(fileDao) : fileDao;
            PeopleService service = new PeopleService(dao);

            // Создаем очередь команд
            BlockingQueue<Command> commandQueue = new ArrayBlockingQueue<>(100);

            // Запускаем контроллер и диспетчер в отдельных потоках
            Controller controller = new Controller(commandsDir, commandQueue);
            Dispatcher dispatcher = new Dispatcher(commandQueue, service);

            Thread controllerThread = new Thread(controller, "Controller");
            Thread dispatcherThread = new Thread(dispatcher, "Dispatcher");

            controllerThread.start();
            dispatcherThread.start();

            System.out.println("🎉 Система запущена! Ожидаю команды в папке: " + commandsDir);
            System.out.println("⏹️ Для остановки нажмите Ctrl+C");

            // Ждем завершения потоков
            controllerThread.join();
            dispatcherThread.join();

        } catch (Exception e) {
            System.err.println("❌ Критическая ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}