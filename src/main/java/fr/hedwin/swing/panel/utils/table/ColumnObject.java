/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: ColumnObject
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.table;

import java.util.function.Function;

public class ColumnObject<T, R extends Comparable<R>> implements Column {

    private String name;
    private Function<T, R> value;

    public ColumnObject(String name, Function<T, R> value){
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getValueString(T t) {
        Object o = value.apply(t);
        return o != null ? o.toString() : "";
    }

    public R getValue(T t) {
        return value.apply(t);
    }

}
