/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: FormEntry
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.form;

import fr.hedwin.exceptions.EntryFormValueException;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class FormEntry<T, R> {

    protected List<Component> components = new LinkedList<>();
    protected String label;
    protected T value;
    protected Function<T, String> setter;
    protected Function<String, R> getter;
    protected Consumer<R> updateValue;
    protected Function<R, Boolean> conditionOnResult;
    protected T[] options;
    protected Supplier<R> entry;

    @SafeVarargs
    public FormEntry(String label, T value, Function<T, String> setter, Function<String, R> getter, Function<R, Boolean> conditionOnResult, T... options){
        this.label = label;
        this.value = value;
        this.setter = t -> setter.apply(t) == null ? "" : setter.apply(t);
        this.getter = getter;
        this.conditionOnResult = conditionOnResult;
        this.options = options;
    }

    public R getValue() throws EntryFormValueException {
        R r = entry.get();
        if(conditionOnResult.apply(r)){
            setOutline(null);
            return r;
        }else{
            setOutline("error");
            throw new EntryFormValueException("Le champ '"+getLabel()+"' est mal renseigné !");
        }
    }

    @Deprecated
    public Supplier<R> getEntry() {
        return entry;
    }

    public void setValue(R r){
        updateValue.accept(r);
    }

    public void setOutline(String value){
        components.stream().filter(JTextField.class::isInstance).map(JTextField.class::cast).forEach(c -> c.putClientProperty("JComponent.outline", value));
    }

    public void setOutlineColor(Color value){
        components.stream().filter(JTextField.class::isInstance).map(JTextField.class::cast).forEach(c -> c.putClientProperty("JComponent.outline", value));
    }

    protected void setConditionOnResult(Function<R, Boolean> conditionOnResult) {
        this.conditionOnResult = conditionOnResult;
    }

    public List<Component> getComponents() {
        return components;
    }

    public String getLabel(){
        return (label.charAt(0) + label.substring(1).toLowerCase()).replace("_", " ");
    }

    abstract void initComponents();

}
