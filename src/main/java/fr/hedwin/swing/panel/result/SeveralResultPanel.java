/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: SeveralResultPanel
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.result;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import fr.hedwin.db.Results;
import fr.hedwin.db.model.IdElement;
import fr.hedwin.db.model.TmdbElement;
import fr.hedwin.db.object.ResultsPage;
import fr.hedwin.db.utils.Future;
import fr.hedwin.swing.IHM;
import fr.hedwin.swing.other.LoadDataBar;
import fr.hedwin.swing.panel.result.properties.ResultPanelReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static fr.hedwin.utils.Utils.getPanelElement;

@SuppressWarnings("unchecked")
public class SeveralResultPanel<T> extends ResultPanel<Results<T>> {

    private MultipleResultPanel multipleResultPanel;
    private final ResultPanelReturn<T> resultPanelReturn;
    private Map<Integer, ResultPagePanel<T>> pagePanel;
    private int actualPage;
    private JLabel pageLabel;

    private static Logger logger = LoggerFactory.getLogger(SeveralResultPanel.class);

    public SeveralResultPanel(MultipleResultPanel multipleResultPanel, float fraction, Results<T> result, LoadDataBar loadDataBar, ResultPanelReturn<T> resultPanelReturn) throws Exception {
        super(fraction, result, loadDataBar);
        this.multipleResultPanel = multipleResultPanel;
        this.resultPanelReturn = resultPanelReturn;

        if(result.getTotalResults() <= 0) throw new Exception("Aucun résultat !");
        if(resultPanelReturn != null){
            addElementBottom(resultPanelReturn.getSuccessBtn(this::getSelectedElement));
            addElementBottom(resultPanelReturn.getCancelBtn());
        }
        updateSucessEnabled();
    }

    @Override
    protected void initComponents() throws Exception{
        this.pagePanel = new HashMap<>();
        this.pageLabel = new JLabel();

        if(!result.getResultsPage(1).call().isEmpty()){
            if(result.getTotalResults() == 1) {
                center_panel.add(getPanelElement(fraction, (TmdbElement) result.getFirstPage().getResults().get(0), getLoadDataBar()), BorderLayout.CENTER);
                getLoadDataBar().setFraction(1);
            } else openPage(1);

            if(result.getTotalPage() > 1){
                JButton left = new JButton(new FlatSVGIcon("images/arrowLeft_dark.svg"));
                left.addActionListener(evt -> openPage(actualPage-1));

                JButton right = new JButton(new FlatSVGIcon("images/arrowRight_dark.svg"));
                right.addActionListener(evt -> openPage(actualPage+1));

                addElementBottom(left);
                addElementBottom(pageLabel);
                addElementBottom(right);
            }
        }
        add(center_panel, BorderLayout.CENTER);
    }

    @Override
    public void onClose() {
        pagePanel.values().forEach(ResultPagePanel::onClose);
        if(resultPanelReturn != null) resultPanelReturn.cancel();
    }

    public void openPage(int page){
        if(pagePanel.containsKey(page)){
            if(center_panel.getComponents().length > 0) {
                pagePanel.get(actualPage).setVisible(false);
            }
            if(Arrays.asList(center_panel.getComponents()).contains(pagePanel.get(page))) {
                pagePanel.get(page).setVisible(true);
            }else center_panel.add(pagePanel.get(page));
            center_panel.repaint();
            center_panel.revalidate();
            setNumberPageLabel(page);
            this.actualPage = page;
        }
        else {
            if(page == 1) {
                try {
                    pagePanel.put(page, new ResultPagePanel<>(fraction, this, result.getFirstPage(), getLoadDataBar()));
                    openPage(page);
                } catch (Exception e) {
                    logger.error("Impossible de générer un panel d'une page :", e);
                }
            }
            else{
                if(page <= 0 || page > result.getTotalPage()) return;
                getLoadDataBar().initialize();
                Future<ResultsPage<T>> future = result.getPage(page);
                future.then((result) -> {
                    try {
                        //On met la fraction à 1 parceque qu'on charge plus tard
                        pagePanel.put(page, new ResultPagePanel<>(1, this, result, getLoadDataBar()));
                    } catch (Exception e) {
                        logger.error("Impossible de créer un page panel", e);
                    }
                    getLoadDataBar().close();
                    //le panel a été ajouté dans la map donc on peut appellé la méthode où le if sera executé
                    openPage(page);
                });
            }
        }
    }

    public void setNumberPageLabel(int page){
        this.pageLabel.setText(page+"/"+result.getTotalPage());
    }

    public ResultPanelReturn<T> getResultPanelReturn() {
        return resultPanelReturn;
    }

    public void updateSucessEnabled(){
        T t = getSelectedElement();
        if(t == null || getResultPanelReturn() == null) {
            if(multipleResultPanel != null) multipleResultPanel.updateSucessEnabled();
        }
        else if(getResultPanelReturn().isVerifiedForReturn(t)) {
            if(t instanceof IdElement) getResultPanelReturn().setEnabledSucess(!IHM.isAlreadyAdded(((IdElement) t).getId()));
            else getResultPanelReturn().setEnabledSucess(true);
        }
        else getResultPanelReturn().setEnabledSucess(false);
    }

    public T getSelectedElement() {
        if(pagePanel.isEmpty()){
            return ((ResultElementPanel<T>) center_panel.getComponents()[0]).result;
        }else{
            return pagePanel.get(actualPage).getSelectedElement();
        }
    }
}
