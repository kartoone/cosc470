import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GrayscaleDrawingApp extends JFrame {
    private final BufferedImage image = new BufferedImage(280, 280, BufferedImage.TYPE_BYTE_GRAY);
    private final BufferedImage smallImage = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_GRAY);
    private Graphics2D g2d = image.createGraphics();
    private boolean isDrawing = false;

    public GrayscaleDrawingApp() {
        super("Grayscale Drawing App");
        setupDrawingArea();
        setupButtons();
        setSize(320, 360);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        clearDrawingArea(); // Initialize drawing area with a black background
    }

    private void setupDrawingArea() {
        JPanel drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, this);
            }
        };
        drawingPanel.setPreferredSize(new Dimension(280, 280));
        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isDrawing = true;
                drawBrush(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDrawing = false;
            }
        });
        drawingPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDrawing) {
                    drawBrush(e.getX(), e.getY());
                }
            }
        });
        add(drawingPanel);
    }

    private void drawBrush(int x, int y) {
        g2d.setPaint(Color.WHITE); 
        g2d.fillOval(x - 10, y - 10, 20, 20); // Adjust brush size and shape if needed
        repaint();
    }

    private void setupButtons() {
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveDrawing());
        add(saveButton);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearDrawingArea());
        add(clearButton);
    }

    private void clearDrawingArea() {
        g2d.setPaint(Color.BLACK); // Set the background to black
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        repaint();
    }

    private void saveDrawing() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Drawing");
        fileChooser.setSelectedFile(new File("drawing.png"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG Image", "png"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".png")) {
                fileToSave = new File(fileToSave + ".png");
            }

            try {
                Graphics2D g2dSmall = smallImage.createGraphics();
                g2dSmall.drawImage(image, 0, 0, 28, 28, null);
                g2dSmall.dispose();
                ImageIO.write(smallImage, "png", fileToSave);
                JOptionPane.showMessageDialog(this, "Drawing saved as " + fileToSave.getName(), "Save Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving image: " + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GrayscaleDrawingApp::new);
    }
}
