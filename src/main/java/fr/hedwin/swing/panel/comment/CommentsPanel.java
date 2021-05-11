/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: CommentsPanel
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.swing.panel.comment;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import fr.hedwin.Main;
import fr.hedwin.objects.Comment;
import fr.hedwin.objects.Movie;
import fr.hedwin.swing.IHM;
import fr.hedwin.swing.other.CardPanel;
import fr.hedwin.swing.panel.utils.form.Form;
import fr.hedwin.swing.panel.utils.form.FormActionEntry;
import fr.hedwin.swing.panel.utils.form.FormSingleEntry;
import fr.hedwin.swing.panel.utils.form.FormSingleNumberEntry;
import fr.hedwin.swing.window.CommentDialog;
import fr.hedwin.swing.window.FormDialog;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

public class CommentsPanel extends JPanel {

    private CommentDialog commentDialog;
    private final Movie movie;
    private final UUID movieUuid;
    private final Collection<Comment> commentList;
    private final Box centerPanel = Box.createVerticalBox();

    public CommentsPanel(CommentDialog commentDialog, UUID movieUuid){
        this.commentDialog = commentDialog;
        this.movie = Main.movies.get(movieUuid);
        this.movieUuid = movieUuid;
        this.commentList = movie.getComments().values();
        initComponents();
    }

    public void initComponents(){
        setLayout(new BorderLayout());

        JToolBar jToolBar = new JToolBar(JToolBar.HORIZONTAL);
        JButton jButton = new JButton("Ajouter un commentaire");
        jButton.addActionListener(evt -> addCommentButton());
        jToolBar.add(jButton);
        add(jToolBar, BorderLayout.NORTH);

        if(commentList.isEmpty()) centerPanel.add(new JLabel("Aucun commentaire"));
        for (Comment comment : commentList) addComment(movie, comment);
        JScrollPane jScrollPane = new JScrollPane(centerPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setBorder(null);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(jScrollPane, BorderLayout.CENTER);
        setPreferredSize(new Dimension(700, 400));
    }

    private void addCommentButton(){
        FormDialog formDialog = new FormDialog(commentDialog, "Ajouter un commentaire au film "+movie.getNom(), true);
        FormSingleEntry<String> formEntrie = new FormSingleEntry<>("TITLE", "C'est un super film !!", s->s, s->s);
        FormSingleNumberEntry note = new FormSingleNumberEntry("NOTE (entre 0 et 100)", 0, i -> i >= 0 && i <= 100);
        FormSingleEntry<String> formEntrieComment = new FormSingleEntry<>("COMMENTAIRE", null, s->s, s->s, FormSingleEntry.Type.TEXTAREA);
        FormActionEntry send = new FormActionEntry("Envoyer", () -> {
            UUID commentUuid = UUID.randomUUID();
            Comment comment = new Comment(commentUuid, movieUuid, IHM.INSTANCE.getUser(), formEntrie.getValue(), note.getValue(), formEntrieComment.getValue(), new Date());
            movie.addComment(comment);
            IHM.INSTANCE.getCinematheque().getTable().getRow(movieUuid).update();
            addComment(movie, comment);
            formDialog.dispose();
        }, e -> {});
        Form form = new Form("Modifier", formEntrie, note, formEntrieComment, send);
        form.setPreferredSize(new Dimension(500, 350));
        formDialog.initComponents(form);
    }

    private void addComment(Movie movie, Comment comment){
        CardPanel<Comment> cardPanel = new CardPanel<>(comment);
        JPanel jCard = new JPanel(new BorderLayout()){{
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#2F333D")));
            add(cardPanel, BorderLayout.CENTER);
        }};
        cardPanel.addElementEntry("Auteur", c -> c.getUser().getName());
        cardPanel.addElementEntry("Titre", Comment::getTitle);
        cardPanel.addElementEntry("Note", Comment::getNote);
        cardPanel.addElementEntryln("Commentaire", Comment::getContent);
        if(comment.getUser().equals(IHM.INSTANCE.getUser())) {
            cardPanel.addButton(new FlatSVGIcon("images/edit_dark.svg"), "Modifier mon commentaire", () -> {
                FormDialog formDialog = new FormDialog(commentDialog, "Modifier votre commentaire sur "+movie.getNom(), true);
                FormSingleEntry<String> formEntrie = new FormSingleEntry<>("TITLE", comment.getTitle(), s->s, s->s);
                FormSingleNumberEntry note = new FormSingleNumberEntry("NOTE (entre 0 et 100)", comment.getNote(), i -> i >= 0 && i <= 100);
                FormSingleEntry<String> formEntrieComment = new FormSingleEntry<>("COMMENTAIRE", comment.getContent(), s->s, s->s, FormSingleEntry.Type.TEXTAREA);
                FormActionEntry send = new FormActionEntry("Appliquer les modifications", () -> {
                    comment.setTitle(formEntrie.getValue());
                    comment.setContent(formEntrieComment.getValue());
                    comment.setNote(note.getValue());
                    IHM.INSTANCE.getCinematheque().getTable().getRow(movieUuid).update();
                    cardPanel.update();
                    formDialog.dispose();
                }, e -> JOptionPane.showMessageDialog(formDialog, e.getMessage(), e.getMessage(), JOptionPane.WARNING_MESSAGE));
                Form form = new Form("Modifier", formEntrie, note, formEntrieComment, send);
                form.setPreferredSize(new Dimension(500, 350));
                formDialog.initComponents(form);
            });
            cardPanel.addButton(new FlatSVGIcon("images/remove_dark.svg"), "Supprimer mon commentaire", () -> {
                int r = JOptionPane.showConfirmDialog(commentDialog, "Etes-vous sur de vouloir supprimer votre commentaire ?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (r == JOptionPane.YES_OPTION) {
                    movie.removeComment(comment.getUuid());
                    Arrays.stream(centerPanel.getComponents()).filter(c -> c == jCard).forEach(centerPanel::remove);
                    update();
                }
            });
        }
        cardPanel.addButton(new FlatSVGIcon("images/sendHovered.svg"), "Répondre", () -> {});

        Arrays.stream(centerPanel.getComponents()).filter(c -> c instanceof Box.Filler).forEach(centerPanel::remove);
        jCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int)jCard.getPreferredSize().getHeight()));
        centerPanel.add(jCard);
        centerPanel.add(Box.createVerticalGlue());
        update();
    }

    private void update(){
        centerPanel.repaint();
        centerPanel.revalidate();
    }

}
