/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: FormActionEntry
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.form;

import fr.hedwin.utils.fonctional.Callable;

import javax.swing.*;
import java.util.function.Consumer;

public class FormActionEntry extends FormEntry<Void, Callable> {

    private Consumer<Throwable> error;
    private JButton applyButton;

    public FormActionEntry(String label) {
        this(label, () -> {}, throwable -> {});
    }

    public void setValue(Callable callable, Consumer<Throwable> error) {
        super.setValue(callable);
        this.error = error;
    }

    public FormActionEntry(String label, Callable callable, Consumer<Throwable> error) {
        super(label, null, null, s -> callable, r -> true);

        this.error = error;
        initComponents();
        updateValue.accept(callable);
    }

    public Consumer<Throwable> getError() {
        return error;
    }

    public JButton getApplyButton() {
        return applyButton;
    }

    public void setApplyButtonText(String text) {
        applyButton.setText(text);

    }

    @Override
    void initComponents() {
        JButton applyButton = new JButton(getLabel());
        this.applyButton = applyButton;
        components.add(applyButton);
        updateValue = r -> {
            applyButton.addActionListener(evt -> {
                try {
                    r.run();
                } catch (Exception e) {
                    getError().accept(e);
                }
            });
            entry = () -> r;
        };
    }
}
