package systems.dmx.tesseract;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
// import net.sourceforge.tess4j.Tesseract1;

import systems.dmx.core.osgi.PluginActivator;
import systems.dmx.core.service.Inject;
import systems.dmx.files.FilesService;

// import javax.imageio.ImageIO;
import java.io.File;
import java.util.logging.Logger;



public class TesseractPlugin extends PluginActivator implements TesseractService {

    // ------------------------------------------------------------------------------------------------------- Constants

    private static final String DATA_PATH = System.getProperty("dmx.tesseract.data_path");

    // ---------------------------------------------------------------------------------------------- Instance Variables

    @Inject
    private FilesService files;

    private Logger logger = Logger.getLogger(getClass().getName());

    // -------------------------------------------------------------------------------------------------- Public Methods

    // *** Service ***

    @Override
    public String doOCR(String repoPath) {
        ClassLoader tccl = null;
        try {
            if (DATA_PATH == null) {
                throw new RuntimeException("dmx.tesseract.data_path configuration is missing");
            }
            // ImageIO.scanForPlugins();                    // for server environment (?)
            File imageFile = files.getFile(repoPath);
            logger.info("### Tesseract data path: " + DATA_PATH + ", exists=" + new File(DATA_PATH).exists());
            logger.info("### Reading image file " + imageFile.getAbsolutePath() + ", exists=" + imageFile.exists());
            ITesseract tesseract = new Tesseract();         // JNA Interface Mapping
            // ITesseract tesseract = new Tesseract1();     // JNA Direct Mapping (?)
            tesseract.setDatapath(DATA_PATH);
            // tesseract.setLanguage("eng");
            //
            // For the actual OCR process (e.g. for reading TIFF images) we temporarily switch the thread context class
            // loader to this bundle's class loader in order to enable Java Image I/O to dynamically load the readers
            // and writers provided by the jai-imageio-core library embedded in this bundle
            tccl = Thread.currentThread().getContextClassLoader();      // Thread Context Class Loader
            ClassLoader bcl = getClass().getClassLoader();              // Bundle Class Loader
            logger.fine("### Classloader\ntccl = " + tccl + "\nbcl  = " + bcl);
            Thread.currentThread().setContextClassLoader(bcl);
            //
            String text = tesseract.doOCR(imageFile);
            logger.info("\"" + text + "\"");
            return text;
        } catch (Exception e) {
            throw new RuntimeException("Tesseract OCR failed", e);
        } finally {
            Thread.currentThread().setContextClassLoader(tccl);         // Restore original class loader
        }
    }
}
