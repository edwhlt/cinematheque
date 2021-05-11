/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: Form
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.form;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Form extends JPanel {

    private final Map<String, FormEntry<?, ?>> entries;
    private final Map<String, FormActionEntry> actionsMap;
    public final GridBagConstraints gbc;

    public Form(String name, FormEntry<?, ?>... formEntries){
        this.entries = Arrays.stream(formEntries).filter(e -> !(e instanceof FormActionEntry)).collect(Collectors.toMap(e -> e.label, e -> e, (x, y) -> y, LinkedHashMap::new));
        this.actionsMap = Arrays.stream(formEntries).filter(FormActionEntry.class::isInstance).map(FormActionEntry.class::cast).collect(Collectors.toMap(e -> e.label, e -> e, (x, y) -> y, LinkedHashMap::new));
        this.gbc = new GridBagConstraints();
        setBorder(new EmptyBorder(10, 20, 10, 20));
        //setLayout(new GridLayout(0, 1));
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(new GridBagLayout());
        addEntries();
    }

    public void addEntries(){
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridy = 0;
        gbc.gridx = 0;
        removeAll();
        entries.values().forEach(e -> {
            if(gbc.gridy > 0) gbc.insets = new Insets(20,5,0,0);
            else gbc.insets = new Insets(0,5,0,0);
            e.getComponents().forEach(component -> {
                if(!(component instanceof JLabel)) gbc.insets = new Insets(0,0,0,0);
                add(component, gbc);
                gbc.gridy += 1;
            });
        });
        JPanel jPanel = new JPanel(new GridLayout(0, actionsMap.size()));
        actionsMap.values().forEach(e -> e.getComponents().forEach(jPanel::add));
        gbc.insets = new Insets(20,0,0,0);
        add(jPanel, gbc);
    }

    public Map<String, FormEntry<?, ?>> getEntries() {
        return entries;
    }

    public <R> R getEntryValue(FormEntry<?, R> formEntry) throws Exception {
        return ((FormEntry<?, R>) entries.get(formEntry.label)).getValue();
    }

}
