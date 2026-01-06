package com.example.anki_flashcard_projekt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

import static com.example.anki_flashcard_projekt.Serialization.skrivFlashcardObjekterNedIFil;

public class Controller
{
    @FXML
    private TextArea answerField;

    @FXML
    private ImageView imageField;

    @FXML
    private Label labelAntalDelvisKorrekte;

    @FXML
    private Label labelAntalIkkeKorrekte;

    @FXML
    private Label labelAntalKorrekte;

    @FXML
    private Label labelAntalKort;

    @FXML
    private Label labelAntalNæstenKorrekte;

    @FXML
    private Label labelIkkeSpillet;

    // Definition af listen der holder kortene
    private final ObservableList<Flashcard> flashcards = FXCollections.observableArrayList();

    public void initialize()
    {
        try
        {
            ObservableList<Flashcard> flashcards = Serialization.indlæsFlashcardObjekterFraFil(); // Indlæser data fra fil
        }
        catch (Exception e) // Ved fejl i indlæsning af filerne får brugeren besked
        {
            System.out.println("Fejl ved indlæsning af filer");
        }
    }

    // Metode der bruges til at gemme data når brugeren lukker programmet
    public void gemData() throws IOException
    {
        skrivFlashcardObjekterNedIFil(flashcards); // Gemmer Flashcard-objekter i filen flashcards.txt
    }

    @FXML
    void handleButtonAfslut(MouseEvent event)
    {

    }

    @FXML
    void handleButtonIkkeKorrekt(MouseEvent event)
    {

    }

    @FXML
    void handleButtonIrrelevantFlashcard(MouseEvent event)
    {

    }

    @FXML
    void handleButtonImportAnkiFlashcardSet(MouseEvent event)
    {

    }

    @FXML
    void handleButtonKorrekt(MouseEvent event)
    {

    }

    @FXML
    void handleButtonNæstenKorrekt(MouseEvent event)
    {

    }

    @FXML
    void handleButtonPause(MouseEvent event)
    {

    }

    @FXML
    void handleButtonStartForfra(MouseEvent event)
    {

    }

    @FXML
    void handleButtonVisDelvisKorrekt(MouseEvent event)
    {

    }

    @FXML
    void handleButtonVisSvar(MouseEvent event)
    {

    }
}