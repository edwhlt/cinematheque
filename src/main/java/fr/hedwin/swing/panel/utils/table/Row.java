/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: Row
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.table;

import fr.hedwin.utils.StringTraitement;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Row<T> {

    private Table<T> table;
    private final T element;
    private final Map<Column, JComponent> componentMap = new LinkedHashMap<>();
    private final GridBagConstraints gbc;

    public Row(Table<T> table, T element, List<Column> columnList){
        this.table = table;
        this.gbc = table.gbc;
        this.element = element;
        columnList.forEach(column -> {
            if(column instanceof ColumnObject) {
                ColumnObject<T, ?> columnString = (ColumnObject<T, ?>) column;
                componentMap.put(column, new JLabel(getCellText(columnString.getValueString(element))));
            }else if(column instanceof ColumnAction){
                ColumnAction<T> columnAction = (ColumnAction<T>) column;
                JButton btn = new JButton(columnAction.getName(), columnAction.getIcon());
                btn.setBorderPainted(false);
                btn.setFocusable(false);
                btn.setBackground(null);
                btn.setToolTipText(columnAction.getTooltip());
                btn.addActionListener(evt -> columnAction.execute(this, element));
                componentMap.put(column, btn);
            }
        });
    }

    public T getElement() {
        return element;
    }

    public void add(int y){
        gbc.gridy = y;
        gbc.gridx = 0;
        componentMap.values().forEach(jComponent -> {
            if(jComponent instanceof JButton) gbc.insets = new Insets(10,10,10,10);
            else gbc.insets = new Insets(10, table.getRowSpace(), 10, table.getRowSpace());
            table.contentPanel.add(jComponent, gbc);
            gbc.gridx++;
        });
        refreshRow();
    }

    @SuppressWarnings("unchecked")
    public void update(){
        componentMap.entrySet().stream().filter(e -> e.getValue() instanceof JLabel)
                .forEach(e -> ((JLabel) e.getValue()).setText(getCellText(((ColumnObject<T, ?>) e.getKey()).getValueString(element))));
        refreshRow();
    }

    public void remove(){
        componentMap.values().forEach(table.contentPanel::remove);
        refreshRow();
    }

    public void setVisible(boolean visible){
        componentMap.values().forEach(e -> e.setVisible(visible));
    }

    private void refreshRow(){
        table.contentPanel.repaint();
        table.contentPanel.revalidate();
    }

    private String getCellText(String value){
        return "<html><div style=\"text-align: center;\">"+ StringTraitement.parseHTML(value, table.getRowSize())+"</div></html>";
    }

    public Map<Column, JComponent> getComponentMap() {
        return componentMap;
    }

}
