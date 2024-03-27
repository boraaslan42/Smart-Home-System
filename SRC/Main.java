
public class Main {
    static String outputFileName;
    static String inputFileName;
    public static void main(String[] args) {
        outputFileName = args[1];
        inputFileName = args[0];

        MainCoordinator.run();
        OutputWriter.close();
    }
}