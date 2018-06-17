import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Cell {

    int px, py, size, id, neighborCount, iIndex, jIndex;

    boolean hasBoba, revealed, flag;

    public static int bobas = 50;

    static BufferedImage img;

    static final String IMG_FILE = "Assets/Images/Art Reading.png";

    public Cell(int px, int py, int size, int id) {
        this.px = px;
        this.py = py;
        this.size = size;
        this.id = id;

        iIndex = 0;
        jIndex = 0;

        flag = false;
        neighborCount = 0;
        hasBoba = false;
        revealed = false;

        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

    }

    public void draw(Graphics g) {
        //g.setColor(new Color(211, 211, 211));
        //g.fillRect(px, py, size, size);
        g.setColor(Color.BLACK);
        g.drawRect(px, py, size, size);
        if (this.revealed) {
            if (hasBoba) {
                g.setColor(new Color(183, 176, 179));
                g.fillRect(px, py, size, size);
                g.setColor(Color.BLACK);
                g.drawRect(px, py, size, size);
//                g.setColor(new Color(133, 87, 35));
//                g.fillOval(px + 4, py + 4, 12, 12);
                g.drawImage(img, px + 2, py, 18, 21, null);
            } else if (this.neighborCount != 0) {
                g.setColor(new Color(183, 176, 179));
                g.fillRect(px, py, size, size);
                g.setColor(Color.BLACK);
                g.drawRect(px, py, size, size);
                g.drawString(Integer.toString(neighborCount), px + 6, py + 15);
            } else {
                g.setColor(Color.GRAY);
                g.fillRect(px, py, size, size);
                g.setColor(Color.BLACK);
                g.drawRect(px, py, size, size);

            }
        }
        if (this.flag && !this.revealed) {
            g.setColor(new Color(166, 35, 65));
            g.drawString("P", px + 6, py + 15);
        }
    }

    public void mousePressed(MouseEvent e) {
        if (e.getX() > this.px && e.getX() < this.px + this.size
                && e.getY() > this.py && e.getY() < this.py + this.size) {
            if (e.getClickCount() == 1 && !this.revealed) {
                this.flag = !this.flag;
            } else if (e.getClickCount() == 2) {
                this.revealed = true;
            }
        }
    }

}
