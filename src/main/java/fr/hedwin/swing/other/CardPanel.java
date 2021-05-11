/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: CardPanel
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.other;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import fr.hedwin.utils.StringTraitement;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CardPanel<T> extends JPanel {

    private final GridBagConstraints gbc;
    private final T t;
    private Map<JLabel, Runnable> entries = new HashMap<>();
    private JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);
    private JPanel centerPanel = new JPanel();

    public CardPanel(T t){
        this.t = t;
        this.gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        initComponents();
    }

    private void initComponents(){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        toolBar.setFloatable(false);
        add(centerPanel, BorderLayout.CENTER);
        add(toolBar, BorderLayout.WEST);
    }

    private String getTextEntry(String label, String value, boolean ln){
        return "<html><b>"+label+"</b>"+(ln ? "<br>" : " : ")+ StringTraitement.parseHTML(value, 100) +"</html>";
    }

    private JLabel addElementEntry(String label, String value, boolean ln){
        JLabel date = new JLabel(getTextEntry(label, value, ln));
        date.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        gbc.gridy = entries.size();
        centerPanel.add(date, gbc);
        return date;
    }

    public CardPanel<T> addElementEntryln(String label, Function<T, Object> value){
        JLabel jLabel = addElementEntry(label, value == null ? "Indéfinie" : value.apply(t).toString().replace("\n", "<br>"), true);
        if(value != null) entries.put(jLabel, () -> {
            jLabel.setText(getTextEntry(label, value.apply(t).toString().replace("\n", "<br>"), true));
        });
        return this;
    }

    public CardPanel<T> addElementEntry(String label, Function<T, Object> value){
        JLabel jLabel = addElementEntry(label, value == null ? "Indéfinie" : value.apply(t).toString().replace("\n", "<br>"), false);
        if(value != null) entries.put(jLabel, () -> {
            jLabel.setText(getTextEntry(label, value.apply(t).toString().replace("\n", "<br>"), false));
        });
        return this;
    }

    public CardPanel<T> addButton(String label, String tooltip, Runnable runnable){
        JButton jButton = new JButton(label);
        jButton.setToolTipText(tooltip);
        jButton.addActionListener(evt -> runnable.run());
        toolBar.add(jButton);
        return this;
    }

    public CardPanel<T> addButton(FlatSVGIcon icon, String tooltip, Runnable runnable){
        JButton jButton = new JButton(icon);
        jButton.setToolTipText(tooltip);
        jButton.addActionListener(evt -> runnable.run());
        toolBar.add(jButton);
        return this;
    }

    public void update() {
        for (Runnable value : entries.values()) value.run();
    }

}
