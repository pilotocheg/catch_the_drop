package ua.geekbrains.catch_the_drop;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame {

    private static GameWindow gameWindow;

    // Image resources
    private static Image background;
    private static Image gameOver;
    private static Image drop;

    private static long lastFrameTime;

    // Drop coordinates
    private static float dropLeft = 200;
    private static float dropTop = -100;
    /**
     * Drop's speed
     */
    private static float dropV = 200;
    /**
     * Game score
     */
    private static int score = 0;
    /**
     * Is game over or not
     */
    private static boolean isGameOver = false;

    public static void main(String[] args) throws IOException {
        // reading images
        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.png"));
        gameOver = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png"));

        // setting last frame time
        lastFrameTime = System.nanoTime();

        // create game window
        gameWindow = new GameWindow();
        gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameWindow.setLocation(200, 100);
        gameWindow.setSize(906, 478);
        gameWindow.setResizable(false);

        // creating game field
        GameField gameField = new GameField();
        // creating drop click listener
        gameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                int x = e.getX();
                int y = e.getY();
                float dropRight = dropLeft + drop.getWidth(null);
                float dropBottom = dropTop + drop.getHeight(null);
                boolean isDrop = x >= dropLeft && x <= dropRight && y >= dropTop && y <= dropBottom;
                if (isDrop) {
                    dropTop = -100;
                    dropLeft = (int) (Math.random() * (gameField.getWidth() - drop.getWidth(null)));
                    dropV = dropV + 10;
                    score++;
                    gameWindow.setTitle("Score: " + score);
                }
            }
        });
        // add game field to the game window
        gameWindow.add(gameField);
        // show game window
        gameWindow.setVisible(true);
    }

    private static void onRepaint(Graphics g) {
        // draw background
        g.drawImage(background, 0, 0, null);
        g.drawString("Score: " + score, 10, 435);
        // calculating drop position and draw it
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime - lastFrameTime) * 0.000000001f;
        lastFrameTime = currentTime;
        dropTop = dropTop + dropV * deltaTime;
        g.drawImage(drop, (int) dropLeft, (int) dropTop, null);
        // if drop is out of the window then stop the game
        if (dropTop > gameWindow.getHeight()) {
            g.drawImage(gameOver, 280, 120, null);
            isGameOver = true;
        }
    }

    private static class GameField extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            onRepaint(g);
            if (!isGameOver) {
                repaint();
            }
        }
    }
}
