/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: RequestListForm
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.other.jlist;

import fr.hedwin.swing.IHM;
import fr.hedwin.swing.panel.utils.form.Form;
import fr.hedwin.swing.panel.utils.form.FormActionEntry;
import fr.hedwin.swing.panel.utils.form.FormEntry;
import fr.hedwin.utils.fonctional.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RequestListForm {

    private Form form;
    private JPanel jPanel;
    private String name;
    private FormActionEntry search;

    private static Logger logger = LoggerFactory.getLogger(RequestListForm.class);

    public RequestListForm(JPanel jPanel, String name, Callable formSucces, FormEntry<?, ?>... formEntries){
        this.jPanel = jPanel;
        this.name = name;
        this.search = new FormActionEntry("Rechercher", formSucces, e -> {
            JOptionPane.showMessageDialog(IHM.INSTANCE, e.getMessage(), "Erreur", JOptionPane.WARNING_MESSAGE);
            logger.error("Erreur formulaire de recherche :", e);
        });
        List<FormEntry<?, ?>> list = Arrays.stream(formEntries).collect(Collectors.toCollection(LinkedList::new));
        list.add(search);
        this.form = new Form("Rechercher", list.toArray(new FormEntry[]{}));
    }

    public Form getForm() {
        return form;
    }

    public String getName() {
        return name;
    }

    public FormActionEntry actionSucess() {
        return search;
    }

    public void open(){
        Arrays.stream(jPanel.getComponents()).filter(Form.class::isInstance).forEach(form -> jPanel.remove(form));
        form.addEntries();
        jPanel.add(form, BorderLayout.NORTH);
        jPanel.repaint();
        jPanel.revalidate();
    }


}
