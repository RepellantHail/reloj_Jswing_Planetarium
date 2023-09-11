import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Reloj extends JFrame{
    private JPanel mainContainer;
    private JLayeredPane layeredPane;
    private JPanel watchFaceContainer;
    private BufferedImage watchFaceImage;
    private BufferedImage sunImage;

    private MovingHand moon;
    Reloj(){
        setTitle("Reloj Protecto 1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,900);

        //Designwatch face
        createWatch();


        mainContainer.setLayout(new BorderLayout());
        mainContainer.setPreferredSize(new Dimension(900,900));
        mainContainer.add(layeredPane,BorderLayout.CENTER);
        add(mainContainer);
        setVisible(true);
    }

    private void createWatch() {
        double sunScalingFactor = 0.45;
        double watchfaceScalingFactor = 1.5;
        //Load Images
        try {
            watchFaceImage = ImageIO.read(new File("/home/jearim/IdeaProjects/RelojAnalogico/res/watchFaceBackground.png"));
            sunImage = ImageIO.read(new File("/home/jearim/IdeaProjects/RelojAnalogico/res/sun.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Create a panel to hold the watch face components
        watchFaceContainer = new JPanel(new BorderLayout());
        watchFaceContainer.setOpaque(false);

        //Set watchface
        JLabel watchFacelbl = createImageLabel(watchFaceImage,watchfaceScalingFactor,0,0);
        Dimension watchFaceSize = watchFacelbl.getPreferredSize();//Get size of watchface

        // Calculate the center position of watchface
        int centerX = (int) ((watchFaceSize.width  - (sunImage.getWidth()  * sunScalingFactor)) / 2);
        int centerY = (int) ((watchFaceSize.height - (sunImage.getHeight() * sunScalingFactor)) / 2);

        //Set sun Image
        JLabel sunFaceLbl = createImageLabel(sunImage,sunScalingFactor,centerX,centerY);

        //Add elements to watchface container
        watchFaceContainer.add(sunFaceLbl);//Add sun
        watchFaceContainer.add(watchFacelbl); //Add watchface image

        //Set position and size of watchFace
        //Center watchFace
        watchFaceContainer.setBounds(calculateCenter(watchFacelbl), calculateCenter(watchFacelbl), watchFaceSize.width, watchFaceSize.height);
        watchFaceContainer.setPreferredSize(watchFaceSize);


        //Create a layered pane to manage layers
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(900,900));
        layeredPane.add(watchFaceContainer,1);
        layeredPane.add(new Orbit(450,450,150, Color.WHITE),0);//Add small orbit
        layeredPane.add(new Orbit(450,450,250, Color.ORANGE),0);//Add big Orbit

        //add dynamic objects
        layeredPane.add(new MovingHand(0,0,250,"moon.png", 0.3,1),JLayeredPane.DRAG_LAYER);
        layeredPane.add(new MovingHand(0,0,150,"earth.png", 0.2,60),JLayeredPane.DRAG_LAYER);
//
//        repaintObjects();
    }

    private void repaintObjects(){
        //Threat to make repaint
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moon.repaint();
            }
        });
        timer.start();
    }

    private int calculateCenter(JLabel labelToCenter){
        int center = 900 / 2;
        return center - (labelToCenter.getWidth() / 2);
    }

    private ImageIcon resizeImage(BufferedImage imageToScale, double scaleFactor){
        int newWidth = (int) (imageToScale.getWidth() * scaleFactor);
        int newHeight = (int) (imageToScale.getHeight() * scaleFactor);
        Image scaledImage = imageToScale.getScaledInstance(newWidth,newHeight,Image.SCALE_SMOOTH);
        return  new ImageIcon(scaledImage);
    }

    private JLabel createImageLabel(BufferedImage image, double scaleFactor, int positionX, int positionY){
        ImageIcon imageToAppend = resizeImage(image,scaleFactor);
        JLabel labelWithImage = new JLabel(imageToAppend);
        labelWithImage.setBounds(
                positionX,
                positionY,
                imageToAppend.getIconWidth(),
                imageToAppend.getIconHeight()
        );
        return labelWithImage;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Reloj reloj = new Reloj();
        });
    }
}