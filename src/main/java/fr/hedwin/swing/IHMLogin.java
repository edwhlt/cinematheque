/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: IHMLogin
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing;

import com.fasterxml.jackson.core.type.TypeReference;
import fr.hedwin.Main;
import fr.hedwin.db.utils.CompletableFuture;
import fr.hedwin.objects.Movie;
import fr.hedwin.objects.User;
import fr.hedwin.swing.other.LoadDataBar;
import fr.hedwin.swing.panel.utils.form.Form;
import fr.hedwin.swing.panel.utils.form.FormActionEntry;
import fr.hedwin.swing.panel.utils.form.FormEntryPassword;
import fr.hedwin.swing.panel.utils.form.FormSingleEntry;
import fr.hedwin.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class IHMLogin extends JFrame {

    private static Logger logger = LoggerFactory.getLogger(IHMLogin.class);

    private final LoadDataBar loadDataBar = new LoadDataBar();
    public static IHMLogin INSTANCE;

    public IHMLogin(){
        super("Connexion");
        this.INSTANCE = this;
        initComponents();
    }

    public void initComponents() {
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icon.png"));
        setIconImage(icon);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        FormSingleEntry<String> name = new FormSingleEntry<>("IDENTIFIANT", null, s->s, s->s);
        FormEntryPassword pass = new FormEntryPassword("MOT_DE_PASSE", null);
        FormActionEntry login = new FormActionEntry("CONNEXION");
        login.setValue(() -> {
            loadDataBar.initialize();
            CompletableFuture.async(() -> {
                if(Main.users.isEmpty()){
                    try {
                        Utils.loadJSON("users.json", new TypeReference<Map<UUID, User>>() {}, users -> Main.users = users);
                    }catch (Exception e){
                        logger.error("Impossible de charger les utilisateurs ! ", e);
                    }
                }
                User user = null;
                for (User u : Main.users.values()) {
                    if(u.getUsername().equals(name.getValue()) && u.getPassword().equals(pass.getValue())) user = u;
                }
                if(user == null) throw new Exception("Identifiant ou mot de passe incorrect !");
                //OUVERTURE DE LA FENETRE

                IHM ihm = new IHM(user);
                //CHARGEMENT DES DONNEES
                Utils.loadJSON("datas.json", new TypeReference<Map<UUID, Movie>>() {}, movies -> Main.movies = movies);
                Main.movies.forEach(ihm.getCinematheque().getTable()::addRow);
                return ihm;
            }).then(ihm -> {
                loadDataBar.close();
                ihm.setVisible(true);
                dispose();
            }).error(e -> {
                name.setOutline("error");
                pass.setOutline("error");
                loadDataBar.close();
                JOptionPane.showMessageDialog(this, e.getMessage(), e.getMessage(), JOptionPane.WARNING_MESSAGE);
                logger.error("Erreur connection : ", e);
            });
        }, (e) -> {});


        FormActionEntry singin = new FormActionEntry("INSCRIPTION", () -> {
            IHMRegister ihmRegister = new IHMRegister(this);
            ihmRegister.setVisible(true);
            setVisible(false);
        }, (e) -> {});


        Form form = new Form("Login", name, pass, login, singin);
        form.setPreferredSize(new Dimension(250, 200));
        add(new JPanel(new BorderLayout()){{
            add(loadDataBar, BorderLayout.NORTH);
            add(form, BorderLayout.CENTER);
        }});
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
