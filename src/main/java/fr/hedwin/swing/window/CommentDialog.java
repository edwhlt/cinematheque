/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: CommentDialog
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.window;

import fr.hedwin.swing.panel.comment.CommentsPanel;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class CommentDialog extends JDialog {

    public CommentDialog(Window parent, String title, boolean modal, UUID movie) {
        super(parent, title, modal ? Dialog.DEFAULT_MODALITY_TYPE : ModalityType.MODELESS);
        initComponents(movie);
    }

    public void initComponents(UUID movie){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(new CommentsPanel(this, movie));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
