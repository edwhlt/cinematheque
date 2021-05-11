/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: Table
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.utils.table;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Table<T> extends JPanel {

    private final List<Column> columnList = new LinkedList<>();

    private final Map<UUID, Row<T>> rows = new LinkedHashMap<>();
    public final GridBagConstraints gbc;

    public JPanel contentPanel = new JPanel();
    private boolean isGenerated = false;
    private final int rowSpace;
    private final int rowSize;

    public Table(int rowSpace, int rowSize, Column... column){
        this.rowSpace = rowSpace;
        this.rowSize = rowSize;
        addColumns(column);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel search = new JPanel();
        search.setLayout(new BoxLayout(search, BoxLayout.X_AXIS));
        JComboBox<ColumnObject<T, ?>> jComboBox = new JComboBox<ColumnObject<T, ?>>(columnList.stream().filter(ColumnObject.class::isInstance).toArray(ColumnObject[]::new));
        jComboBox.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> jList, Object requestListForm, int i, boolean b, boolean b1) {
                return super.getListCellRendererComponent( jList, ((ColumnObject<?, ?>) requestListForm).getName(), i, b, b1 );
            }
        });
        JTextField jTextField = new JTextField();
        jTextField.setSize(new Dimension(100, 30));
        jTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filter(jTextField.getText(), jComboBox.getItemAt(jComboBox.getSelectedIndex()));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filter(jTextField.getText(), jComboBox.getItemAt(jComboBox.getSelectedIndex()));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        search.add(jComboBox);
        search.add(jTextField);

        add(search, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.setLayout(new GridBagLayout());
        scrollPane.getViewport().add(new JPanel(new FlowLayout()){{
            add(contentPanel);
        }});
        add(scrollPane, BorderLayout.CENTER);
        this.gbc = new GridBagConstraints();
    }

    private void addColumns(Column... column) {
        if(!isGenerated) columnList.addAll(Arrays.asList(column));
    }

    public Table<T> generate(){
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.insets = new Insets(10, rowSpace,10, rowSpace);
        columnList.stream().filter(ColumnObject.class::isInstance).map(ColumnObject.class::cast).forEach(column -> {
            JPanel jPanel = new JPanel();
            JLabel jLabel = new JLabel(column.getName());
            jLabel.setFont(new Font("", Font.BOLD, 14));
            jPanel.add(jLabel);
            SortButton sortButton = new SortButton(new FlatSVGIcon("images/sorted_dark.svg"), new FlatSVGIcon("images/sorted_up_dark.svg"));
            sortButton.setBorderPainted(false);
            sortButton.setFocusable(false);
            sortButton.setBackground(null);
            sortButton.setToolTipText("Trier");
            sortButton.addActionListener(evt -> sortRowBy(sortButton, column));
            jPanel.add(sortButton);
            contentPanel.add(jPanel, gbc);
            gbc.gridx++;
        });
        this.isGenerated = true;
        return this;
    }

    public void addRow(UUID uuid, T element) {
        Row<T> row = new Row<>(this, element, columnList);
        row.add(rows.size()+1);
        rows.put(uuid, row);
        if(lastFilter != null) lastFilter.run();
        if(lastSort != null) lastSort.run();
    }

    public int getRowSpace() {
        return rowSpace;
    }

    public int getRowSize() {
        return rowSize;
    }

    public Map<UUID, Row<T>> getRows() {
        return rows;
    }

    public Row<T> getRow(UUID movieUUID){
        return rows.get(movieUUID);
    }

    private Runnable lastFilter;

    public void filter(String value, ColumnObject<T, ?> columnObject){
        rows.values().forEach(r -> r.setVisible(columnObject.getValueString(r.getElement()).toLowerCase().contains(value.toLowerCase())));
        lastFilter = () -> filter(value, columnObject);
    }

    private Runnable lastSort;

    public void sortRowBy(SortButton sortButton, ColumnObject<T, ?> columnObject){
        Map<UUID, Row<T>> map = rows.entrySet().stream().sorted((e1, e2) -> {
            T t1 = e1.getValue().getElement();
            T t2 = e2.getValue().getElement();
            if(sortButton.getSortMode().equals(SortButton.SortMode.ASCENDANT)){
                return Comparator.comparing(columnObject::getValue).compare(t1, t2);
            }else if(sortButton.getSortMode().equals(SortButton.SortMode.DESCENDANT)){
                return Comparator.comparing(columnObject::getValue).compare(t2, t1);
            }
            return 0;
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
        sortButton.reverseSortMode();

        Function<String, String> funt = (string) -> "abcd";
        BiFunction<String, String, String> funt2 = (str1, st2) -> "abcd";
        Supplier<String> supplier = () -> "abcd";

        rows.values().forEach(Row::remove);
        rows.clear();
        map.forEach((k, v) -> {
            v.add(rows.size()+1);
            rows.put(k, v);
        });
        lastSort = () -> sortRowBy(sortButton, columnObject);
    }
}
