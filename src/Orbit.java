import javax.swing.*;
import java.awt.*;

public class Orbit extends JPanel {
    private  int posX, posY, radius;
    private  Color color;

    public Orbit(int posX,int posY, int radius, Color color) {
        this.posX = posX;
        this.posY = posY;
        this.color = color;
        this.radius = radius;
        //Set background tranparent
        this.setOpaque(false);
        this.setBounds(450-radius,450 -radius,radius*2,radius*2);
        this.setSize(radius*2, radius*2);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.drawOval(0 , 0, 2 * radius, 2 * radius);
    }
}