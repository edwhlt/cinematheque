/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: FormSingleNumberEntry
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.form;

import java.util.function.Function;

public class FormSingleNumberEntry extends FormSingleEntry<Integer> {

    public FormSingleNumberEntry(String label, Integer value) {
        this(label, value, r -> true);
    }

    public FormSingleNumberEntry(String label, Integer value, Function<Integer, Boolean> conditionOnResult) {
        this(label, value, conditionOnResult, Type.TEXT);
    }

    public FormSingleNumberEntry(String label, Integer value, Function<Integer, Boolean> conditionOnResult, Type type, Integer... options) {
        super(label, value, String::valueOf, s -> {
            try {
                return Integer.parseInt(s);
            }catch (NumberFormatException ex){
                return null;
            }
        }, i -> i != null && conditionOnResult.apply(i), type, options);
    }

}
