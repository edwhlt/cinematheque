/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: MultipleResultPanel
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.result;

import fr.hedwin.db.model.IdElement;
import fr.hedwin.db.model.TmdbElement;
import fr.hedwin.swing.IHM;
import fr.hedwin.swing.other.LoadDataBar;
import fr.hedwin.swing.panel.result.properties.ResultPanelReturn;
import fr.hedwin.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("unchecked")
public class MultipleResultPanel extends ResultPanel<Map<String, ? extends TmdbElement>> {

    private final ResultPanelReturn<TmdbElement> resultPanelReturn;
    private JTabbedPane tabbedPane;

    public MultipleResultPanel(Map<String, ? extends TmdbElement> result, LoadDataBar loadDataBar, ResultPanelReturn<TmdbElement> resultPanelReturn) throws Exception {
        super(result, loadDataBar);
        this.resultPanelReturn = resultPanelReturn;

        if(resultPanelReturn != null){
            addElementBottom(resultPanelReturn.getSuccessBtn(this::getSelectedElement));
            addElementBottom(resultPanelReturn.getCancelBtn());
        }
        updateSucessEnabled();
    }

    @Override
    protected void initComponents() throws Exception {
        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        //tabbedPane.putClientProperty("JTabbedPane.tabAlignment", "trailing");

        boolean success = false;
        for(Map.Entry<String, ?> entry : result.entrySet()){
            if(entry.getValue() == null) return;
            try{
                tabbedPane.addTab(entry.getKey(), Utils.getPanelResult(this, (float) 1/result.size(), entry.getValue(), loadDataBar, null));
                success = true;
            }catch (Exception ignored){}
        }
        if(!success) throw new Exception("Aucun résultat");

        tabbedPane.addChangeListener(e -> updateSucessEnabled());
        center_panel.add(tabbedPane, BorderLayout.CENTER);
        add(center_panel, BorderLayout.CENTER);
    }

    @Override
    public void onClose() {
        Arrays.stream(tabbedPane.getComponents()).filter(ResultPanel.class::isInstance).map(ResultPanel.class::cast).forEach(ResultPanel::onClose);
    }

    public ResultPanelReturn<TmdbElement> getResultPanelReturn() {
        return resultPanelReturn;
    }

    public void updateSucessEnabled(){
        TmdbElement t = getSelectedElement();
        if(t == null || getResultPanelReturn() == null) return;
        if(getResultPanelReturn().isVerifiedForReturn(t)) {
            if(t instanceof IdElement) getResultPanelReturn().setEnabledSucess(!IHM.isAlreadyAdded(((IdElement) t).getId()));
            else getResultPanelReturn().setEnabledSucess(true);
        }
        else getResultPanelReturn().setEnabledSucess(false);
    }

    public TmdbElement getSelectedElement(){
        Component component = tabbedPane.getSelectedComponent();
        if(component instanceof SeveralResultPanel){
            return ((SeveralResultPanel<TmdbElement>) component).getSelectedElement();
        }else if(component instanceof ResultElementPanel) return ((ResultElementPanel<TmdbElement>) component).result;
        return null;
    }

}
