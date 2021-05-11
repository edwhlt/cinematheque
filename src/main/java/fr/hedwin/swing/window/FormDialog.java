/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: FormDialog
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.window;
import fr.hedwin.swing.panel.utils.form.Form;

import javax.swing.*;
import java.awt.*;

public class FormDialog extends JDialog {

    public FormDialog(Window parent, String title, boolean modal){
        super(parent, title, modal ? Dialog.DEFAULT_MODALITY_TYPE : ModalityType.MODELESS);
    }

    public void initComponents(Form form){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(form);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }


}
