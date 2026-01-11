package com.example.anki_flashcard_projekt;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Controller
{
    @FXML
    private Label svarFelt;

    @FXML
    private ImageView billedeFelt;

    @FXML
    private Label labelAntalKorrekte;

    @FXML
    private Label labelAntalNæstenKorrekte;

    @FXML
    private Label labelAntalDelvisKorrekte;

    @FXML
    private Label labelAntalIkkeKorrekte;

    @FXML
    private Label labelAntalKort;

    @FXML
    private Label labelIkkeSpillet;

    private Træningssession træningssession;

    public void initialize()
    {
        try
        {
            // Indlæser en tidligere gemt træningssession fra filen Træningssession.dat
            træningssession = Serialization.indlæsTræningssession();
        }
        catch (Exception e) // Hvis der opstår en fejl under indlæsningen,
        {
            // så oprettes der en ny Træningssession, så man starter på en frisk
            træningssession = new Træningssession(new ArrayList<>(AnkiImport.importerAnkiFil()));
        }

        // Opdatere tællerne
        opdaterTræningsstatus();

        // Henter Flashcard
        visFlashcard();
    }

    // Metode der bruges til at gemme data når brugeren lukker programmet
    public void gemData() throws IOException
    {
        Serialization.gemTræningssession(træningssession);
    }

    // Metode der skifter til næste kort
    private void næsteKort()
    {
        // Svaret fjernes, så feltet er tomt og klar til næste svar
        svarFelt.setText("");

        // Henter index på det flashcard der vises lige nu, og lægger 1 til, for at få næste kort
        int nuværendeFlashcardDerVises = træningssession.getNuværendeFlashcardDerVises() + 1;

        // Hvis der ikke er flere flashcards
        if (nuværendeFlashcardDerVises >= træningssession.getFlashcards().size())
        {
            nuværendeFlashcardDerVises = 0; // Starter listen forfra
        }

        // Gemmer hvilket flashcard-objekt der vises lige nu
        træningssession.setNuværendeFlashcardDerVises(nuværendeFlashcardDerVises);

        // Henter billedet til næste flashcard
        visFlashcard();

        // Opdaterer tællerne
        opdaterTræningsstatus();
    }

    // Metode der viser billedet til det næste kort
    private void visFlashcard()
    {
        // Tjekker at der findes flashcards
        if (!træningssession.getFlashcards().isEmpty())
        {
            // Henter og indsætter billedet
            billedeFelt.setImage(træningssession.getFlashcards().get(træningssession.getNuværendeFlashcardDerVises()).getBillede());
        }
    }

    // Metode til korrekt-knappen
    @FXML
    void handleButtonKorrekt(MouseEvent event)
    {
        // Tæller 1 op i korrekt
        træningssession.setKorrekt(træningssession.getKorrekt() + 1);
        // Hopper videre til næste flashcard
        næsteKort();
    }

    // Metode til næsten-Korrekt-knappen
    @FXML
    void handleButtonNæstenKorrekt(MouseEvent event)
    {
        // Tæller 1 op i næsten korrekt
        træningssession.setNæstenKorrekt(træningssession.getNæstenKorrekt() + 1);
        // Hopper videre til næste flashcard
        næsteKort();
    }

    // Metode til delvis-korrekt-knappen
    @FXML
    void handleButtonDelvisKorrekt(MouseEvent event)
    {
        // Tæller 1 op i delvis korrekt
        træningssession.setDelvisKorrekt(træningssession.getDelvisKorrekt() + 1);
        // Hopper videre til næste flashcard
        næsteKort();
    }

    // Metode til ikke-korrekt-knappen
    @FXML
    void handleButtonIkkeKorrekt(MouseEvent event)
    {
        // Tæller 1 op i ikke korrekt
        træningssession.setIkkeKorrekt(træningssession.getIkkeKorrekt() + 1);
        // Hopper videre til næste flashcard
        næsteKort();
    }

    // Metode der opdaterer tallene over i træningsstatus
    private void opdaterTræningsstatus()
    {
        labelAntalKort.setText(String.valueOf(træningssession.getFlashcards().size()));
        labelAntalKorrekte.setText(String.valueOf(træningssession.getKorrekt()));
        labelAntalNæstenKorrekte.setText(String.valueOf(træningssession.getNæstenKorrekt()));
        labelAntalDelvisKorrekte.setText(String.valueOf(træningssession.getDelvisKorrekt()));
        labelAntalIkkeKorrekte.setText(String.valueOf(træningssession.getIkkeKorrekt()));

        int spilledeKort = træningssession.getKorrekt() + træningssession.getNæstenKorrekt() + træningssession.getDelvisKorrekt() + træningssession.getIkkeKorrekt();

        labelIkkeSpillet.setText(String.valueOf(træningssession.getFlashcards().size() - spilledeKort));
    }

    // Metode til irrelevant-flashcard-knappen
    @FXML
    void handleButtonIrrelevantFlashcard(MouseEvent event)
    {
        if(træningssession.getFlashcards().isEmpty()) return;

        træningssession.getFlashcards().remove(træningssession.getNuværendeFlashcardDerVises());

        if (træningssession.getNuværendeFlashcardDerVises() >= træningssession.getFlashcards().size())
        {
            træningssession.setNuværendeFlashcardDerVises(0);
        }

        opdaterTræningsstatus();
        visFlashcard();
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
        træningssession.startForfra();
        svarFelt.setText("");

        opdaterTræningsstatus();
        visFlashcard();
    }

    // Metode til afslut-knappen
    @FXML
    void handleButtonAfslut(MouseEvent event)
    {
        try
        {
            Serialization.gemTræningssession(træningssession);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.exit(0);
    }

    // Metode til vis-svar knappen
    @FXML
    void handleButtonVisSvar(MouseEvent event)
    {
        if(!træningssession.getFlashcards().isEmpty())
        {
            svarFelt.setText(træningssession.getFlashcards().get(træningssession.getNuværendeFlashcardDerVises()).getSvar());
        }
    }
}