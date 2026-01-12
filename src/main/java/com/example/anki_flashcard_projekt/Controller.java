package com.example.anki_flashcard_projekt;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

    private boolean erSvaretVist = false;

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

        // Tjekker om vi er på sidste flashcard, hvis ja -> metoden stoppes her og der tjekkes om alle svar er korrekte
        if (nuværendeFlashcardDerVises >= træningssession.getFlashcards().size())
        {
            tjekOmAlleSvarErKorrekte();
            return;
        }

        // Gemmer hvilket flashcard-objekt der vises lige nu
        træningssession.setNuværendeFlashcardDerVises(nuværendeFlashcardDerVises);

        // Henter billedet til næste flashcard
        visFlashcard();

        // Opdaterer tællerne
        opdaterTræningsstatus();

        // Tjekker om spillet er færdigt
        tjekOmAlleSvarErKorrekte();
    }

    // Metode der viser billedet til det næste kort
    private void visFlashcard()
    {
        // Tjekker at der findes flashcards, hvis ikke -> metoden stoppes her
        if(træningssession.getFlashcards().isEmpty()) {return;}

        // Henter index på det flashcard der vises lige nu
        int index = træningssession.getNuværendeFlashcardDerVises();
        // Tjekker om vi er på sidste flashcard, hvis ja -> metoden stoppes her
        if (index >= træningssession.getFlashcards().size()) {return;}

        // Henter og indsætter billedet
        billedeFelt.setImage(træningssession.getFlashcards().get(træningssession.getNuværendeFlashcardDerVises()).getBillede());
    }

    // Metode til korrekt-knappen
    @FXML
    void handleButtonKorrekt(MouseEvent event)
    {
        // Tjekker om brugeren har klikket vis svar, hvis ikke, så kan svaret ikke vurderes -> metoden stoppes her
        if (!erSvaretVist) {return;}

        // Tæller 1 op i korrekt
        træningssession.setKorrekt(træningssession.getKorrekt() + 1);
        // Hopper videre til næste flashcard
        næsteKort();
    }

    // Metode til næsten-Korrekt-knappen
    @FXML
    void handleButtonNæstenKorrekt(MouseEvent event)
    {
        // Tjekker om brugeren har klikket vis svar, hvis ikke, så kan svaret ikke vurderes -> metoden stoppes her
        if (!erSvaretVist) {return;}

        // Tæller 1 op i næsten korrekt
        træningssession.setNæstenKorrekt(træningssession.getNæstenKorrekt() + 1);
        // Hopper videre til næste flashcard
        næsteKort();
    }

    // Metode til delvis-korrekt-knappen
    @FXML
    void handleButtonDelvisKorrekt(MouseEvent event)
    {
        // Tjekker om brugeren har klikket vis svar, hvis ikke, så kan svaret ikke vurderes -> metoden stoppes her
        if (!erSvaretVist) {return;}

        // Tæller 1 op i delvis korrekt
        træningssession.setDelvisKorrekt(træningssession.getDelvisKorrekt() + 1);
        // Hopper videre til næste flashcard
        næsteKort();
    }

    // Metode til ikke-korrekt-knappen
    @FXML
    void handleButtonIkkeKorrekt(MouseEvent event)
    {
        // Tjekker om brugeren har klikket vis svar, hvis ikke, så kan svaret ikke vurderes -> metoden stoppes her
        if (!erSvaretVist) {return;}

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
        // Hvis der ikke findes flashcards, så stoppes metoden her
        if(træningssession.getFlashcards().isEmpty()) return;

        // Fjerner det flashcard der vises fra array listen med flashcards
        træningssession.getFlashcards().remove(træningssession.getNuværendeFlashcardDerVises());

        // Hvis det er sidste flashcard i listen
        if (træningssession.getNuværendeFlashcardDerVises() >= træningssession.getFlashcards().size())
        {
            // Startes der forfra i arraylisten
            træningssession.setNuværendeFlashcardDerVises(0);
        }

        // Opdaterer tællerne
        opdaterTræningsstatus();

        // Henter billedet til næste flashcard
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
        // Kalder startForfra metoden
        træningssession.startForfra();

        // Fjerner svaret fra det tidligere flashcard
        svarFelt.setText("");

        // Opdaterer tællerne
        opdaterTræningsstatus();

        // Henter billedet til næste flashcard
        visFlashcard();
    }

    // Metode til afslut-knappen
    @FXML
    void handleButtonAfslut(MouseEvent event)
    {
        try
        {
            // Gemmer træningssessionen, så den kan genoptages når man åbner programmet igen
            Serialization.gemTræningssession(træningssession);
        }
        catch (Exception e)
        {
            // Hvis der sker fejl udskrives fejlen
            e.printStackTrace();
        }
        // Lukker programmet
        System.exit(0);
    }

    // Metode til vis-svar knappen
    @FXML
    void handleButtonVisSvar(MouseEvent event)
    {
        // Tjekker at der findes flashcards
        if(!træningssession.getFlashcards().isEmpty())
        {
            // Indsætter svaret på det flashcard der vises lige nu
            svarFelt.setText(træningssession.getFlashcards().get(træningssession.getNuværendeFlashcardDerVises()).getSvar());
        }
        erSvaretVist = true;
    }

    public void tjekOmAlleSvarErKorrekte()
    {
        // Henter antal flashcards i hele bunken
        int antalFlashcards = træningssession.getFlashcards().size();

        // Hvis der ingen flashcards findes -> metoden stoppes
        if(antalFlashcards == 0) {return;}

        // Henter antal flashcard der er spillet
        int antalSpilledeKort = træningssession.getKorrekt() + træningssession.getNæstenKorrekt()
                + træningssession.getDelvisKorrekt() + træningssession.getIkkeKorrekt();

        // Tjekker om alle flashcards er spillet og om alle svar er korrekte
        if (antalSpilledeKort == antalFlashcards && træningssession.getKorrekt() == antalFlashcards)
        {
            duHarVundet();
        }
    }

    // Metode til at vise brugeren at alle flashcards er færdigspillet og korrekte
    public void duHarVundet()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Godt klaret!");
        alert.setHeaderText("Tillykke!");
        alert.setContentText("Du har gennemført alle flashcards og svaret korrekt!");
        alert.showAndWait();
    }
}