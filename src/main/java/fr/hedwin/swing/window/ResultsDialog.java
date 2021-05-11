/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: ResultsDialog
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.window;

import fr.hedwin.swing.panel.result.ResultPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ResultsDialog extends JDialog {

    public <T> ResultsDialog(Window parent, String title, boolean modal, ResultPanel<T> futureResult) {
        super(parent, title, modal ? Dialog.DEFAULT_MODALITY_TYPE : ModalityType.MODELESS);
        initComponents(futureResult);
    }

    private <T> void initComponents(ResultPanel<T> result) {
        //jPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                result.onClose();
            }
        });
        result.getLoadDataBar().close();
        add(result);
        pack();
        setLocationRelativeTo(null);
    }

}
