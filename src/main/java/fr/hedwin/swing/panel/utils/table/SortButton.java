/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: SortButton
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.table;

import javax.swing.*;

public class SortButton extends JButton {

    private Icon iconDown;
    private Icon iconUp;

    public enum SortMode{ ASCENDANT, DESCENDANT }

    private SortMode sortMode = SortMode.ASCENDANT;

    public SortButton(Icon iconDown, Icon iconUp){
        super(iconUp);
        this.iconDown = iconDown;
        this.iconUp = iconUp;
    }

    public SortMode getSortMode() {
        return sortMode;
    }

    public void reverseSortMode() {
        if(sortMode.equals(SortMode.ASCENDANT)){
            setIcon(iconDown);
            this.sortMode = SortMode.DESCENDANT;
        }else{
            setIcon(iconUp);
            this.sortMode = SortMode.ASCENDANT;
        }
    }

}
