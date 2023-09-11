import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MovingHand extends JPanel {
    private int radius;
    private double scalingFactor;
    private BufferedImage handResized;
    private double angle = 0;
    private Timer timer;
    private int step;
    public MovingHand( int sizeX,int sizeY ,int radius,String imgName, double scalingFactor, int step) {
        this.radius = radius;
        this.scalingFactor = scalingFactor;
        this.step = step;

        //Temp image
        BufferedImage hand;

        //Load image
        try {
             hand = ImageIO.read(new File("/home/jearim/IdeaProjects/RelojAnalogico/res/"+imgName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Calculte new image size
        int newWidth  = (int) (hand.getWidth()  * scalingFactor);
        int newHeight = (int) (hand.getHeight() * scalingFactor);

        handResized = resizeImage(hand, newWidth, newHeight);

        //initialize component
        this.setBounds(0,0,radius*2,radius*2);
        this.setSize(radius*2,radius*2);
        this.setOpaque(false);


        //Initialize timer
        timer  = new Timer(1000 * step, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAngle();
                repaint();
            }
        });
        timer.start();

    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int newWidth, int newHeight) {
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return resizedImage;
    }

    private void updateAngle(){
        angle += Math.toRadians(6);
    }

    private void calculatePosition() {
        int offSetY = 20;
        Container parent = getParent();
        if (parent != null) {
            int x = ( (parent.getWidth() - handResized.getWidth()) /2 ) ; // Center horizontally
            int y = ( (parent.getHeight()  - handResized.getHeight()) / 2)  ; // Center vertically
            x += radius * Math.cos(angle);
            y += radius * Math.sin(angle);
            y+=offSetY;
            setLocation(x , y );
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Cast to 2dgraphics
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(handResized, 0 , 0, handResized.getWidth(), handResized.getHeight(),this);
        //Reposition image relativve to parent
        calculatePosition();

        // You may also want to update the position here based on the angle
    }
}
