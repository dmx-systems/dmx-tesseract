package systems.dmx.tesseract;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
// import net.sourceforge.tess4j.Tesseract1;

import systems.dmx.core.osgi.PluginActivator;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.logging.Logger;



public class TesseractPlugin extends PluginActivator {

    // ------------------------------------------------------------------------------------------------------- Constants

    // ---------------------------------------------------------------------------------------------- Instance Variables

    private Logger logger = Logger.getLogger(getClass().getName());

    // -------------------------------------------------------------------------------------------------- Public Methods

    // *** Hooks ***

    @Override
    public void init() {
        try {
            // ImageIO.scanForPlugins();                    // for server environment (?)
            // File dataDir = new File("/Users/jri/Downloads/Tess4j/tessdata");
            File dataDir = new File("/usr/local/Cellar/tesseract/5.2.0/share/tessdata");      // TODO: make configurable
            File imageFile = new File("test-data/eurotext.png");
            logger.info("### Data dir " + dataDir + ", exists=" + dataDir.exists());
            logger.info("### Reading image file " + imageFile.getAbsolutePath() + ", exists=" + imageFile.exists());
            ITesseract instance = new Tesseract();          // JNA Interface Mapping
            // ITesseract instance = new Tesseract1();      // JNA Direct Mapping (?)
            instance.setDatapath(dataDir.getPath());
            // instance.setLanguage("eng");
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (Exception e) {
            throw new RuntimeException("OCR failed", e);
        }
    }

    // ------------------------------------------------------------------------------------------------- Private Methods
}
