/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: ProgressFrame
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.window;

import javax.swing.*;
import java.awt.*;

public class ProgressFrame extends JDialog {

    private final JProgressBar jProgressBar;

    public ProgressFrame(Frame owner, String title){
        this(owner, title, -1);
    }

    public ProgressFrame(Frame owner, String title, int max){
        super(owner, false);
        setTitle(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JProgressBar jProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
        this.jProgressBar = jProgressBar;
        if(max < 0) jProgressBar.setIndeterminate(true);
        else jProgressBar.setMaximum(max);
        jProgressBar.setStringPainted(true);
        jProgressBar.setPreferredSize(new Dimension(300, 10));
        add(jProgressBar);
        pack();
        setLocationRelativeTo(null);
    }

    public JProgressBar getjProgressBar() {
        return jProgressBar;
    }

    public void setValue(int i){
        jProgressBar.setValue(i);
    }

    public void addValue(int i){
        jProgressBar.setValue(jProgressBar.getValue()+i);
    }

}
