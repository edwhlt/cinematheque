/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: ColumnAction
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.table;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.util.function.BiConsumer;

public class ColumnAction<T> implements Column{

    private final BiConsumer<Row<T>, T> execute;
    private String name;
    private Icon icon;
    private final String tooltip;

    public ColumnAction(FlatSVGIcon icon, String tooltip, BiConsumer<Row<T>, T> execute){
        //On met le nom avec un string unique car il sera stocké dans une map dans Row
        this.icon = icon.derive(16, 16);
        this.tooltip = tooltip;
        this.execute = execute;
    }

    public ColumnAction(Icon icon, String tooltip, BiConsumer<Row<T>, T> execute){
        //On met le nom avec un string unique car il sera stocké dans une map dans Row
        this.icon = icon;
        this.tooltip = tooltip;
        this.execute = execute;
    }

    public ColumnAction(String name, String tooltip, BiConsumer<Row<T>, T> execute){
        this.name = name;
        this.tooltip = tooltip;
        this.execute = execute;
    }

    public Icon getIcon() {
        return icon;
    }

    public String getTooltip() {
        return tooltip;
    }

    @Override
    public String getName() {
        return name;
    }

    public void execute(Row<T> table, T t) {
        execute.accept(table, t);
    }
}
