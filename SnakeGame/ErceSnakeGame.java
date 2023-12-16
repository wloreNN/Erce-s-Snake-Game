import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class ErceSnakeGame extends JPanel implements ActionListener, KeyListener {
    private ArrayList<Point> snake;
    private Point food;
    private int direction;
    private boolean isGameOver;
    private Timer timer;
    private int score;
    private boolean isPaused;

    private static final int CELL_SIZE = 30;
    private static final int BOARD_WIDTH = 20;
    private static final int BOARD_HEIGHT = 15;
    private static final int INITIAL_SPEED = 125;
    private static final int SCORE_INCREMENT = 10;
    
    
    private Color snakeColorHead = Color.RED;
    private Color snakeColorBody = Color.PINK;

    public ErceSnakeGame() {
        snake = new ArrayList<Point>();
        direction = KeyEvent.VK_RIGHT;
        isGameOver = false;
        isPaused = false;
        score = 0;


        snake.add(new Point(5, 5));
        snake.add(new Point(4, 5));
        snake.add(new Point(3, 5));

        placeFood();

        timer = new Timer(INITIAL_SPEED, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
        setPreferredSize(new Dimension(CELL_SIZE * BOARD_WIDTH, CELL_SIZE * BOARD_HEIGHT));
    }

    public void actionPerformed(ActionEvent e) {
        if (!isGameOver && !isPaused) {
            move();
            checkCollision();
            repaint();
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (!isGameOver) {
            if (key == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) {
                direction = KeyEvent.VK_LEFT;
            } else if (key == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) {
                direction = KeyEvent.VK_RIGHT;
            } else if (key == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) {
                direction = KeyEvent.VK_UP;
            } else if (key == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP) {
                direction = KeyEvent.VK_DOWN;
            } else if (key == KeyEvent.VK_P) {
                togglePause();
            }
        } else {
            if (key == KeyEvent.VK_R) {
                restartGame();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isGameOver) {
            drawGrid(g);
            drawSnake(g);
            drawFood(g);
        } else {
            drawGameOverScreen(g);
        }

        if (isPaused) {
            drawPauseScreen(g);
        }
    }

    private void drawGrid(Graphics g) {
        for (int x = 0; x < BOARD_WIDTH; x++) {
            for (int y = 0; y < BOARD_HEIGHT; y++) {
                g.setColor((x + y) % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                g.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void drawSnake(Graphics g) {
        for (int i = 0; i < snake.size(); i++) {
            Point point = snake.get(i);
            int x = point.x * CELL_SIZE;
            int y = point.y * CELL_SIZE;
            
            
            g.setColor(i == 0 ? snakeColorHead : snakeColorBody);
            g.fillRoundRect(x, y, CELL_SIZE, CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    private void drawFood(Graphics g) {
        int x = food.x * CELL_SIZE;
        int y = food.y * CELL_SIZE;
        g.setColor(Color.GREEN);
        g.fillRoundRect(x, y, CELL_SIZE, CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    private void drawGameOverScreen(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.setColor(Color.RED);
        String message = "Game Over!";
        String scoreMessage = "Score: " + score;
        String restartMessage = "Press R to restart";
        g.drawString(message, 150, 100);
        g.drawString(scoreMessage, 150, 150);
        g.drawString(restartMessage, 100, 250);
    }

    private void drawPauseScreen(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.setColor(Color.RED);
        String message = "Paused";
        String unpauseMessage = "Press P to resume";
        g.drawString(message, 180, 150);
        g.drawString(unpauseMessage, 100, 200);
    }

    private void move() {
        Point newHead = snake.get(0).getLocation();
        switch (direction) {
            case KeyEvent.VK_LEFT:
                newHead.x -= 1;
                break;
            case KeyEvent.VK_RIGHT:
                newHead.x += 1;
                break;
            case KeyEvent.VK_UP:
                newHead.y -= 1;
                break;
            case KeyEvent.VK_DOWN:
                newHead.y += 1;
                break;
        }
        snake.add(0, newHead);
        if (newHead.equals(food)) {
            score += SCORE_INCREMENT;   
            placeFood();
        } else {
            snake.remove(snake.size() - 1);
        }

        
    }

    private void placeFood() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(BOARD_WIDTH);
            y = rand.nextInt(BOARD_HEIGHT);
        } while (snake.contains(new Point(x, y)));
        food = new Point(x, y);
    }

    private void checkCollision() {
        Point head = snake.get(0);
        if (head.x < 0 || head.x >= BOARD_WIDTH || head.y < 0 || head.y >= BOARD_HEIGHT || snake.subList(1, snake.size()).contains(head)) {
            gameOver();
        }
    }

    private void restartGame() {
        snake.clear();
        direction = KeyEvent.VK_RIGHT;
        isGameOver = false;
        score = 0;
        snake.add(new Point(5, 5));
        snake.add(new Point(4, 5));
        snake.add(new Point(3, 5));
        placeFood();
        timer.setDelay(INITIAL_SPEED);
        isPaused = false;
        repaint();
    }

    private void togglePause() {
        isPaused = !isPaused;
        repaint();
    }

   

    private void gameOver() {
        isGameOver = true;
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Erce's Snake Game");
            ErceSnakeGame game = new ErceSnakeGame();
            frame.add(game);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.addKeyListener(game);
        });
    }
}