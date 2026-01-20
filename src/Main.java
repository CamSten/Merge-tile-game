import Infrastructure.AppManager;

public class Main {
    public static void main(String[] args) {
        startProgram();
    }
    private static void startProgram(){
        AppManager appManager = AppManager.getInstance();
        appManager.runProgram();
    }
}