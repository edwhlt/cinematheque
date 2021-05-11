/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: SearchPanel
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel;

import fr.hedwin.db.TMDB;
import fr.hedwin.db.model.IdElement;
import fr.hedwin.db.model.MovieSortBy;
import fr.hedwin.db.model.SerieSortBy;
import fr.hedwin.db.model.TmdbElement;
import fr.hedwin.db.object.DbMovie;
import fr.hedwin.db.object.DbSerie;
import fr.hedwin.db.object.Genre;
import fr.hedwin.db.utils.Future;
import fr.hedwin.objects.Movie;
import fr.hedwin.swing.IHM;
import fr.hedwin.swing.other.jlist.ListCategorie;
import fr.hedwin.swing.other.jlist.RequestListForm;
import fr.hedwin.swing.panel.result.ResultPanel;
import fr.hedwin.swing.panel.result.properties.ResultPanelReturn;
import fr.hedwin.swing.panel.utils.form.Form;
import fr.hedwin.swing.panel.utils.form.FormActionEntry;
import fr.hedwin.swing.panel.utils.form.FormListEntry;
import fr.hedwin.swing.panel.utils.form.FormSingleEntry;
import fr.hedwin.swing.window.FormDialog;
import fr.hedwin.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Date;
import java.util.UUID;

import static fr.hedwin.utils.Utils.getPanelResult;

