/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: ClientHttp
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.hedwin.db.utils.CompletableFuture;
import fr.hedwin.db.utils.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ClientHttp {
    private HttpURLConnection connexion;
    private String reception;

    private static final Logger logger = LoggerFactory.getLogger(ClientHttp.class);

    public static <T> Future<T> executeRequest(TmdbURL adresse, TypeReference<T> typeReference) {
        return CompletableFuture.async(() -> {
            logger.info("HTTP Request: "+adresse.getLink());
            ClientHttp clientHttp = new ClientHttp();
            clientHttp.connection(adresse.getLink(), "GET");
            clientHttp.executeRequest();
            try {
                return new ObjectMapper().readValue(clientHttp.getResult(), typeReference);
            } catch (JsonProcessingException e) {
                throw new Exception("Impossible d'accèder au résultat ! "+e.getMessage());
            }
        });
    }

    public static <T> Future<T> executeRequest(TmdbURL adresse, Class<T> typeReference) {
        return CompletableFuture.async(() -> {
            logger.info("HTTP Request: "+adresse.getLink());
            ClientHttp clientHttp = new ClientHttp();
            clientHttp.connection(adresse.getLink(), "GET");
            clientHttp.executeRequest();
            try {
                return new ObjectMapper().readValue(clientHttp.getResult(), typeReference);
            } catch (JsonProcessingException e) {
                throw new Exception("Impossible d'accèder au résultat !"+e.getMessage());
            }
        });
    }

    public static Image loadImage(String adresse) throws Exception {
        ClientHttp clientHttp = new ClientHttp();
        clientHttp.connection(adresse, "GET");
        return clientHttp.getImage();
    }

    public boolean connection(String adresse, String methode) {
        URL url;
        try {
            url = new URL(adresse);
        } catch (MalformedURLException ex) {
            return false;
        }
        try {
            connexion = (HttpURLConnection) url.openConnection();
            connexion.setDoOutput(true);
            connexion.setDoInput(true);
            connexion.setRequestMethod(methode);
            connexion.setRequestProperty("charset", "UTF-8");
            connexion.connect();
        } catch (IOException ex) {
            return false;
        }
        //System.out.println("connexion OK");
        return true;
    }
    public boolean executeRequest() {
        InputStream inp = null;
        total = "";
        try {
            inp = connexion.getInputStream();
        } catch (IOException ex) {
            //System.out.println("reception pas OK");
            return false;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(inp));
        try {
            int i = 0;
            while ((reception = in.readLine()) != null) {
                total += reception + "\n";
                //System.out.println("ligne " + i + " " + reception);
                i++;
            }
        } catch (IOException ex) {
            //System.out.println("erreur reception");
            return false;
        }
        return true;
    }

    private String total = "";
    public String getResult() {
        return total;
    }

    public Image getImage() throws Exception{
        InputStream is = null;
        try {
            is = new BufferedInputStream(connexion.getInputStream());
            BufferedImage image = ImageIO.read(is);
            is.close();
            return image;
            //ImageIO.write(image, "jpg", new File("src/images/image.jpg"));
        } catch (IOException ex) {
            throw ex;
            //Logger.getLogger(ClientHttp.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                throw ex;
                //Logger.getLogger(ClientHttp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
