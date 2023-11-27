package systems.dmx.tesseract;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
// import net.sourceforge.tess4j.Tesseract1;

import systems.dmx.core.osgi.PluginActivator;
import systems.dmx.core.service.Inject;
import systems.dmx.files.FilesService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.logging.Logger;



@Path("/ocr")
public class TesseractPlugin extends PluginActivator implements TesseractService {

    // ------------------------------------------------------------------------------------------------------- Constants

    // ---------------------------------------------------------------------------------------------- Instance Variables

    @Inject
    private FilesService files;

    private Logger logger = Logger.getLogger(getClass().getName());

    // -------------------------------------------------------------------------------------------------- Public Methods

    // *** Service ***

    @GET
    @Path("/{repoPath}")
    @Produces(MediaType.TEXT_PLAIN)
    @Override
    public String doOCR(@PathParam("repoPath") String repoPath) {
        ClassLoader tccl = null;
        try {
            // ImageIO.scanForPlugins();                    // for server environment (?)
            File imageFile = files.getFile(repoPath);
            File dataDir = new File("/usr/local/Cellar/tesseract/5.2.0/share/tessdata");      // TODO: make configurable
            logger.info("### Data dir " + dataDir + ", exists=" + dataDir.exists());
            logger.info("### Reading image file " + imageFile.getAbsolutePath() + ", exists=" + imageFile.exists());
            ITesseract tesseract = new Tesseract();         // JNA Interface Mapping
            // ITesseract tesseract = new Tesseract1();     // JNA Direct Mapping (?)
            tesseract.setDatapath(dataDir.getPath());
            // tesseract.setLanguage("eng");
            //
            // For the actual OCR process we temporarily switch the thread context class loader to this bundle's class
            // loader in order to enable Java Image I/O to dynamically load the readers and writers provided by the
            // jai-imageio-core library, e.g. for TIFF support
            tccl = Thread.currentThread().getContextClassLoader();      // Thread Context Class Loader
            ClassLoader bcl = getClass().getClassLoader();              // Bundle Class Loader
            logger.info("### Classloader\ntccl = " + tccl + "\nbcl  = " + bcl);
            Thread.currentThread().setContextClassLoader(bcl);
            //
            String result = tesseract.doOCR(imageFile);
            logger.info(result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("OCR failed", e);
        } finally {
            Thread.currentThread().setContextClassLoader(tccl);         // Restore original class loader
        }
    }
}
