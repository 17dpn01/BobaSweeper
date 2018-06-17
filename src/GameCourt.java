import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class GameCourt extends JPanel {

    Cell[][] cells;
    Set<Integer> ids;

    boolean playing;

    int counter, time, flagCount, rightFlags;


    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;

    public static final int INTERVAL = 35;


    public GameCourt() {
        setBackground(new Color(158, 152, 152));

        counter = 0;
        time = 0;
        rightFlags = 0;

        playing = true;

        ids = new TreeSet();
        for (int i = 0; i < Cell.bobas; i++) {
            int id = (new Random()).nextInt(624);
            if (ids.contains(id)) {
                i--;
            }
            ids.add(id);

        }


        setSize(WIDTH, HEIGHT);
        cells = new Cell[25][25];

        int id = 0;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells.length; j++) {
                cells[i][j] = new Cell(i * 20, j * 20, 20, id);
                cells[i][j].iIndex = i;
                cells[i][j].jIndex = j;
                if (ids.contains(cells[i][j].id)) {
                    cells[i][j].hasBoba = true;
                }
                id++;
            }
        }

        setNeighbors();


        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (int i = 0; i < cells.length; i++) {
                    for (int j = 0; j < cells[0].length; j++) {
                        cells[i][j].mousePressed(e);
                        flagCount = countFlags();
                        if (MouseInRange(e, cells[i][j])){
                            if (cells[i][j].hasBoba) {
                                revealAll();
                                repaint();
                                return;
                            }
                            revealNeighbors(cells[i][j]);
                        }
                    }
                }
                repaint();
            }
        });

        Timer timer = new Timer(INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playing) {
                    counter += 5;
                    if (counter % 140 == 0) {
                        time++;
                        repaint();
                    }
                }
            }
        });
        timer.start();


    }

    private void setNeighbors() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                countNeighbors(cells[i][j]);
            }
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j].draw(g);
            }
        }
        if (!this.playing) {
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[0].length; j++) {
                    if (cells[i][j].flag && !cells[i][j].hasBoba && !cells[i][j].revealed) {
                        g.setColor(Color.RED);
                        g.drawString("X", cells[i][j].px + 6, cells[i][j].py + 15);
                    }
                }
            }
            g.setColor(Color.BLACK);
            Font myFont = new Font("Courier", Font.BOLD, 30);
            g.setFont(myFont);
            g.drawString("GAME OVER", WIDTH / 2 - 75, HEIGHT / 2);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Courier", Font.PLAIN, 12));
        g.drawString("Timer: " + Integer.toString(time), WIDTH / 2  - 120, HEIGHT + 15);
        g.drawString("Flagged: " + Integer.toString(Cell.bobas - this.flagCount),
                WIDTH / 2 - 15, HEIGHT + 15);

        if (this.playing && this.rightFlags == 100) {
            g.setColor(Color.BLACK);
            Font myFont = new Font("Courier", Font.BOLD, 30);
            g.setFont(myFont);
            g.drawString("NOICEEEEE!", WIDTH / 2 - 75, HEIGHT / 2);
        }
    }

    private void countNeighbors(Cell curr) {
        for (int i = Math.max(curr.iIndex - 1, 0); i <= Math.min(curr.iIndex + 1, cells.length - 1); i++) {
            for (int j = Math.max(curr.jIndex - 1, 0); j <= Math.min(curr.jIndex + 1, cells.length - 1); j++){
                if (cells[i][j].hasBoba ) curr.neighborCount++;

            }
        }
    }

    private void revealNeighbors(Cell curr) {
        if (curr.neighborCount == 0) {
            for (int i = Math.max(curr.iIndex - 1, 0); i <= Math.min(curr.iIndex + 1, cells.length - 1); i++) {
                for (int j = Math.max(curr.jIndex - 1, 0); j <= Math.min(curr.jIndex + 1, cells.length - 1); j++) {
                    if (!cells[i][j].hasBoba && !cells[i][j].revealed && !cells[i][j].flag) {
                        cells[i][j].revealed = true;
                        if (cells[i][j].neighborCount == 0)  revealNeighbors(cells[i][j]);
                    }
                }
            }
        }
    }

    private boolean MouseInRange(MouseEvent e, Cell cell) {
        return (e.getX() > cell.px && e.getX() < cell.px + cell.size
                && e.getY() > cell.py && e.getY() < cell.py + cell.size
                    && e.getClickCount() == 2);
    }

    private void revealAll() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (!cells[i][j].flag) cells[i][j].revealed = true;
            }
        }
        this.playing = false;
    }

    private int countFlags() {
        int currFlagCount = 0;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (cells[i][j].flag && !cells[i][j].revealed) {
                    currFlagCount++;
                    if (cells[i][j].hasBoba) rightFlags++;
                }
                if (cells[i][j].revealed) {
                }
            }
        }
        return currFlagCount;
    }

    public void reset() {
        Cell.bobas = 100;
        counter = 0;
        this.setVisible(false);
        this.setFocusable(false);
    }

}
