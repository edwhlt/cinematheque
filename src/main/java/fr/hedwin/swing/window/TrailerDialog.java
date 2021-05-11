/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: TrailerDialog
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.window;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.view.swing.BrowserView;
import fr.hedwin.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;

public class TrailerDialog extends JDialog {

    private static final Logger logger = LoggerFactory.getLogger(TrailerDialog.class);
    public static String LICENSE = getLicenseKey();

    public static void launchTrailer(Window parent, String title, String youtubeId){
        new TrailerDialog(parent, title, youtubeId);
    }

    public TrailerDialog(Window parent, String title, String youtubeId){
        super(parent, title, Dialog.DEFAULT_MODALITY_TYPE);
        generate(youtubeId);
    }

    private static String getLicenseKey(){
        String license = null;
        try (InputStream input = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            license = prop.getProperty("jxBrowserLicense");
            logger.info("JxBrowserLicense: "+license);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if(license == null) license = "1BNDHFSC1FZ18NPW81L23QYLBV875KQF21EQI2YAQQZ78LGFU7T176KTPRNSC9BCX627ZR";
        return license;
    }

    private void generate(String youtubeId){
        Engine engins = Engine.newInstance(EngineOptions.newBuilder(HARDWARE_ACCELERATED).licenseKey(LICENSE).build());
        Browser browser = engins.newBrowser();
        browser.navigation().loadUrl("https://www.youtube.com/embed/"+youtubeId);
        BrowserView view = BrowserView.newInstance(browser);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                engins.close();
            }
        });
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        add(view, BorderLayout.CENTER);

        setSize(1280, 720);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
