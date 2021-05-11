/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: FormListEntry
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.form;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class FormListEntry<T> extends FormEntry<T, List<T>> {

    private JList<T> jList;

    @SafeVarargs
    public FormListEntry(String label, T value, Function<T, String> setter, T... options) {
        this(label, value, setter, (r) -> true, options);
    }

    @SafeVarargs
    public FormListEntry(String label, T value, Function<T, String> setter, Function<List<T>, Boolean> conditionOnResult, T... options) {
        super(label, value, setter, null, conditionOnResult, options);
        initComponents();
    }

    @Override
    void initComponents() {
        components.add(new JLabel(getLabel()+" : ", JLabel.LEFT){{
            setMinimumSize(new Dimension(Integer.MAX_VALUE, 30));
        }});
        LinkedList<T> linkedList = new LinkedList<>(Arrays.asList(options));
        JList<T> jList = new JList<>(options);
        this.jList = jList;
        jList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> jList, Object requestListForm, int i, boolean b, boolean b1) {
                return super.getListCellRendererComponent( jList, setter.apply((T) requestListForm), i, b, b1 );
            }
        });
        JScrollPane sp = new JScrollPane();
        sp.setViewportView(jList);
        jList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jList.setMinimumSize(new Dimension(100, 100));
        components.add(sp);
        updateValue = r -> {
            int[] integers = r.stream().mapToInt(linkedList::indexOf).toArray();
            jList.setSelectedIndices(integers);
        };
        entry = jList::getSelectedValuesList;
    }

    public void setValue(T... t) {
        super.setValue(Arrays.asList(t));
    }
}
