import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameMenu extends JPanel {
    public GameMenu() {
        setLayout(new GridBagLayout());
        setBackground(Color.CYAN);

        JLabel titleLabel = new JLabel("Flappy Bird");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField nameField = new JTextField(15);
        JButton startButton = new JButton("Iniciar Juego");
        JButton topScoresButton = new JButton("Top Scores");
        JButton exitButton = new JButton("Salir");

        startButton.addActionListener(e -> {
            PlayerScore.setCurrentPlayerName(nameField.getText().isEmpty() ? "Player" : nameField.getText());
            App.startGame();
        });

        topScoresButton.addActionListener(e -> {
            List<String> topScores = PlayerScore.getTopScores();
            StringBuilder scoresDisplay = new StringBuilder();
            topScores.forEach(score -> scoresDisplay.append(score).append("\n"));
            JOptionPane.showMessageDialog(this, scoresDisplay.toString(), "Top Scores", JOptionPane.INFORMATION_MESSAGE);
        });

        exitButton.addActionListener(e -> System.exit(0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(titleLabel, gbc);

        gbc.gridy++;
        add(nameField, gbc);

        gbc.gridy++;
        add(startButton, gbc);

        gbc.gridy++;
        add(topScoresButton, gbc);

        gbc.gridy++;
        add(exitButton, gbc);
    }
}
