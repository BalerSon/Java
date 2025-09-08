public class Main {
    public static void main(String[] args) {
        if(hasFlag(args, "-h") || hasFlag(args, "-help")) {
            printHelp();
            return;
        }
        if(hasFlag(args, "-i")) {
            InteractiveMode.dialogue();
        }
        else {
            NonInteractiveMode.run(args);
        }
    }

    private static boolean hasFlag(String[] args, String flag) {
        for(String arg : args) {
            if(flag.equals(arg)) {
                return true;
            }
        }
        return false;
    }

    private static void printHelp() {
        System.out.println("Options");
        System.out.println("  -i               Interactive mode");
        System.out.println("  -md5             Use MD5 algorithm");
        System.out.println("  -blake2b256      Use BLAKE2b-256 algorithm");
        System.out.println("  -blake2s256      Use BLAKE2s-256 algorithm");
        System.out.println("  -shake256 [bits] Use SHAKE-256 algorithm (optimally with bit length)");
        System.out.println("  -sha256          Use SHA-256 algorithm (default)");
        System.out.println("  -f <file>...     Specify files to hash");
        System.out.println("  -h, --help       Show this help message");
    }
}