public class SearchPanel extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(SearchPanel.class);

    private final JList<RequestListForm> jlist;
    private final FormSingleEntry<String> nameEntry;
    private final FormSingleEntry<String> personsEntry;
    private final FormSingleEntry<String> yearsEntry;
    private final FormListEntry<Genre> genresMovieEntry;
    private final FormListEntry<Genre> genresSerieEntry;
    private final FormSingleEntry<MovieSortBy> movieSortByEntry;
    private final FormSingleEntry<SerieSortBy> serieSortByEntry;
    private IHM ihm;

    public SearchPanel(IHM ihm) {
        this.ihm = ihm;
        setLayout(new BorderLayout());
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        jSplitPane.setDividerSize(2);
        jSplitPane.setAutoscrolls(true);
        add(jSplitPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BorderLayout());

        FormSingleEntry<String> name = new FormSingleEntry<>("VALEUR", null, s->s, s->s);
        this.nameEntry = name;
        FormListEntry<Genre> genresMovie = new FormListEntry<>("GENRES", null, Genre::getName, TMDB.getGenresMovieSorted().toArray(new Genre[]{}));
        this.genresMovieEntry = genresMovie;
        FormListEntry<Genre> genresSerie = new FormListEntry<>("GENRES", null, Genre::getName, TMDB.getGenresSerieSorted().toArray(new Genre[]{}));
        this.genresSerieEntry = genresMovie;
        FormSingleEntry<String> persons = new FormSingleEntry<>("ACTEUR(S)", null, s->s, s->s);
        this.personsEntry = persons;
        FormSingleEntry<String> years = new FormSingleEntry<>("ANNéE", null, s->s, s->s);
        this.yearsEntry = years;
        FormSingleEntry<MovieSortBy> movieSortBy = new FormSingleEntry<>("TRIER PAR", MovieSortBy.getDefault(), MovieSortBy::getName, MovieSortBy::getSortByName, s -> true, FormSingleEntry.Type.COMBOBOX, MovieSortBy.values());
        this.movieSortByEntry = movieSortBy;
        FormSingleEntry<SerieSortBy> serieSortBy = new FormSingleEntry<>("TRIER PAR", SerieSortBy.getDefault(), SerieSortBy::getName, SerieSortBy::getSortByName, s -> true, FormSingleEntry.Type.COMBOBOX, SerieSortBy.values());
        this.serieSortByEntry = serieSortBy;

        RequestListForm[] items = {
                new ListCategorie("Recherche"),
                new RequestListForm(formPanel, "Titre de film", () -> generatePanel(jSplitPane, TMDB.searchMovie(name.getValue())), name),
                new RequestListForm(formPanel, "Titre de Série", () -> generatePanel(jSplitPane, TMDB.searchTvSerie(name.getValue())), name),
                new RequestListForm(formPanel, "Nom d'acteur", () -> generatePanel(jSplitPane, TMDB.searchPerson(name.getValue())), name),
                new RequestListForm(formPanel, "Recherche company", () -> {}),
                new RequestListForm(formPanel, "Recherche collection", () -> {}),
                new ListCategorie("Récupérer"),
                new RequestListForm(formPanel, "Id de film", () -> generatePanel(jSplitPane, TMDB.getMovie(Integer.parseInt(name.getValue()))), name),
                new RequestListForm(formPanel, "Id d'acteur", () -> generatePanel(jSplitPane, TMDB.getPerson(Integer.parseInt(name.getValue()))), name),
                new RequestListForm(formPanel, "Id de série TV", () -> generatePanel(jSplitPane, TMDB.getTvSeries(Integer.parseInt(name.getValue()))), name),
                new ListCategorie("Découvrir"),
                new RequestListForm(formPanel, "Films",
                        () -> generatePanel(jSplitPane, TMDB.discoverMovie(movieSortBy.getValue(), years.getValue(), genresMovie.getValue(), persons.getValue())), persons, movieSortBy, years, genresMovie),
                new RequestListForm(formPanel, "Série TV",
                        () -> generatePanel(jSplitPane, TMDB.discoverSeries(serieSortBy.getValue(), years.getValue(), genresSerie.getValue())), serieSortBy, years, genresSerie)
        };

        JScrollPane jScrollPane = new JScrollPane();
        JList<RequestListForm> jList = new JList<>(items);
        this.jlist = jList;
        jList.setBorder(null);
        jList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> jList, Object requestListForm, int i, boolean b, boolean b1) {
                JComponent component;
                if(requestListForm instanceof ListCategorie){
                    component = (JComponent) super.getListCellRendererComponent( jList, "――― "+((ListCategorie) requestListForm).getName()+" ―――", i, b, b1 );
                }
                else component = (JComponent) super.getListCellRendererComponent( jList, ((RequestListForm) requestListForm).getName(), i, b, b1 );
                //component.setToolTipText("Essai");

                return component;
            }
        });
        jList.addListSelectionListener(listSelectionEvent -> {
            if(jList.getSelectedValue() instanceof ListCategorie) {
                jList.setSelectedIndex(jList.getSelectedIndex()+1);
            }else jList.getSelectedValue().open();
        });
        jScrollPane.setViewportView(jList);

        jSplitPane.setLeftComponent(new JPanel(new BorderLayout()){{
            setBorder(new EmptyBorder(10, 10, 10, 10));
            add(jScrollPane, BorderLayout.WEST);
            add(formPanel, BorderLayout.CENTER);
        }});
        jSplitPane.setRightComponent(new JPanel(){{{setPreferredSize(new Dimension(700, 100));}}});
        jSplitPane.setResizeWeight(1.0);
        jSplitPane.setDividerLocation(0.5);
        jList.setSelectedIndex(1);
    }

    public void generatePanel(JSplitPane parent, Future<?> result){
        ihm.getProgressData().initialize();
        ResultPanelReturn<TmdbElement> resultPanelReturn = new ResultPanelReturn<>("Ajouter ce film/série à la cinémathèque", (ob) -> {
            if(!(ob instanceof DbMovie) && !(ob instanceof DbSerie)) return;
            String title = ob instanceof DbMovie ? ((DbMovie) ob).getTitle() : ((DbSerie) ob).getName();
            FormDialog formDialog = new FormDialog(ihm, "Choisir un format", true);
            FormSingleEntry<Movie.Format> btn_group = new FormSingleEntry<>("FORMAT", null, Movie.Format::toString, Movie.Format::getIndice, r -> true, FormSingleEntry.Type.RADIOBUTTON, Movie.Format.values());
            FormActionEntry add = new FormActionEntry("Ajouter "+title, () -> {
                try {
                    Movie movie = new Movie(((IdElement) ob).getId(), title, Movie.Format.getIndice(btn_group.getValue().toString()), new Date());
                    IHM.INSTANCE.addMovie(UUID.randomUUID(), movie);
                    IHM.INSTANCE.addNotifCinematheque();
                    formDialog.dispose();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.WARNING_MESSAGE);
                }
            }, e -> {});
            Form formFormat = new Form("Ajouter", btn_group, add);
            formFormat.setPreferredSize(new Dimension(300, 200));
            formDialog.initComponents(formFormat);
        }, DbMovie.class, DbSerie.class);
        result.then(r -> {
            ResultPanel<?> jPanel = getPanelResult(null, 1, r, ihm.getProgressData(), resultPanelReturn);
            parent.setRightComponent(jPanel);
            ihm.getProgressData().close();
        }).error(e -> {
            logger.error("Aucun résultat", e);
            ihm.getProgressData().close();
            Utils.errorPopup(this, "Aucun résultat", e);
        });
    }

    public FormSingleEntry<String> getNameEntry() {
        return nameEntry;
    }

    public FormListEntry<Genre> getGenresMovieEntry() {
        return genresMovieEntry;
    }

    public FormListEntry<Genre> getGenresSerieEntry() {
        return genresSerieEntry;
    }

    public FormSingleEntry<MovieSortBy> getMovieSortByEntry() {
        return movieSortByEntry;
    }

    public FormSingleEntry<SerieSortBy> getSerieSortByEntry() {
        return serieSortByEntry;
    }

    public FormSingleEntry<String> getPersonsEntry() {
        return personsEntry;
    }

    public FormSingleEntry<String> getYearsEntry() {
        return yearsEntry;
    }

    public JList<RequestListForm> getJlist() {
        return jlist;
    }

}
