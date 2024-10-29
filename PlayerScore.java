import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerScore {
    private static String currentPlayerName = "";
    private static final String SCORE_FILE = "scores.txt";

    public static void setCurrentPlayerName(String name) {
        currentPlayerName = name;
    }

    public static String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public static void saveScore(String playerName, int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE, true))) {
            writer.write(playerName + ":" + score);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getTopScores() {
        List<String> scores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scores.add(line);
            }
            Collections.sort(scores, (a, b) -> Integer.parseInt(b.split(":")[1]) - Integer.parseInt(a.split(":")[1]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scores.size() > 10 ? scores.subList(0, 10) : scores;
    }
}
