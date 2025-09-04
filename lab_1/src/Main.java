public class Main {
    public static void main(String[] args) {
        if(args.length > 0 && ("--help".equals(args[0]) || "-h".equals(args[0]))) {
            printHelp();
            return;
        }
        if(args.length > 0 && "-i".equals(args[0])) {
            InteractiveMode.dialogue();
        }
        else {
            NonInteractiveMode.run();
        }
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