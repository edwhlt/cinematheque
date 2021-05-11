/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: FormSingleEntry
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.form;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class FormSingleEntry<T> extends FormEntry<T, T> {

    private Type type;

    public enum Type{COMBOBOX, RADIOBUTTON, TEXTAREA, TEXT}

    public FormSingleEntry(String label, T value, Function<T, String> setter, Function<String, T> getter){
        this(label, value, setter, getter, t -> true);
    }

    public FormSingleEntry(String label, T value, Function<T, String> setter, Function<String, T> getter, Function<T, Boolean> conditionOnResult){
        this(label, value, setter, getter, conditionOnResult, Type.TEXT);
    }

    public FormSingleEntry(String label, T value, Function<T, String> setter, Function<String, T> getter, Type type){
        this(label, value, setter, getter, t -> true, type);
    }

    @SafeVarargs
    public FormSingleEntry(String label, T value, Function<T, String> setter, Function<String, T> getter, Function<T, Boolean> conditionOnResult, Type type, T... options){
        super(label, value, setter, getter, conditionOnResult, options);
        this.type = type;
        initComponents();
    }

    @Override
    public void initComponents(){
        components.add(new JLabel(getLabel()+" : "){{
            setMinimumSize(new Dimension(Integer.MAX_VALUE, 30));
        }});
        if(options.length == 0){
            JTextComponent jTextComponent;
            if(type.equals(Type.TEXTAREA)) {
                JTextArea jTextArea = new JTextArea(7, 20);
                jTextArea.setLineWrap(true);
                jTextComponent = jTextArea;
                components.add(new JScrollPane(jTextComponent, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
            }
            else {
                jTextComponent = new JTextField();
                components.add(jTextComponent);
            }
            if(value != null) jTextComponent.setText(setter.apply(value));
            updateValue = r -> jTextComponent.setText(setter.apply(r));
            entry = () -> getter.apply(jTextComponent.getText());
            //VERIFICATION BONNE VALEUR
            Consumer<DocumentEvent> consumer = documentEvent -> {
                if(conditionOnResult.apply(getEntry().get())) setOutlineColor(Color.decode("#98C379"));
                else setOutline("error");
            };
            jTextComponent.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent documentEvent) {
                    consumer.accept(documentEvent);
                }

                @Override
                public void removeUpdate(DocumentEvent documentEvent) {
                    consumer.accept(documentEvent);
                }

                @Override
                public void changedUpdate(DocumentEvent documentEvent) {
                    consumer.accept(documentEvent);
                }
            });
        }else{
            if(type.equals(Type.RADIOBUTTON)){
                ButtonGroup btn_group = new ButtonGroup();
                Map<T, JRadioButton> radioButtonTemp = new HashMap<>();
                for(T type : options){
                    JRadioButton jRadioButton1 = new JRadioButton(setter.apply(type));
                    btn_group.add(jRadioButton1);
                    radioButtonTemp.put(type, jRadioButton1);
                    if(type == value) jRadioButton1.setSelected(true);
                    jRadioButton1.setMargin(new Insets(0, 20, 0, 0));
                    components.add(jRadioButton1);
                }
                updateValue = (r) -> radioButtonTemp.get(r).setSelected(true);
                //return
                entry = () -> {
                    for(Enumeration<AbstractButton> abs = btn_group.getElements(); abs.hasMoreElements();){
                        AbstractButton abstractButton = abs.nextElement();
                        if(abstractButton.isSelected()) return getter.apply(abstractButton.getText());
                    }
                    return null;
                };
            }else if(type.equals(Type.COMBOBOX)){
                JComboBox<T> jComboBox = new JComboBox<>(options);
                jComboBox.setRenderer(new DefaultListCellRenderer(){
                    @Override
                    public Component getListCellRendererComponent(JList<?> jList, Object requestListForm, int i, boolean b, boolean b1) {
                        return super.getListCellRendererComponent( jList, setter.apply(((T) requestListForm)), i, b, b1 );
                    }
                });
                components.add(jComboBox);
                updateValue = jComboBox::setSelectedItem;
                entry = () -> jComboBox.getItemAt(jComboBox.getSelectedIndex());
            }
        }
    }

}
