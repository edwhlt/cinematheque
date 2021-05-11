/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: Main
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin;

import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.extras.FlatInspector;
import com.formdev.flatlaf.extras.FlatUIDefaultsInspector;
import fr.hedwin.objects.Movie;
import fr.hedwin.objects.User;
import fr.hedwin.swing.IHMLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main {

    public static Map<UUID, Movie> movies = new HashMap<>();
    public static Map<UUID, User> users = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws URISyntaxException, IOException {
        try{
            IntelliJTheme.install(Main.class.getClassLoader().getResourceAsStream("edwin-red-dark.theme.json"));
            FlatInspector.install("ctrl shift alt X");
            FlatUIDefaultsInspector.install("ctrl shift alt Y");
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Erreur chargement de thème !");
        }

        /*for(int i = 1; i <= 100; i++){
            generateRessourceSVG("images/notif/"+i+".svg", getSVGNotif(i, Color.decode("#42A5F5")));
        }*/
        logger.info("Lancement !");

        IHMLogin ihmLogin = new IHMLogin();
        ihmLogin.setVisible(true);
    }

    public static void generateRessourceSVG(String pathName, String svgContent) throws URISyntaxException, IOException {
        String path = Main.class.getResource("/").toURI().getPath()+pathName;
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
            FileOutputStream writer = new FileOutputStream(path);
            writer.write(svgContent.getBytes());
            writer.close();
        }
    }

    public static String getSVGNotif(int number, Color color){
        String hex = "#"+Integer.toHexString(color.getRGB()).substring(2);
        StringBuilder stringBuilder = new StringBuilder().append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" viewBox=\"0 0 16 16\"><circle fill=\"").append(hex).append("\" cx=\"8\" r=\"8\" cy=\"8\">f</circle>");
        if(number > 99){
            stringBuilder.append("<text fill=\"#2F343F\" font-family=\"Verdana\" x=\"1\" y=\"10.5\" font-size=\"7\">99+</text>");
        }else if(number >= 10){
            stringBuilder.append("<text fill=\"#2F343F\" font-family=\"Verdana\" x=\"1\" y=\"12\" font-size=\"11\">").append(number).append("</text>");
        }else if(number >= 1){
            stringBuilder.append("<text fill=\"#2F343F\" font-family=\"Verdana\" x=\"4.5\" y=\"12\" font-size=\"11\">").append(number).append("</text>");
        }
        return stringBuilder.append("</svg>").toString();
    }

}