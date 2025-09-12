import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import org.bouncycastle.crypto.digests.SHAKEDigest;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class NonInteractiveMode {
    public static void run(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        int shakeLength = 256;
        String algorithm = null;
        String provider = "Standard";
        List<String> fileNames = new ArrayList<>();

        for (int i = 0; i < args.length; ++i) {
            switch (args[i]) {
                case "-md5":
                    algorithm = "MD5";
                    provider = "Standard";
                    break;
                case "-sha256":
                    algorithm = "SHA-256";
                    provider = "Standard";
                    break;
                case "-blake2b256":
                    algorithm = "BLAKE2b-256";
                    provider = "BC";
                    break;
                case "-blake2s256":
                    algorithm = "BLAKE2s-256";
                    provider = "BC";
                    break;
                case "-shake256":
                    algorithm = "SHAKE256";
                    provider = "BC";
                    if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                        shakeLength = Integer.parseInt(args[++i]);
                    }
                    break;
                case "-f":
                    while(i + 1 < args.length && !args[i + 1].startsWith("-")) {
                        fileNames.add(args[++i]);
                    }
                    break;
                default:
                    algorithm = "SHA-256";
                    provider = "Standard";
                    break;
            }
        }

        if (fileNames.isEmpty()) {
            System.out.println("No file found!");
            return;
        }

        for (String fileName : fileNames) {
            try {
                File file = new File(fileName);
                if (!file.exists()) {
                    System.out.println("No file " + fileName + " found!");
                    continue;
                }

                String hash;
                if ("SHAKE256".equals(algorithm)) {
                    hash = calculateShakeHash(file, shakeLength);
                } else {
                    hash = calculateHash(algorithm, provider, file);
                }
                System.out.println("Your hash: " + hash);
            } catch (Exception e) {
                System.out.println("Error with file " + fileName + e.getMessage());
            }
        }

    }

    private static String calculateHash(String algorithm, String provider, File file) throws Exception {
        MessageDigest digest;
        if ("BC".equals(provider)) {
            digest = MessageDigest.getInstance(algorithm, "BC");
        } else  {
            digest = MessageDigest.getInstance(algorithm);
        }

        try(FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) > 0) {
                digest.update(buffer, 0, bytesRead);
            }
            byte[] bytesHash = digest.digest();
            return bytesToHex(bytesHash);
        }
    }

    private static String calculateShakeHash(File file, int length) throws Exception {
        SHAKEDigest shake = new SHAKEDigest(256);
        int bytesRead;
        byte[] buffer = new byte[8192];

        try (FileInputStream fis = new FileInputStream(file)){
            while ((bytesRead = fis.read(buffer)) > 0) {
                shake.update(buffer, 0, bytesRead);
            }
            int newLength = length / 8;
            byte[] bytesHash = new byte[newLength];
            shake.doOutput(bytesHash, 0, newLength);
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
}
