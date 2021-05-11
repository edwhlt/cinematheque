/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: ResultPanel
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.result;

import fr.hedwin.swing.other.LoadDataBar;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public abstract class ResultPanel<T> extends JPanel {

    protected final T result;
    protected final LoadDataBar loadDataBar;
    protected final float fraction;

    protected JPanel btm_panel = new JPanel();
    protected JPanel center_panel = new JPanel();
    protected JToolBar top_panel = new JToolBar(JToolBar.HORIZONTAL);

    public ResultPanel(T result, LoadDataBar loadDataBar) throws Exception {
        this(1, result, loadDataBar);
    }

    public ResultPanel(float fraction, T result, LoadDataBar loadDataBar) throws Exception {
        this.fraction = fraction;
        this.result = result;
        this.loadDataBar = loadDataBar;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(700, 500));
        center_panel.setLayout(new BorderLayout());
        btm_panel.setLayout(new FlowLayout());
        top_panel.setLayout(new FlowLayout());
        initComponents();
        if(top_panel.getComponents().length > 0) add(top_panel, BorderLayout.NORTH);
        if(btm_panel.getComponents().length > 0) add(btm_panel, BorderLayout.SOUTH);
    }

    protected void addElementTop(Component component){
        top_panel.add(component);
        if(!Arrays.asList(getComponents()).contains(top_panel)) {
            add(top_panel, BorderLayout.NORTH);
            repaint();
            revalidate();
        }
    }

    protected void addElementBottom(Component component){
        if(component == null) return;
        btm_panel.add(component);
        if(!Arrays.asList(super.getComponents()).contains(btm_panel)) {
            add(btm_panel, BorderLayout.SOUTH);
            repaint();
            revalidate();
        }
    }

    protected abstract void initComponents() throws Exception;

    public abstract void onClose();

    public LoadDataBar getLoadDataBar() {
        return loadDataBar;
    }

}
