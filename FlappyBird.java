import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;
    int birdX = boardWidth / 8;
    int birdY = boardHeight / 2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
        
        int getX() {
            return x;
        }
    }

    Bird bird;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();
    Timer gameLoop;
    Timer placePipeTimer;
    boolean gameOver = false;
    boolean paused = false;
    double score = 0;

    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        bird = new Bird(birdImg);
        pipes = new ArrayList<>();

        placePipeTimer = new Timer(1500, e -> placePipes());
        placePipeTimer.start();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    void placePipes() {
        int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = boardHeight / 4;
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);
        g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);

        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 48));

        if (gameOver) {
            drawGameOverMenu(g);
        } else if (paused) {
            drawPauseMenu(g);
        } else {
            g.setFont(new Font("Arial", Font.PLAIN, 32));
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    private void drawPauseMenu(Graphics g) {
        int menuWidth = boardWidth - 100;
        int menuHeight = boardHeight / 4;
        int menuX = (boardWidth - menuWidth) / 2;
        int menuY = (boardHeight - menuHeight) / 2;

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 30, 30);

        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.YELLOW);
        String pauseText = "Juego Pausado";
        int textX = menuX + (menuWidth - g.getFontMetrics().stringWidth(pauseText)) / 2;
        int textY = menuY + 50;
        g.drawString(pauseText, textX, textY);

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.setColor(Color.WHITE);
        String resumeText = "Presiona 'Esc' para reanudar";
        int resumeTextX = menuX + (menuWidth - g.getFontMetrics().stringWidth(resumeText)) / 2;
        g.drawString(resumeText, resumeTextX, textY + 30);
    }

    private void drawGameOverMenu(Graphics g) {
        int menuWidth = boardWidth - 100;
        int menuHeight = boardHeight / 3;
        int menuX = (boardWidth - menuWidth) / 2;
        int menuY = (boardHeight - menuHeight) / 2;

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 30, 30);

        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.setColor(Color.RED);
        String gameOverText = "Game Over";
        int textX = menuX + (menuWidth - g.getFontMetrics().stringWidth(gameOverText)) / 2;
        int textY = menuY + 50;
        g.drawString(gameOverText, textX, textY);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.setColor(Color.WHITE);
        String scoreText = "Puntaje: " + (int) score;
        String restartText = "R - Reiniciar";
        String menuText = "M - Menú Principal";

        g.drawString(scoreText, menuX + (menuWidth - g.getFontMetrics().stringWidth(scoreText)) / 2, textY + 50);
        g.drawString(restartText, menuX + (menuWidth - g.getFontMetrics().stringWidth(restartText)) / 2, textY + 100);
        g.drawString(menuText, menuX + (menuWidth - g.getFontMetrics().stringWidth(menuText)) / 2, textY + 130);
    }

    public void move() {
        if (!paused) {
            velocityY += gravity;
            bird.y += velocityY;
            bird.y = Math.max(bird.y, 0);

            for (Pipe pipe : pipes) {
                pipe.x += velocityX;

                if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                    score += 0.5;
                    pipe.passed = true;
                }

                if (collision(bird, pipe)) {
                    gameOver = true;
                    PlayerScore.saveScore(PlayerScore.getCurrentPlayerName(), (int) score);
                }
            }

            if (bird.y > boardHeight) {
                gameOver = true;
                PlayerScore.saveScore(PlayerScore.getCurrentPlayerName(), (int) score);
            }
        }
    }

    boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
            velocityY = -10;
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            paused = !paused;
        }

        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            restartGame();
        }

        if (e.getKeyCode() == KeyEvent.VK_M && gameOver) {
            App.showMainMenu();
        }
    }

    private void restartGame() {
        bird.y = boardHeight / 2;
        pipes.clear();
        score = 0;
        gameOver = false;
        paused = false;
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBird game = new FlappyBird();
        frame.add(game);
        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        game.startGame();
    }

    private void startGame() {
        gameLoop.start();
    }
}
