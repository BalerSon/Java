import java.util.Scanner;

public class InteractiveMode {
    public static void dialogue() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Choose the hash algorithm:");
            System.out.println("1) MD5");
            System.out.println("2) SHA-256");
            System.out.println("3) BLAKE2s-256");
            System.out.println("4) BLAKE2b-256");
            System.out.println("5) SHAKE-256");
            System.out.print("Enter the algorithm number or 0 to quit: ");

            String algoName = scanner.nextLine();
            if (algoName.equals("0")) {
                System.out.println("You are welcome!");
                break;
            }

            System.out.print("Write the filename: ");
            String fileName = scanner.nextLine();

            //function call

            System.out.print("Do you want to continue with another file? (y/n): ");
            String decision = scanner.nextLine();
            if (!decision.equalsIgnoreCase("y")) {
                break;
            }
        }
    }
}