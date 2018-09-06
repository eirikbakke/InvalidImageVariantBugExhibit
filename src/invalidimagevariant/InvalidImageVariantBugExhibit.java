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

/*
    Demonstration of "Invalid Image variant" exception when attempting to use a MultiResolutionImage
    on Windows 10 on Java 10.0.2.

      Setup:
      * Windows 10
      * OpenJDK 10.0.2.
      * A HiDPI laptop screen set at scaling "200%" in the Windows "Display" settings app, with a
        regular non-HiDPI external monitor as the main display.
        (Tested on a Lenovo X1 Carbon 6th gen laptop.)

      The situation in which the bug was consistently encountered was as follows:
      1) Start this application. The JFrame will appear on the primary monitor. The icon in the
         button says "ICON 1x".
      2) Now drag the Window over to the HiDPI screen. The exception below occurs:

    Exception in thread "AWT-EventQueue-0" java.lang.IllegalArgumentException: Invalid Image variant
      at java.desktop/sun.awt.image.SurfaceManager.getManager(SurfaceManager.java:82)
      at java.desktop/sun.java2d.SurfaceData.getSourceSurfaceData(SurfaceData.java:218)
      at java.desktop/sun.java2d.pipe.DrawImage.renderImageScale(DrawImage.java:635)
      at java.desktop/sun.java2d.pipe.DrawImage.tryCopyOrScale(DrawImage.java:319)
      at java.desktop/sun.java2d.pipe.DrawImage.transformImage(DrawImage.java:258)
      at java.desktop/sun.java2d.pipe.DrawImage.copyImage(DrawImage.java:76)
      at java.desktop/sun.java2d.pipe.DrawImage.copyImage(DrawImage.java:1027)
      at java.desktop/sun.java2d.SunGraphics2D.drawImage(SunGraphics2D.java:3415)
      at java.desktop/sun.java2d.SunGraphics2D.drawImage(SunGraphics2D.java:3391)
      at java.desktop/javax.swing.ImageIcon.paintIcon(ImageIcon.java:425)
      at java.desktop/javax.swing.plaf.basic.BasicButtonUI.paintIcon(BasicButtonUI.java:358)
      at java.desktop/javax.swing.plaf.basic.BasicButtonUI.paint(BasicButtonUI.java:275)
      at java.desktop/com.sun.java.swing.plaf.windows.WindowsButtonUI.paint(WindowsButtonUI.java:167)
      at java.desktop/javax.swing.plaf.ComponentUI.update(ComponentUI.java:161)
      at java.desktop/javax.swing.JComponent.paintComponent(JComponent.java:797)
      at java.desktop/javax.swing.JComponent.paint(JComponent.java:1074)
      at java.desktop/javax.swing.JComponent.paintChildren(JComponent.java:907)
      at java.desktop/javax.swing.JComponent.paint(JComponent.java:1083)
      at java.desktop/javax.swing.JComponent.paintChildren(JComponent.java:907)
      at java.desktop/javax.swing.JComponent.paint(JComponent.java:1083)
      at java.desktop/javax.swing.JComponent.paintChildren(JComponent.java:907)
      at java.desktop/javax.swing.JComponent.paint(JComponent.java:1083)
      at java.desktop/javax.swing.JLayeredPane.paint(JLayeredPane.java:590)
      at java.desktop/javax.swing.JComponent.paintChildren(JComponent.java:907)
      at java.desktop/javax.swing.JComponent.paintToOffscreen(JComponent.java:5262)
      at java.desktop/javax.swing.RepaintManager$PaintManager.paintDoubleBufferedImpl(RepaintManager.java:1633)
      at java.desktop/javax.swing.RepaintManager$PaintManager.paintDoubleBuffered(RepaintManager.java:1608)
      at java.desktop/javax.swing.RepaintManager$PaintManager.paint(RepaintManager.java:1546)
      at java.desktop/javax.swing.RepaintManager.paint(RepaintManager.java:1313)
      at java.desktop/javax.swing.JComponent.paint(JComponent.java:1060)
      at java.desktop/java.awt.GraphicsCallback$PaintCallback.run(GraphicsCallback.java:39)
      at java.desktop/sun.awt.SunGraphicsCallback.runOneComponent(SunGraphicsCallback.java:78)
      at java.desktop/sun.awt.SunGraphicsCallback.runComponents(SunGraphicsCallback.java:115)
      at java.desktop/java.awt.Container.paint(Container.java:2000)
      at java.desktop/java.awt.Window.paint(Window.java:3940)
      at java.desktop/javax.swing.RepaintManager$4.run(RepaintManager.java:868)
      at java.desktop/javax.swing.RepaintManager$4.run(RepaintManager.java:840)
      at java.base/java.security.AccessController.doPrivileged(Native Method)
      at java.base/java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:87)
      at java.desktop/javax.swing.RepaintManager.paintDirtyRegions(RepaintManager.java:840)
      at java.desktop/javax.swing.RepaintManager.paintDirtyRegions(RepaintManager.java:815)
      at java.desktop/javax.swing.RepaintManager.prePaintDirtyRegions(RepaintManager.java:764)
      at java.desktop/javax.swing.RepaintManager.access$1200(RepaintManager.java:69)
      at java.desktop/javax.swing.RepaintManager$ProcessingRunnable.run(RepaintManager.java:1880)
      at java.desktop/java.awt.event.InvocationEvent.dispatch(InvocationEvent.java:313)
      at java.desktop/java.awt.EventQueue.dispatchEventImpl(EventQueue.java:770)
      at java.desktop/java.awt.EventQueue.access$600(EventQueue.java:97)
      at java.desktop/java.awt.EventQueue$4.run(EventQueue.java:721)
      at java.desktop/java.awt.EventQueue$4.run(EventQueue.java:715)
      at java.base/java.security.AccessController.doPrivileged(Native Method)
      at java.base/java.security.ProtectionDomain$JavaSecurityAccessImpl.doIntersectionPrivilege(ProtectionDomain.java:87)
      at java.desktop/java.awt.EventQueue.dispatchEvent(EventQueue.java:740)
      at java.desktop/java.awt.EventDispatchThread.pumpOneEventForFilters(EventDispatchThread.java:203)
      at java.desktop/java.awt.EventDispatchThread.pumpEventsForFilter(EventDispatchThread.java:124)
      at java.desktop/java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:113)
      at java.desktop/java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:109)
      at java.desktop/java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:101)
      at java.desktop/java.awt.EventDispatchThread.run(EventDispatchThread.java:90)
*/
public class InvalidImageVariantBugExhibit {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(InvalidImageVariantBugExhibit::onEDT);
  }

  private static Image loadImage(String resourceName) {
    URL url = InvalidImageVariantBugExhibit.class.getResource(resourceName);
    if (url == null)
      throw new RuntimeException("Could not find resource URL for " + resourceName);
    Image ret = Toolkit.getDefaultToolkit().getImage(url);
    if (ret == null)
      throw new RuntimeException("Failed to load image " + url);
    /* Commenting out this may cause the "Invalid Image variant" exception to be thrown even when
    "button.setEnabled(false)" is omitted. It may also cause incorrect icon positioning even on
    regular non-HiDPI displays, as shown in the screenshot in
    WrongIconPositionIfNotWaitingForImageToLoad.png. */
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
    BaseMultiResolutionImage mri = new BaseMultiResolutionImage(new Image[] { img1x, img2x });
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
