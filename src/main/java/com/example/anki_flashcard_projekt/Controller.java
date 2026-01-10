package com.example.anki_flashcard_projekt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.IOException;

import static com.example.anki_flashcard_projekt.Serialization.skrivFlashcardObjekterNedIFil;

public class Controller
{
    @FXML
    private Label svarFelt;

    @FXML
    private ImageView billedeFelt;

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
    private ObservableList<Flashcard> flashcards = FXCollections.observableArrayList();

    // Holder styr på hvilket kort der vises lige nu
    private int nuværendeFlashcardDerVises = 0;

    // Holder styr på brugerens vurdering af svar
    private int korrekt = 0;
    private int næsten_korrekt = 0;
    private int delvis_korrekt = 0;
    private int ikke_korrekt = 0;

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
        opdaterTræningsstatus();
        visNæsteKort();
    }

    // Metode der bruges til at gemme data når brugeren lukker programmet
    public void gemData() throws IOException
    {
        skrivFlashcardObjekterNedIFil(flashcards); // Gemmer Flashcard-objekter i filen flashcards.txt
    }

    // Metode der skifter til næste kort i ObservableList
    private void næsteKort()
    {
        svarFelt.setText(""); // Svaret fjernes, så feltet er tomt og klar til næste svar
        nuværendeFlashcardDerVises = nuværendeFlashcardDerVises + 1; // Vi tæller op hver gang der vises et flashcard

        // Hvis der ikke er flere flashcards
        if (nuværendeFlashcardDerVises >= flashcards.size())
        {
            nuværendeFlashcardDerVises = 0; // Starter listen forfra
        }

        visNæsteKort(); // Henter billedet til næste flashcard
        opdaterTræningsstatus(); // Opdaterer tallene
    }

    // Metode der viser billedet til det næste kort
    private void visNæsteKort()
    {
        // Tjekker at der findes flashcards
        if (!flashcards.isEmpty())
        {
            billedeFelt.setImage(flashcards.get(nuværendeFlashcardDerVises).getBilledeSti()); // Henter og indsætter billedet
        }
    }

    // Metode til korrekt-knappen
    @FXML
    void handleButtonKorrekt(MouseEvent event)
    {
        korrekt = korrekt + 1;
        næsteKort();
    }

    // Metode til næsten-Korrekt-knappen
    @FXML
    void handleButtonNæstenKorrekt(MouseEvent event)
    {
        næsten_korrekt = næsten_korrekt + 1;
        næsteKort();
    }

    // Metode til delvis-korrekt-knappen
    @FXML
    void handleButtonDelvisKorrekt(MouseEvent event)
    {
        delvis_korrekt = delvis_korrekt + 1;
        næsteKort();
    }

    // Metode til ikke-korrekt-knappen
    @FXML
    void handleButtonIkkeKorrekt(MouseEvent event)
    {
        ikke_korrekt = ikke_korrekt + 1;
        næsteKort();
    }

    // Metode der opdaterer tallene over i træningsstatus
    private void opdaterTræningsstatus()
    {
        labelAntalKort.setText(String.valueOf(flashcards.size()));
        labelAntalKorrekte.setText(String.valueOf(korrekt));
        labelAntalNæstenKorrekte.setText(String.valueOf(næsten_korrekt));
        labelAntalDelvisKorrekte.setText(String.valueOf(delvis_korrekt));
        labelAntalIkkeKorrekte.setText(String.valueOf(ikke_korrekt));

        int spilledeKort = korrekt + næsten_korrekt + delvis_korrekt + ikke_korrekt;

        labelIkkeSpillet.setText(String.valueOf(flashcards.size() - spilledeKort));
    }

    // Metode til irrelevant-flashcard-knappen
    @FXML
    void handleButtonIrrelevantFlashcard(MouseEvent event)
    {
        flashcards.remove(nuværendeFlashcardDerVises);

        if (nuværendeFlashcardDerVises >= flashcards.size())
        {
            nuværendeFlashcardDerVises = 0;
        }

        opdaterTræningsstatus();
        visNæsteKort();
    }

    // Metode til import-knappen
    @FXML
    void handleButtonImportAnkiFlashcardSet(MouseEvent event)
    {
        flashcards = AnkiImport.importerAnkiFil();

        nuværendeFlashcardDerVises = 0;
        korrekt = 0;
        næsten_korrekt = 0;
        delvis_korrekt = 0;
        ikke_korrekt = 0;

        opdaterTræningsstatus();
        visNæsteKort();
    }

    // Metode til pause-knappen
    @FXML
    void handleButtonPause(MouseEvent event)
    {

    }

    // Metode til start-forfra-knappen
    @FXML
    void handleButtonStartForfra(MouseEvent event)
    {
        nuværendeFlashcardDerVises = 0;
        korrekt = 0;
        næsten_korrekt = 0;
        delvis_korrekt = 0;
        ikke_korrekt = 0;

        svarFelt.setText("");

        opdaterTræningsstatus();
        visNæsteKort();
    }

    // Metode til afslut-knappen
    @FXML
    void handleButtonAfslut(MouseEvent event)
    {

    }

    // Metode til vis-svar knappen
    @FXML
    void handleButtonVisSvar(MouseEvent event)
    {
        if(!flashcards.isEmpty())
        {
            svarFelt.setText(flashcards.get(nuværendeFlashcardDerVises).getSvar());
        }
    }
}