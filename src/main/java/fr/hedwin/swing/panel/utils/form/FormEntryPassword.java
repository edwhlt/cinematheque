/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: FormEntryPassword
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.form;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.function.Consumer;

public class FormEntryPassword extends FormSingleEntry<String> {

    private FormEntryPassword confirmed;

    public FormEntryPassword(String label, String value) {
        super(label, value, s -> s, s -> s);
    }

    public FormEntryPassword(String label, String value, FormEntryPassword confirmed) {
        super(label, value, s -> s, s -> s);
        setConfirmed(confirmed);
    }

    public void setConfirmed(FormEntryPassword confirmed) {
        this.confirmed = confirmed;
        setConditionOnResult(s -> s.equals(confirmed.getEntry().get()));
    }

    @Override
    public void initComponents() {
        components.add(new JLabel(getLabel()+" : "));
        JPasswordField jPasswordField = new JPasswordField();
        if(value != null) jPasswordField.setText(value);
        components.add(jPasswordField);
        //return
        updateValue = r -> jPasswordField.setText(setter.apply(r));
        entry = () -> getter.apply(new String(jPasswordField.getPassword()));
        //VERIFICATION BONNE VALEUR
        Consumer<DocumentEvent> consumer = documentEvent -> {
            if(conditionOnResult.apply(getEntry().get())) setOutlineColor(Color.decode("#98C379"));
            else setOutline("error");
        };
        jPasswordField.getDocument().addDocumentListener(new DocumentListener() {
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
    }

}
