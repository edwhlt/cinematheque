/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: Utils
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import fr.hedwin.db.Results;
import fr.hedwin.db.TMDB;
import fr.hedwin.db.model.MovieSortBy;
import fr.hedwin.db.model.NamedIdElement;
import fr.hedwin.db.model.TmdbElement;
import fr.hedwin.db.object.*;
import fr.hedwin.exceptions.ResultException;
import fr.hedwin.swing.IHM;
import fr.hedwin.swing.other.LoadDataBar;
import fr.hedwin.swing.other.jlist.RequestListForm;
import fr.hedwin.swing.panel.SearchPanel;
import fr.hedwin.swing.panel.result.MultipleResultPanel;
import fr.hedwin.swing.panel.result.ResultElementPanel;
import fr.hedwin.swing.panel.result.ResultPanel;
import fr.hedwin.swing.panel.result.SeveralResultPanel;
import fr.hedwin.swing.panel.result.properties.ResultPanelReturn;
import fr.hedwin.swing.panel.utils.table.Column;
import fr.hedwin.swing.panel.utils.table.ColumnAction;
import fr.hedwin.swing.panel.utils.table.ColumnObject;
import fr.hedwin.swing.panel.utils.table.Table;
import fr.hedwin.swing.window.ResultsDialog;
import fr.hedwin.swing.window.TableDialog;
import fr.hedwin.swing.window.TrailerDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static <T> void loadJSON(String filename, TypeReference<T> tTypeReference, Consumer<T> reader) throws IOException {
        File f = new File(filename);
        if(f.exists()) reader.accept(new ObjectMapper().readValue(new BufferedReader(new InputStreamReader(new FileInputStream(f))), tTypeReference));
    }

    public static <T> void saveJSON(String filename, T object) throws IOException {
        try (Writer writer = new FileWriter(filename)) {
            new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValue(writer, object);
        }
    }

    public static <T> String getTitleElement(T t) {
        if(t instanceof DbMovie)  return ((DbMovie) t).getTitle();
        if(t instanceof NamedIdElement) return ((NamedIdElement) t).getName();
        else return "Indéfinie";
    }

    @SuppressWarnings("unchecked")
    public static <E> ResultPanel<?> getPanelResult(MultipleResultPanel multipleResultPanel, float fraction, E e, LoadDataBar loadDataBar, ResultPanelReturn<TmdbElement> resultPanelReturn) throws Exception {
        if(e instanceof Results) return Utils.getSeveralResultPanel(multipleResultPanel, fraction, (Results<TmdbElement>) e, loadDataBar, resultPanelReturn);
        else return Utils.getPanelElement(fraction, e, loadDataBar);
    }

    public static <T> ResultPanel<?> getPanelElement(float fraction, T t, LoadDataBar loadDataBar) throws Exception {
        if(t instanceof DbMovie) return getMoviePanel(fraction, ((DbMovie) t), loadDataBar);
        else if(t instanceof Person) return getPersonPanel(fraction, ((Person) t), loadDataBar);
        else if(t instanceof DbSerie) return getSeriePanel(fraction, ((DbSerie) t), loadDataBar);
        else return new ResultElementPanel<>(fraction, t, loadDataBar).addElementEntry("Aucun panel associé à ce type de résultat :", t.toString());
    }

    public static ResultPanel<Map<String, ? extends TmdbElement>> getMultipleResultPanel(Map<String, ? extends TmdbElement> map, LoadDataBar loadDataBar, ResultPanelReturn<TmdbElement> properties) throws Exception {
        return new MultipleResultPanel(map, loadDataBar, properties);
    }

    public static <T> ResultPanel<Results<T>> getSeveralResultPanel(MultipleResultPanel parent, float fraction, Results<T> results, LoadDataBar loadDataBar, ResultPanelReturn<T> properties) throws Exception {
        return new SeveralResultPanel<>(parent, fraction, results, loadDataBar, properties);
    }

    public static ResultPanel<DbMovie> getMoviePanel(float fraction, DbMovie dbMovie, LoadDataBar loadDataBar) throws Exception {
        ResultElementPanel<DbMovie> resultElementPanel = new ResultElementPanel<>(fraction, dbMovie, loadDataBar).setImage(dbMovie.getPosterPath())
                .addElementEntry("Notation :", dbMovie.getVoteAverage())
                .addElementEntry("Identifiant :", dbMovie.getId())
                .addElementEntry("Titre :", dbMovie.getTitle())
                .addElementEntry("Description :", dbMovie.getOverview())
                .addElementEntry("Date de sortie :", dbMovie.getReleaseDate());
        resultElementPanel.addButton(new FlatSVGIcon("images/recording_2_dark.svg"), "Toute les vidéos", () -> {
            loadDataBar.initialize();
            dbMovie.getVideos()
                    .then(videos -> openVideos(resultElementPanel, "Vidéo de "+dbMovie.getTitle(), videos))
                    .error(e -> errorPopup(resultElementPanel, "Erreur vidéo", e));
        });
        resultElementPanel.addButton(new FlatSVGIcon("images/users_dark.svg"), "Voir les acteurs/actrices", () -> {
            loadDataBar.initialize();
            dbMovie.getCredits()
                    .then(credits -> openCasting(resultElementPanel, "Casting de "+dbMovie.getTitle(), credits))
                    .error(e -> errorPopup(resultElementPanel, "Erreur casting", e));
        });
        resultElementPanel.addButton("Genres", "Afficher les genres", () -> {
            loadDataBar.initialize();
            TMDB.getMovie(dbMovie.getId())
                    .then(movie -> openGenres(resultElementPanel, "Genres de "+movie.getTitle(), movie.getGenres()))
                    .error(e -> errorPopup(resultElementPanel, "Erreur genre", e));
        });
        return resultElementPanel;
    }

    public static ResultPanel<Person> getPersonPanel(float fraction, Person person, LoadDataBar loadDataBar) throws Exception {
        ResultElementPanel<Person> resultElementPanel = new ResultElementPanel<>(fraction, person, loadDataBar).setImage(person.getProfilePath())
                .addElementEntry("Identifiant :", person.getId())
                .addElementEntry("Nom :", person.getName())
                .addElementEntry("Sexe :", person.getGender().getString())
                .addElementEntry("Biographie :", person.getBiography())
                .addElementEntry("Date de naissance :", person.getBirthday());
        resultElementPanel.addButton(new FlatSVGIcon("images/recording_2_dark.svg"), "A joué dans les films", () -> {
            loadDataBar.initialize();
            Consumer<ResultPanel<?>> openDialog = (panel) -> new ResultsDialog(SwingUtilities.getWindowAncestor(resultElementPanel), "Film de "+person.getName(), true, panel).setVisible(true);
            TMDB.discoverMovie(null, null, (String) null, person.getId()+"")
                    .then(resultsMovie -> openDialog.accept(getPanelResult(null, 1, resultsMovie, loadDataBar, null)));
        });
        return resultElementPanel;
    }

    public static ResultPanel<DbSerie> getSeriePanel(float fraction, DbSerie dbSerie, LoadDataBar loadDataBar) throws Exception {
        ResultElementPanel<DbSerie> resultElementPanel = new ResultElementPanel<>(fraction, dbSerie, loadDataBar).setImage(dbSerie.getPosterPath())
                .addElementEntry("Titre :", dbSerie.getName())
                .addElementEntry("Description :", dbSerie.getOverview())
                .addElementEntry("Date de sortie :", dbSerie.getFirstAirDate());
        resultElementPanel.addButton(new FlatSVGIcon("images/recording_2_dark.svg"), "Voir la bande d'annonce", () -> {
            loadDataBar.initialize();
            TMDB.getTvSeriesVideos(dbSerie.getId())
                    .then(videos -> openVideos(resultElementPanel, "Vidéos de "+dbSerie.getName(), videos))
                    .error(e -> errorPopup(resultElementPanel, "Erreur vidéo", e));
        });
        return resultElementPanel;
    }

    public static void closeAllDialogFrom(JDialog window){
        if(window.getParent() instanceof JDialog) closeAllDialogFrom(((JDialog) window.getOwner()));
        window.dispose();
    }

    private static <T> void openVideos(ResultPanel<T> panel, String title, Videos videos) throws ResultException{
        if(videos == null || videos.getTrailers().isEmpty()) throw new ResultException("Aucune vidéo disponible");
        TableDialog jDialog = new TableDialog(SwingUtilities.getWindowAncestor(panel), title, true);
        jDialog.setPreferredSize(new Dimension(700, 500));

        Column[] columns = {
                new ColumnObject<>("Titre", Videos.Video::getName), new ColumnObject<>("Type", Videos.Video::getType),
                new ColumnAction<Videos.Video>(new FlatSVGIcon("images/execute_dark.svg"), "Voir la vidéo", (row, video) -> {
                    new TrailerDialog(jDialog, video.getName(), video.getKey());
                })
        };
        Table<Videos.Video> table = new Table<Videos.Video>(30, 40, columns).generate();
        videos.getVideoList().forEach(video -> table.addRow(UUID.randomUUID(), video));
        panel.getLoadDataBar().close();
        jDialog.initComponents(table);
    }

    public static <T> void openGenres(ResultPanel<T> panel, String title, List<Genre> genreList) throws ResultException {
        if(genreList.isEmpty()) throw new ResultException("Aucun genres disponible");
        TableDialog jDialog = new TableDialog(SwingUtilities.getWindowAncestor(panel), title, true);
        Column[] columns = {
                new ColumnObject<>("ID", Genre::getId), new ColumnObject<>("Name", Genre::getName),
                new ColumnAction<Genre>(new FlatSVGIcon("images/find_dark.svg"), "Rechercher les films de ce genre", (row, genre) -> {
                    closeAllDialogFrom(jDialog);
                    SearchPanel searchPanel = IHM.INSTANCE.getSearchPanel();
                    JList<RequestListForm> jList = searchPanel.getJlist();
                    jList.setSelectedIndex(11);
                    searchPanel.getGenresMovieEntry().setValue(genre);
                    searchPanel.getPersonsEntry().setValue("");
                    searchPanel.getMovieSortByEntry().setValue(MovieSortBy.getDefault());
                    searchPanel.getYearsEntry().setValue(null);
                    try {
                        jList.getSelectedValue().actionSucess().getValue().run();
                    } catch (Exception e) {
                        jList.getSelectedValue().actionSucess().getError().accept(e);
                    }
                    IHM.INSTANCE.selectedTab(1);
                })
        };
        Table<Genre> table = new Table<Genre>(30, 40, columns).generate();
        genreList.forEach(genre -> table.addRow(UUID.randomUUID(), genre));
        panel.getLoadDataBar().close();
        jDialog.initComponents(table);
    }

    public static <T> void openCasting(ResultPanel<T> panel, String title, Credits credits) throws ResultException {
        if(credits.getCast().isEmpty()) throw new ResultException("Aucun acteur disponible");
        TableDialog jDialog = new TableDialog(SwingUtilities.getWindowAncestor(panel), title, true);
        Column[] columns = {
                new ColumnObject<>("Acteur", Cast::getName), new ColumnObject<>("Rôle", Cast::getCharacter),
                new ColumnAction<Cast>(new FlatSVGIcon("images/informationDialog_dark.svg"), "Plus d'informations sur l'acteur", (row, cast) -> {
                    closeAllDialogFrom(jDialog);
                    SearchPanel searchPanel = IHM.INSTANCE.getSearchPanel();
                    JList<RequestListForm> jList = searchPanel.getJlist();
                    jList.setSelectedIndex(8);
                    searchPanel.getNameEntry().setValue(cast.getId()+"");
                    try {
                        jList.getSelectedValue().actionSucess().getValue().run();
                    } catch (Exception e) {
                        jList.getSelectedValue().actionSucess().getError().accept(e);
                    }
                    IHM.INSTANCE.selectedTab(1);
                })
        };
        Table<Cast> table = new Table<Cast>(30, 40, columns).generate();
        credits.getCast().forEach(cast -> table.addRow(UUID.randomUUID(), cast));
        panel.getLoadDataBar().close();
        jDialog.initComponents(table);
    }

    public static void errorPopup(Component parent, String message, Throwable throwable){
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(parent), throwable.getMessage(), message, JOptionPane.ERROR_MESSAGE);
    }

}
