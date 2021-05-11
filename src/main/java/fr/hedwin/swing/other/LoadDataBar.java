/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: LoadDataBar
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.other;

import javax.swing.*;
import java.awt.*;

public class LoadDataBar extends JProgressBar {

    public LoadDataBar(){
        super(JProgressBar.HORIZONTAL);
        setMaximumSize(new Dimension(300, 5));
        setMaximum(1000);
        close();
    }

    public void initialize(){
        setVisible(true);
        setFraction(0);
    }

    public void setFraction(float fraction){
        if(fraction == 0) setIndeterminate(true);
        else{
            setIndeterminate(false);
            setValue((int) (fraction*1000));
        }
    }

    public float getFraction(){
        if(!isIndeterminate()) return (float)getValue()/1000;
        return 0;
    }

    public void close(){
        setVisible(false);
    }

}
