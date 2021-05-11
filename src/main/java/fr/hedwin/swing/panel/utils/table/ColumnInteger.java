/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: ColumnInteger
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.table;

import java.util.function.Function;

public class ColumnInteger<T> extends ColumnObject<T, Integer>{

    private Function<T, Integer> value;
    private Function<Integer, Boolean> conditionToNullString;


    public ColumnInteger(String name, Function<T, Integer> value){
        this(name, value, i -> true);
    }

    public ColumnInteger(String name, Function<T, Integer> value, Function<Integer, Boolean> conditionToNullString){
        super(name, value);
        this.value = value;
        this.conditionToNullString = conditionToNullString;
    }

    @Override
    public String getValueString(T t) {
        Integer integer = value.apply(t);
        return conditionToNullString.apply(integer) ? integer.toString() : "";
    }
}
