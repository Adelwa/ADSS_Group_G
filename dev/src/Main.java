import presentationLayer.Menu;

import java.text.ParseException;

public class Main {
    public static void main(String[] args) throws ParseException {
        Menu menu = Menu.getInstance();
        menu.run();
    }
}
