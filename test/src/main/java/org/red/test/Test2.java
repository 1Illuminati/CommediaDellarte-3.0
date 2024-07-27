package org.red.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Test2 extends JPanel {

    private static final int GRID_SIZE = 3;
    private static final int TILE_SIZE = 100;
    private static final int EMPTY_TILE = GRID_SIZE * GRID_SIZE;
    private static final int BOARD_SIZE = TILE_SIZE * GRID_SIZE;
    private int[][] puzzle;
    private Point emptyPos;

    public Test2() {
        setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
        setBackground(Color.WHITE);
        setFont(new Font("SansSerif", Font.BOLD, 40));
        initializePuzzle();
        shufflePuzzle();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                int x = p.x / TILE_SIZE;
                int y = p.y / TILE_SIZE;
                moveTile(x, y);
                repaint();
                if (isSolved()) {
                    JOptionPane.showMessageDialog(Test2.this, "Puzzle Solved!");
                }
            }
        });
    }

    private void initializePuzzle() {
        puzzle = new int[GRID_SIZE][GRID_SIZE];
        int number = 1;
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                puzzle[y][x] = number++;
            }
        }
        puzzle[GRID_SIZE - 1][GRID_SIZE - 1] = EMPTY_TILE;
        emptyPos = new Point(GRID_SIZE - 1, GRID_SIZE - 1);
    }

    private void shufflePuzzle() {
        List<Integer> numbers = new ArrayList<>();
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                numbers.add(puzzle[y][x]);
            }
        }
        Collections.shuffle(numbers);
        int index = 0;
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                puzzle[y][x] = numbers.get(index++);
                if (puzzle[y][x] == EMPTY_TILE) {
                    emptyPos = new Point(x, y);
                }
            }
        }
    }

    private boolean canMoveTile(int x, int y) {
        return (Math.abs(x - emptyPos.x) == 1 && y == emptyPos.y) ||
                (Math.abs(y - emptyPos.y) == 1 && x == emptyPos.x);
    }

    private void moveTile(int x, int y) {
        if (canMoveTile(x, y)) {
            puzzle[emptyPos.y][emptyPos.x] = puzzle[y][x];
            puzzle[y][x] = EMPTY_TILE;
            emptyPos.setLocation(x, y);
        }
    }

    private boolean isSolved() {
        int number = 1;
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                if (puzzle[y][x] != number++) {
                    if (x == GRID_SIZE - 1 && y == GRID_SIZE - 1) {
                        return true;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                int number = puzzle[y][x];
                if (number != EMPTY_TILE) {
                    int xPos = x * TILE_SIZE;
                    int yPos = y * TILE_SIZE;
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(xPos, yPos, TILE_SIZE, TILE_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(xPos, yPos, TILE_SIZE, TILE_SIZE);
                    String text = String.valueOf(number);
                    FontMetrics fm = g.getFontMetrics();
                    int textX = xPos + (TILE_SIZE - fm.stringWidth(text)) / 2;
                    int textY = yPos + (TILE_SIZE - fm.getHeight()) / 2 + fm.getAscent();
                    g.drawString(text, textX, textY);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("3x3 Sliding Puzzle");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new Test2());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

