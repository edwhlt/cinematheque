/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: ColumnDate
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.table;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

public class ColumnDate<T> extends ColumnObject<T, Date>{

    private SimpleDateFormat format;
    private Function<T, Date> value;

    public ColumnDate(String name, SimpleDateFormat format, Function<T, Date> value){
        super(name, value);
        this.format = format;
        this.value = value;
    }

    @Override
    public String getValueString(T t) {
        Date date = value.apply(t);
        return date != null ? format.format(date) : "";
    }

    @Override
    public Date getValue(T t) {
        Date date = value.apply(t);
        return date != null ? date : new Date(0);
    }
}
