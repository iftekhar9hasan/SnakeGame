import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame implements KeyListener {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int TILE_SIZE = 20;

    private ArrayList<Point> snake;
    private Point fruit;
    private int direction;
    private Timer timer;
    private int score;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        addKeyListener(this);

        snake = new ArrayList<>();
        snake.add(new Point(10, 10)); // Initial position of the snake
        direction = KeyEvent.VK_RIGHT; // Initial direction
        score = 0;

        placeFruit(); // Place initial fruit

        timer = new Timer(200, new ActionListener() { // Increased timer delay to 200 ms
            @Override
            public void actionPerformed(ActionEvent e) {
                move();
            }
        });
        timer.start();
    }

    private void placeFruit() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(WIDTH / TILE_SIZE); // Generate x-coordinate within grid bounds
            y = random.nextInt(HEIGHT / TILE_SIZE); // Generate y-coordinate within grid bounds
        } while (snake.contains(new Point(x, y))); // Ensure fruit doesn't overlap with snake
        fruit = new Point(x, y);
    }

    private void move() {
        // Move the snake
        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);
        switch (direction) {
            case KeyEvent.VK_UP:
                newHead.y--;
                break;
            case KeyEvent.VK_DOWN:
                newHead.y++;
                break;
            case KeyEvent.VK_LEFT:
                newHead.x--;
                break;
            case KeyEvent.VK_RIGHT:
                newHead.x++;
                break;
        }

        // Check if the new head position is within the bounds of the game area
        if (newHead.x < 0 || newHead.x >= WIDTH / TILE_SIZE ||
                newHead.y < 0 || newHead.y >= HEIGHT / TILE_SIZE) {
            gameOver();
            return;
        }

        // Check for collisions with itself
        if (snake.contains(newHead)) {
            gameOver();
            return;
        }

        snake.add(0, newHead);

        // Check if the snake eats the fruit
        if (newHead.equals(fruit)) {
            score++;
            placeFruit();
        } else {
            snake.remove(snake.size() - 1);
        }

        repaint();
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Your Score: " + score, "Game Over", JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw snake
        for (Point point : snake) {
            g.setColor(Color.GREEN);
            g.fillRect(point.x * TILE_SIZE, point.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw fruit
        g.setColor(Color.RED);
        g.fillOval(fruit.x * TILE_SIZE, fruit.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Draw score
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, HEIGHT - 10);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT ||
                key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) &&
                (key != KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) ||
                (key != KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) ||
                (key != KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT)) {
            direction = key;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SnakeGame().setVisible(true);
            }
        });
    }
}
