/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: MenuBar
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatUIDefaultsInspector;
import fr.hedwin.Main;
import fr.hedwin.swing.panel.utils.table.ColumnAction;
import fr.hedwin.swing.panel.utils.table.Row;
import fr.hedwin.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MenuBar extends JMenuBar {

    public IHM ihm;
    public MenuBar(IHM ihm) {
        this.ihm = ihm;
        createMenuBar(ihm);
    }

    /* Methode de construction de la barre de menu */
    private void createMenuBar(IHM ihm) {
        // Définition du menu déroulant "File" et de son contenu
        JMenu mnuFile = new JMenu("Fichier");
        mnuFile.setMnemonic('F');

        JMenuItem mnuNewFile = new JMenuItem("Nouveau");
        mnuNewFile.setIcon( new ImageIcon("icons/new.png"));
        mnuNewFile.setMnemonic('N');
        mnuNewFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        mnuFile.add(mnuNewFile);

        mnuFile.addSeparator();

        JMenuItem mnuOpenFile = new JMenuItem("Ouvrir...");
        mnuOpenFile.setIcon( new ImageIcon("icons/open.png"));
        mnuOpenFile.setMnemonic('O');
        mnuOpenFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        mnuFile.add(mnuOpenFile);

        JMenuItem mnuSaveFile = new JMenuItem("Enregistrer...");
        mnuSaveFile.setIcon( new ImageIcon("icons/save.png"));
        mnuSaveFile.setMnemonic('S');
        mnuSaveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        mnuFile.add(mnuSaveFile);

        JMenuItem mnuSaveFileAs = new JMenuItem("Enregister sous...");
        mnuSaveFileAs.setIcon(new ImageIcon("icons/save_as.png"));
        mnuSaveFileAs.setMnemonic('A');
        mnuFile.add(mnuSaveFileAs);

        mnuFile.addSeparator();

        JMenuItem mnuSaveDatas = new JMenuItem("Enregistrer la cinémathèque...");
        mnuSaveDatas.setIcon(new FlatSVGIcon("images/menu-saveall_dark.svg"));
        mnuSaveDatas.setMnemonic('D');
        mnuSaveDatas.addActionListener(this::saveDatas);
        mnuFile.add(mnuSaveDatas);

        mnuFile.addSeparator();

        JMenuItem mnuExit = new JMenuItem("Fermer");
        mnuExit.setIcon( new ImageIcon("icons/exit.png"));
        mnuExit.setMnemonic('x');
        mnuExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
        mnuExit.addActionListener(e -> ihm.closeIHM());
        mnuFile.add(mnuExit);

        add(mnuFile);

        // Définition du menu déroulant "Edit" et de son contenu
        JMenu mnuEdit = new JMenu("Editer");
        mnuEdit.setMnemonic('E');

        JMenuItem mnuUndo = new JMenuItem("Retour");
        mnuUndo.setIcon( new ImageIcon("icons/undo.png"));
        mnuUndo.setMnemonic('U');
        mnuUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        mnuEdit.add(mnuUndo);

        JMenuItem mnuRedo = new JMenuItem("Avancer");
        mnuRedo.setIcon( new ImageIcon("icons/redo.png"));
        mnuRedo.setMnemonic('R');
        mnuRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, KeyEvent.CTRL_DOWN_MASK));
        mnuEdit.add(mnuRedo);

        mnuEdit.addSeparator();

        JMenuItem mnuCopy = new JMenuItem("Copier");
        mnuCopy.setIcon( new ImageIcon("icons/copy.png"));
        mnuCopy.setMnemonic('C');
        mnuCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        mnuEdit.add(mnuCopy);

        JMenuItem mnuCut = new JMenuItem("Couper");
        mnuCut.setIcon( new ImageIcon("icons/cut.png"));
        mnuCut.setMnemonic('t');
        mnuCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        mnuEdit.add(mnuCut);

        JMenuItem mnuPaste = new JMenuItem("Coller");
        mnuPaste.setIcon( new ImageIcon("icons/paste.png"));
        mnuPaste.setMnemonic('P');
        mnuPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        mnuEdit.add(mnuPaste);

        add(mnuEdit);

        // Définition du menu déroulant "Edit" et de son contenu
        JMenu mnuOptions = new JMenu("Options");
        mnuOptions.setMnemonic('O');

        JMenuItem mnuShowUI = new JMenuItem("Ouvrir UI Default Inspector");
        mnuShowUI.addActionListener(e -> showUIDefaultsInspector());
        mnuOptions.add(mnuShowUI);

        JCheckBoxMenuItem underlineMenuSelectionMenuItem = new JCheckBoxMenuItem("Souligner les élements du menu");
        this.underlineMenuSelectionMenuItem = underlineMenuSelectionMenuItem;
        underlineMenuSelectionMenuItem.addActionListener(e -> updateUnderlineMenuSelection());
        mnuOptions.add(underlineMenuSelectionMenuItem);

        JMenu mnuTheme = new JMenu("Thèmes");
        try {
            List<String> fileNames = getResourceFiles(Pattern.compile(".*\\.theme.json"));
            ButtonGroup themesGroup = new ButtonGroup();
            fileNames.forEach(fileName -> {
                try {
                    ObjectNode node = new ObjectMapper().readValue(Main.class.getClassLoader().getResourceAsStream(fileName), ObjectNode.class);
                    JRadioButtonMenuItem themeCheck = new JRadioButtonMenuItem(node.get("name").asText());
                    themeCheck.addActionListener(evt -> updateTheme(fileName));
                    themesGroup.add(themeCheck);
                    mnuTheme.add(themeCheck);
                } catch (IOException e) {
                    e.getStackTrace();
                }
            });
        }catch (IOException e) {
            logger.error("Impossible de chager les thèmes", e);
        }
        mnuOptions.add(mnuTheme);

        add(mnuOptions);


        // Définition du menu déroulant "Help" et de son contenu
        JMenu mnuHelp = new JMenu("Aide");
        mnuHelp.setMnemonic('H');

        add(mnuHelp);
    }

    private void showUIDefaultsInspector() {
        FlatUIDefaultsInspector.show();
    }
    private static Logger logger = LoggerFactory.getLogger(MenuBar.class);

    JCheckBoxMenuItem underlineMenuSelectionMenuItem;
    public void updateUnderlineMenuSelection(){
        UIManager.put("MenuItem.selectionType", underlineMenuSelectionMenuItem.isSelected() ? "underline" : null);
    }

    public void saveDatas(ActionEvent event){
        long d = System.currentTimeMillis();
        try {
            Utils.saveJSON("datas.json", Main.movies);
        } catch (IOException ioException) {
            JOptionPane.showMessageDialog(ihm, "Impossible de sauvegarder les données : "+ioException.getMessage(), "Erreur de sauvegarde", JOptionPane.ERROR_MESSAGE);
            logger.error("Erreur :", ioException);
        }
        long f = System.currentTimeMillis();
        JOptionPane.showMessageDialog(ihm, "Données de la cinémathèque sauvegardées (Temps : "+(f-d)+"ms)", "Sauvegarde de la cinémathèque", JOptionPane.INFORMATION_MESSAGE);
        logger.info("Fichiers sauvegardés");
    }

    public void updateTheme(String path){
        if( path == null ) return;
        FlatAnimatedLafChange.showSnapshot();
        IntelliJTheme.install(Main.class.getClassLoader().getResourceAsStream(path));
        // update all components
        FlatLaf.updateUI();
        //update button's background on rows in table
        for (Row<?> row : IHM.INSTANCE.getCinematheque().getTable().getRows().values()) {
            row.getComponentMap().entrySet().stream().filter(e -> e.getKey() instanceof ColumnAction).forEach(e -> {
                e.getValue().setBackground(null);
            });
        }
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    private List<String> getResourceFiles(Pattern pattern) throws IOException {
        List<String> filenames = new ArrayList<>();
        filenames.add("edwin-dark.theme.json");
        filenames.add("edwin-red-dark.theme.json");
        filenames.add("solarized-light.theme.json");
        return filenames;
    }


}