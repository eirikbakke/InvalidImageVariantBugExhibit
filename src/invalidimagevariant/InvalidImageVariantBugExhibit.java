package invalidimagevariant;

import java.awt.Canvas;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BaseMultiResolutionImage;
import java.net.URL;
import java.util.Arrays;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class InvalidImageVariantBugExhibit {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(InvalidImageVariantBugExhibit::onEDT);
  }

  private static Image loadImage(String resourceName) {
    URL url = InvalidImageVariantBugExhibit.class.getResource(resourceName);
    if (url == null)
      throw new RuntimeException("Could not find resource " + resourceName);
    Image ret = Toolkit.getDefaultToolkit().getImage(url);
    if (ret == null)
      throw new RuntimeException("Failed to load image " + url);
    waitForImage(ret);
    return ret;
  }

  private static void waitForImage(Image image) {
    final MediaTracker mt = new MediaTracker(new Canvas());
    mt.addImage(image, 0);
    try {
      mt.waitForAll();
    } catch (InterruptedException e) {
      throw new RuntimeException("Unexpected interrupt ", e);
    }
    if (mt.isErrorAny()) {
      throw new RuntimeException("Unexpected MediaTracker error " +
          Arrays.toString(mt.getErrorsAny()));
    }
  }

  private static void onEDT() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | IllegalAccessException |
        InstantiationException | UnsupportedLookAndFeelException e)
    {
      throw new RuntimeException(e);
    }

    JFrame frame = new JFrame();
    frame.setSize(300, 100);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel panel = new JPanel(new FlowLayout());

    Image img1x = loadImage("/invalidimagevariant/icon_single_size.png");
    Image img2x = loadImage("/invalidimagevariant/icon_double_size.png");
    BaseMultiResolutionImage mri = new BaseMultiResolutionImage(new Image[]{ img1x, img2x });
    Icon icon = new ImageIcon(mri);

    JButton button = new JButton();
    button.setIcon(icon);
    button.setText("Test Button");
    button.setEnabled(false);
    panel.add(button);

    frame.getContentPane().add(panel);
    frame.setVisible(true);
  }
}
