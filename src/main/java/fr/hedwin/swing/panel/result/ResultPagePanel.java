/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: ResultPagePanel
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.result;

import fr.hedwin.db.object.ResultsPage;
import fr.hedwin.swing.other.LoadDataBar;
import fr.hedwin.utils.StringTraitement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static fr.hedwin.utils.Utils.getPanelElement;
import static fr.hedwin.utils.Utils.getTitleElement;

@SuppressWarnings("unchecked")
public class ResultPagePanel<T> extends ResultPanel<ResultsPage<T>> {

    private static final Logger logger = LoggerFactory.getLogger(ResultPagePanel.class);

    private JTabbedPane tabbedPane;
    private SeveralResultPanel<T> severalResultPanel;

    public ResultPagePanel(float fraction, SeveralResultPanel<T> severalResultPanel, ResultsPage<T> result, LoadDataBar loadDataBar) throws Exception {
        super(fraction, result, loadDataBar);
        this.severalResultPanel = severalResultPanel;
    }

    @Override
    public void initComponents(){
        tabbedPane = new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.putClientProperty("JTabbedPane.tabAlignment", "trailing");

        List<T> resultsPage = result.getResults();
        float lastFraction = loadDataBar.getFraction();
        resultsPage.forEach(m -> {
            if(m == null) return;
            String title = "<html><div style=\"text-align: right;\">"+ StringTraitement.parseHTML(getTitleElement(m), 30)+"</div></html>";
            try {
                tabbedPane.addTab(title, getPanelElement(lastFraction + fraction*(float) (resultsPage.indexOf(m)+1) / resultsPage.size(), m, loadDataBar));
            } catch (Exception e) {
                logger.error("Impossible de chager un panel depuis '"+title+"'", e);
            }
        });

        tabbedPane.addChangeListener(e -> updateSucessEnabled());

        center_panel.add(tabbedPane, BorderLayout.CENTER);
        add(center_panel, BorderLayout.CENTER);
    }

    @Override
    public void onClose() {
        Arrays.stream(tabbedPane.getComponents()).filter(ResultPanel.class::isInstance).map(ResultPanel.class::cast).forEach(ResultPanel::onClose);
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public T getSelectedElement(){
        ResultPanel<T> resultPanel = (ResultPanel<T>) tabbedPane.getSelectedComponent();
        return resultPanel == null ? null : resultPanel.result;
    }

    public void updateSucessEnabled(){
        severalResultPanel.updateSucessEnabled();
    }

}
