import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class NonInteractiveMode {
    public static void run(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        int shakeLength = 256;
        String algorithm = null;
        String provider = "Standard";
        List<String> fileNames = new ArrayList();

        for (int i = 0; i < args.length; ++i) {
            switch (args[i]) {
                case "-md5":
                    algorithm = ALG_MD5;
                    provider = "Standard";
                    break;
                case "-sha256":
                    algorithm = ALG_SHA256;
                    provider = "Standard";
                    break;
                case "-blake2b256":
                    algorithm = ALG_BLAKE2b;
                    provider = "BC";
                    break;
                case "-blake2s256":
                    algorithm = ALG_BLAKE2s;
                    provider = "BC";
                    break;
                case "-shake256":
                    algorithm = ALG_SHAKE256;
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
                    algorithm = ALG_SHA256;
                    provider = "Standard";
            }
        }

        //fileCheck & output

    }

    private static String calculateHash(String algorithm, String provider, File file, int length) throws Exception {
        MessageDigest digest;
        if ("BC".equals(provider)) {
            digest = MessageDigest.getInstance(algorithm, "BC");
        } else  {
            digest = MessageDigest.getInstance(algorithm);
        }

        try(FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            if ((bytesRead = fis.read(buffer)) > 0) {
                digest.update(buffer, 0, bytesRead);
            }
            byte[] bytesHash;
            if ("SHAKE-256".equals(algorithm)) {
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
