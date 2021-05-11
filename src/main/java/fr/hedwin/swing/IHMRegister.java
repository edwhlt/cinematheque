/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: IHMRegister
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing;

import fr.hedwin.Main;
import fr.hedwin.objects.User;
import fr.hedwin.swing.panel.utils.form.Form;
import fr.hedwin.swing.panel.utils.form.FormActionEntry;
import fr.hedwin.swing.panel.utils.form.FormEntryPassword;
import fr.hedwin.swing.panel.utils.form.FormSingleEntry;
import fr.hedwin.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.UUID;

public class IHMRegister extends JFrame {

    private static Logger logger = LoggerFactory.getLogger(IHMRegister.class);

    public IHMRegister(IHMLogin ihmLogin){
        super("Inscription");
        initComponents(ihmLogin);
    }

    public void initComponents(IHMLogin ihmLogin){
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/icon.png"));
        setIconImage(icon);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                ihmLogin.setVisible(true);
            }
        });
        setResizable(false);

        FormSingleEntry<String> nameR = new FormSingleEntry<>("NOM", null, s->s, s->s);
        FormSingleEntry<String> usernameR = new FormSingleEntry<>("IDENTIFIANT", null, s->s, s->s);
        FormEntryPassword passR = new FormEntryPassword("MOT_DE_PASSE", null);
        FormEntryPassword confirmPass = new FormEntryPassword("CONFIRMéE_MOT_DE_PASSE", null, passR);
        FormActionEntry register = new FormActionEntry("S'ENREGISTRER");
        register.setValue(() -> {
            UUID uuid = UUID.randomUUID();
            Main.users.put(uuid, new User(uuid, nameR.getValue(), usernameR.getValue(), confirmPass.getValue()));
            Utils.saveJSON("users.json", Main.users);
            dispose();
            ihmLogin.setVisible(true);
        }, e -> {
            JOptionPane.showMessageDialog(this, e.getMessage(), e.getMessage(), JOptionPane.WARNING_MESSAGE);
            logger.error("Erreur inscription : ", e);
        });
        Form form = new Form("Inscription", nameR, usernameR, passR, confirmPass, register);
        form.setPreferredSize(new Dimension(250, 300));
        add(form);
        pack();
        setLocationRelativeTo(null);
    }

}
