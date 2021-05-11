/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: Cinematheque
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import fr.hedwin.Main;
import fr.hedwin.db.TMDB;
import fr.hedwin.db.model.IdElement;
import fr.hedwin.db.model.TmdbElement;
import fr.hedwin.db.object.DbMovie;
import fr.hedwin.db.object.DbSerie;
import fr.hedwin.db.utils.CompletableFuture;
import fr.hedwin.db.utils.Future;
import fr.hedwin.objects.Movie;
import fr.hedwin.swing.IHM;
import fr.hedwin.swing.panel.result.ResultPanel;
import fr.hedwin.swing.panel.result.properties.ResultPanelReturn;
import fr.hedwin.swing.panel.utils.form.Form;
import fr.hedwin.swing.panel.utils.form.FormActionEntry;
import fr.hedwin.swing.panel.utils.form.FormSingleEntry;
import fr.hedwin.swing.panel.utils.table.*;
import fr.hedwin.swing.window.CommentDialog;
import fr.hedwin.swing.window.FormDialog;
import fr.hedwin.swing.window.ResultsDialog;
import fr.hedwin.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Cinematheque extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(Cinematheque.class);

    private Table<Movie> table;
    private IHM ihm;

    public Cinematheque(IHM ihm){
        this.ihm = ihm;
        setLayout(new BorderLayout());
        Column[] columnList = new Column[]{
                new ColumnObject<>("Nom", Movie::getNom),
                new ColumnInteger<>("TMDB ID", Movie::getIdTmdbLink, i -> i != -1),
                new ColumnObject<>("Format", Movie::getFormat),
                new ColumnInteger<>("Note (/100)", Movie::getNote),
                new ColumnDate<>("Date d'ajout", new SimpleDateFormat("dd/MM/yyyy"), Movie::getDate),
                new ColumnAction<Movie>(new FlatSVGIcon("images/suggestedRefactoringBulb_dark.svg"), "Rediger ou modifier un avis", (row, movie) -> {
                    new CommentDialog(ihm, "Commentaires de "+movie.getNom(), true, IHM.getUUIDFromMovie(movie));
                }),
                new ColumnAction<Movie>(new FlatSVGIcon("images/find_dark.svg"), "Rechercher dans TMDB", (row, movie) -> {
                    ihm.getProgressData().initialize();
                    Consumer<ResultPanel<?>> openDialog = (panel) -> new ResultsDialog(ihm, movie.getNom(), true, panel).setVisible(true);
                    //SI LE FILM/SERIE EST ASSOCIEE A UN IDENTIFIANT THEMOVIEDB
                    if (movie.getIdTmdbLink() != -1)
                        TMDB.getMovie(movie.getIdTmdbLink()).then(m -> openDialog.accept(Utils.getMoviePanel(1, m, ihm.getProgressData()))).error(ex -> {
                            TMDB.getTvSeries(movie.getIdTmdbLink()).then(s -> openDialog.accept(Utils.getSeriePanel(1, s, ihm.getProgressData()))).error(ex2 -> {
                                JPanel jPanel = new JPanel();
                                jPanel.add(new JLabel(ex.getMessage()));
                                jPanel.add(new JLabel(ex2.getMessage()));
                                JOptionPane.showMessageDialog(this, jPanel,
                                        "L'identifiant "+movie.getIdTmdbLink()+" associé à "+movie.getNom()+" n'est associé à aucun film ou série", JOptionPane.WARNING_MESSAGE);
                            });
                        });
                    else {
                        Map<String, Future<? extends TmdbElement>> futureMap = new HashMap<String, Future<? extends TmdbElement>>(){{{
                            put("Film", TMDB.searchMovie(movie.getNom()));
                            put("Serie", TMDB.searchTvSerie(movie.getNom()));
                        }}};
                        ResultPanelReturn<TmdbElement> resultPanelReturn = new ResultPanelReturn<>("Associer ce film", (o) -> {
                            if(o instanceof IdElement){
                                movie.setIdTmdbLink(((IdElement) o).getId());
                                row.update();
                            }
                        }, DbMovie.class, DbSerie.class);
                        CompletableFuture.async(() -> futureMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> {
                            try {
                                return e.getValue().call();
                            } catch (Exception exception) {
                                return null;
                            }
                        }))).then((map) -> {
                            try {
                                openDialog.accept(Utils.getMultipleResultPanel(map, ihm.getProgressData(), resultPanelReturn));
                            } catch (Exception e) {
                                logger.error("Impossible de charger un multiple result panel", e);
                            }
                        });
                    }
                }),
                new ColumnAction<Movie>(new FlatSVGIcon("images/edit_dark.svg"), "Modifier le film/série", (row, movie) -> {
                    FormDialog formDialog = new FormDialog(ihm, "Modifier "+movie.getNom(), true);
                    FormSingleEntry<String> formEntrie = new FormSingleEntry<>("NAME", movie.getNom(), s->s, s->s);
                    FormSingleEntry<Movie.Format> btn_group = new FormSingleEntry<>("FORMAT", movie.getFormat(), Movie.Format::toString, Movie.Format::getIndice, Objects::nonNull, FormSingleEntry.Type.RADIOBUTTON, Movie.Format.values());
                    FormActionEntry update = new FormActionEntry("Modifier", () -> {
                        try {
                            movie.setNom(formEntrie.getValue());
                            movie.setFormat(btn_group.getValue());
                            row.update();
                            formDialog.dispose();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.WARNING_MESSAGE);
                        }
                    }, e -> {});
                    Form form = new Form("Modifier", formEntrie, btn_group, update);
                    form.setPreferredSize(new Dimension(300, 300));
                    formDialog.initComponents(form);
                }),
                new ColumnAction<Movie>(new FlatSVGIcon("images/remove_dark.svg"), "Supprimer le film/série", (row, movie) -> {
                    int r = JOptionPane.showConfirmDialog(ihm, "Etes-vous sur de vouloir supprimer ?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (r == JOptionPane.YES_OPTION) {
                        row.remove();
                        Main.movies.remove(IHM.getUUIDFromMovie(movie));
                    }
                })
        };
        table = new Table<Movie>(40, 40, columnList).generate();
        add(table, BorderLayout.CENTER);
    }

    public Table<Movie> getTable() {
        return table;
    }

}
