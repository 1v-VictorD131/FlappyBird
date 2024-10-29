import javax.swing.*;

public class App {
    static JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::showMainMenu);
    }

    public static void showMainMenu() {
        if (frame != null) frame.dispose();
        frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setContentPane(new GameMenu());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void startGame() {
        if (frame != null) frame.dispose();
        frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setContentPane(new FlappyBird());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
