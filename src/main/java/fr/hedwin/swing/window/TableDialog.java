/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: TableDialog
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.window;

import fr.hedwin.swing.panel.utils.table.Table;

import javax.swing.*;
import java.awt.*;

public class TableDialog extends JDialog {

    public TableDialog(Window parent, String title, boolean modal) {
        super(parent, title, modal ? Dialog.DEFAULT_MODALITY_TYPE : ModalityType.MODELESS);
    }

    public <T> void initComponents(Table<T> table){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(table);
        setPreferredSize(new Dimension(700, 500));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
