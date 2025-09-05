import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class InteractiveMode {
    public static void dialogue() {
        Security.addProvider(new BouncyCastleProvider());
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Choose the hash algorithm:");
            System.out.println("1) MD5");
            System.out.println("2) SHA-256");
            System.out.println("3) BLAKE2s-256");
            System.out.println("4) BLAKE2b-256");
            System.out.println("5) SHAKE-256");
            System.out.print("Enter the algorithm number or 0 to quit: ");

            String algoNum = scanner.nextLine();
            if (algoNum.equals("0")) {
                System.out.println("You are welcome!");
                break;
            }

            String algorithm = null;
            String provider = "Standard";
            int outputLength = 256;

            switch(algoNum) {
                case "1":
                    algorithm = ALG_MD5;
                    provider = "Standard";
                    break;
                case "2":
                    algorithm = ALG_SHA256;
                    provider = "Standard";
                    break;
                case "3":
                    algorithm = ALG_BLAKE2s;
                    provider = "BC";
                    break;
                case "4":
                    algorithm = ALG_BLAKE2b;
                    provider = "BC";
                    break;
                case "5":
                    algorithm = ALG_SHAKE256;
                    provider = "BC";
                    System.out.print("Enter the output length multiply of 8 (256 default): ");
                    String length = scanner.nextLine();
                    if (!length.isEmpty()) {
                        try {
                            outputLength = Integer.parseInt(length);
                            if (outputLength % 8 != 0) {
                                System.out.println("The length must be multiply of 8! Use 256");
                                outputLength = 256;
                            }
                        } catch(NumberFormatException e) {
                            System.out.println("Incorrect format! Use 256");
                            outputLength = 256;
                        }
                    }
                    break;
                default:
                    System.out.println("There is no algorithm with such number. MD5 is default.");
                    algorithm = ALG_MD5;
                    provider = "Standard";
            }

            try {
                System.out.print("Write the filename: ");
                String fileName = scanner.nextLine();
                File file = new File(fileName);
                if (!file.exists() || !file.isFile()) {
                    System.out.println("File does not exist!");
                    continue;
                }

                String hash = calculateHash(file, algorithm, provider, outputLength);
                System.out.println("Your hash: ");
                System.out.println(hash);
            } catch(Exception e) {
                System.out.println("Invalid file " + e.getMessage());
            }

            System.out.print("Do you want to continue with another file? (y/n): ");
            String decision = scanner.nextLine();
            if (!decision.equalsIgnoreCase("y")) {
                System.out.println("You are welcome!");
                break;
            }
        }
        scanner.close();
    }

    private static String calculateHash(File file, String algorithm, String provider, int length) throws Exception{
        MessageDigest digest;
        if ("BC".equals(provider)) {
            digest = MessageDigest.getInstance(algorithm, "BC");
        } else {
            digest = MessageDigest.getInstance(algorithm);
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) > 0) {
                digest.update(buffer, 0, bytesRead);
            }
            byte[] bytesHash;
            if ("ALG_SHAKE256".equals(algorithm)) {
                bytesHash = digest.digest(new byte[length / 8]);
            } else {
                bytesHash = digest.digest();
            }
            return bytesToHex(bytesHash);
        }
    }

    private static String bytesToHex(byte[] bytesHash) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytesHash) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    private static final String ALG_MD5 = "MD5";
    private static final String ALG_SHA256 = "SHA-256";
    private static final String ALG_BLAKE2b = "BLAKE2b-256";
    private static final String ALG_BLAKE2s = "BLAKE2s-256";
    private static final String ALG_SHAKE256 = "SHAKE256";
}