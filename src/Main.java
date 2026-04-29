import services.ParkingManager;
import ui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        ParkingManager manager = new ParkingManager();
        new LoginFrame(manager);
    }
}