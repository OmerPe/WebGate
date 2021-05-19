import Model.Model;

public class AppStartup {

    public static void main(String[] args) {
        Model.instance.start();
        new AppUI();
    }
}
