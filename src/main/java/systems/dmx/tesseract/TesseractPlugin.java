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
        try {
            // ImageIO.scanForPlugins();                    // for server environment (?)
            File imageFile = files.getFile(repoPath);
            File dataDir = new File("/usr/local/Cellar/tesseract/5.2.0/share/tessdata");      // TODO: make configurable
            logger.info("### Data dir " + dataDir + ", exists=" + dataDir.exists());
            logger.info("### Reading image file " + imageFile.getAbsolutePath() + ", exists=" + imageFile.exists());
            ITesseract instance = new Tesseract();          // JNA Interface Mapping
            // ITesseract instance = new Tesseract1();      // JNA Direct Mapping (?)
            instance.setDatapath(dataDir.getPath());
            // instance.setLanguage("eng");
            String result = instance.doOCR(imageFile);
            logger.info(result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("OCR failed", e);
        }
    }
}
