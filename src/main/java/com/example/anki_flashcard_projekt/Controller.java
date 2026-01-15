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

    // Indeholder den aktuelle træningssession med flashcards og træningsstatus
    private Træningssession træningssession;

    // Bruges til at sikre at brugeren har klikket på "Vis Svar" inden man kan vurdere svaret
    private boolean erSvaretVist = false;

    public void initialize()
    {
        try
        {
            // Indlæser en tidligere gemt træningssession fra filen Træningssession.dat
            // så brugeren kan genoptage det tidligere spil
            træningssession = Serialization.indlæsTræningssession();
        }
        catch (Exception e)
        {
            // Hvis der ikke kan indlæses en tidligere træningssession, så startes der et nyt spil
            // det sikre at programmet altid kan køre og ikke crasher
            træningssession = new Træningssession(new ArrayList<>(AnkiImport.importerAnkiFil()));
        }

        // Opdatere tællerne
        opdaterTræningsstatus();

        // Henter billedet til næste flashcard
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
        // Svaret fjernes, så feltet er klar til næste svar
        svarFelt.setText("");

        // Henter index på det flashcard der vises lige nu, og lægger 1 til, for at få næste kort
        int nuværendeFlashcardDerVises = træningssession.getNuværendeFlashcardDerVises() + 1;

        // Tjekker om vi er på sidste flashcard, hvis ja -> så tjekkes der om alle svar er korrekte og metoden stoppes
        if (nuværendeFlashcardDerVises >= træningssession.getAktuelleFlashcards().size())
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
    }

    // Metode der henter og viser billedet til flashcard
    private void visFlashcard()
    {
        // Tjekker at der findes flashcards, hvis ikke -> metoden stoppes her
        if(træningssession.getAktuelleFlashcards().isEmpty()) {return;}

        // Henter index på det flashcard der vises lige nu
        int index = træningssession.getNuværendeFlashcardDerVises();
        // Tjekker om vi er på sidste flashcard, hvis ja -> metoden stoppes her
        if (index >= træningssession.getAktuelleFlashcards().size()) {return;}

        // Henter og indsætter billedet til flashcard
        billedeFelt.setImage(træningssession.getAktuelleFlashcards().get(træningssession.getNuværendeFlashcardDerVises()).getBillede());
    }

    // Metode til korrekt-knappen
    @FXML
    void handleButtonKorrekt(MouseEvent event)
    {
        // Tjekker om brugeren har klikket vis svar, hvis ikke, så kan svaret ikke vurderes -> metoden stoppes her
        if (!erSvaretVist) {return;}

        // Henter det Flashcard der vises lige nu og markere det som korrekt besvaret
        Flashcard flashcard = træningssession.getAktuelleFlashcards().get(træningssession.getNuværendeFlashcardDerVises());
        flashcard.setKorrektBesvaretFlashcard(true);

        // Tæller 1 korrekt op
        træningssession.setKorrekt(træningssession.getKorrekt() + 1);

        // Tæller 1 op i spillede kort i denne runde, så der kan holdes styr på hvornår en
        // ny runde skal startes med forkert besvaret kort eller hvornår brugeren har vundet med 100% korrekte svar
        træningssession.setSpilledeKortIDenneRunde(træningssession.getSpilledeKortIDenneRunde() + 1);

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

        // Tæller 1 op i spillede kort i denne runde, så der kan holdes styr på hvornår en
        // ny runde skal startes med forkert besvaret kort eller hvornår brugeren har vundet med 100% korrekte svar
        træningssession.setSpilledeKortIDenneRunde(træningssession.getSpilledeKortIDenneRunde() + 1);

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

        // Tæller 1 op i spillede kort i denne runde, så der kan holdes styr på hvornår en
        // ny runde skal startes med forkert besvaret kort eller hvornår brugeren har vundet med 100% korrekte svar
        træningssession.setSpilledeKortIDenneRunde(træningssession.getSpilledeKortIDenneRunde() + 1);

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

        // Tæller 1 op i spillede kort i denne runde, så der kan holdes styr på hvornår en
        // ny runde skal startes med forkert besvaret kort eller hvornår brugeren har vundet med 100% korrekte svar
        træningssession.setSpilledeKortIDenneRunde(træningssession.getSpilledeKortIDenneRunde() + 1);

        // Hopper videre til næste flashcard
        næsteKort();
    }

    // Metode der opdaterer tallene i træningsstatussen
    private void opdaterTræningsstatus()
    {
        labelAntalKort.setText(String.valueOf(træningssession.getAktuelleFlashcards().size()));
        labelAntalKorrekte.setText(String.valueOf(træningssession.getKorrekt()));
        labelAntalNæstenKorrekte.setText(String.valueOf(træningssession.getNæstenKorrekt()));
        labelAntalDelvisKorrekte.setText(String.valueOf(træningssession.getDelvisKorrekt()));
        labelAntalIkkeKorrekte.setText(String.valueOf(træningssession.getIkkeKorrekt()));

        // Finder antal på de flashcards der ikke er spillet endnu i den aktuelle spilrunde
        int ikkeSpilledeFlashcards = træningssession.getAktuelleFlashcards().size() - træningssession.getSpilledeKortIDenneRunde();
        labelIkkeSpillet.setText(String.valueOf(ikkeSpilledeFlashcards));
    }

    // Metode til irrelevant-flashcard knap
    @FXML
    void handleButtonIrrelevantFlashcard(MouseEvent event)
    {
        // Henter det flashcard der vises lige nu og markere det som irrelevant
        Flashcard flashcard = træningssession.getAktuelleFlashcards().get(træningssession.getNuværendeFlashcardDerVises());
        flashcard.setIrrelevantFlashcard(true);

        // Fjerner det flashcard der vises lige nu fra array-listen med aktuelle flashcards
        træningssession.getAktuelleFlashcards().remove(flashcard);

        // Hvis det er sidste flashcard i listen
        if (træningssession.getNuværendeFlashcardDerVises() >= træningssession.getAktuelleFlashcards().size())
        {
            // Startes der forfra i array-listen
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
            // Hvis der sker fejl -> udskrives fejlen
            e.printStackTrace();
        }
        // Lukker programmet
        System.exit(0);
    }

    // Metode til Vis Svar knap
    @FXML
    void handleButtonVisSvar(MouseEvent event)
    {
        // Tjekker at der findes flashcards
        if(!træningssession.getAktuelleFlashcards().isEmpty())
        {
            // Indsætter svaret på det flashcard der vises lige nu
            svarFelt.setText(træningssession.getAktuelleFlashcards().get(træningssession.getNuværendeFlashcardDerVises()).getSvar());

            // Registerer at svaret nu er blevet vist
            erSvaretVist = true;
        }
    }

    // Metode til at tjekke om brugeren har vundet med 100% korrekte svar eller
    // om brugeren skal spille videre med forkert besvaret flashcards
    public void tjekOmAlleSvarErKorrekte()
    {
        // Kører alle flashcards igennem med en for-løkke
        for (Flashcard flashcard : træningssession.getAlleFlashcards())
        {
            // Hvis et flashcard IKKE er besvaret korrekt -> stoppes metoden ved return. Aka man har ikke vunder endnu.
            if (!flashcard.erFlashcardBesvaretKorrekt())
            {
                // Hvis spilleren har spillet alle flashcards igennem i den aktuelle spillerunde,
                // så startes der en ny spilrunde med ikke korrekte flashcards
                if (træningssession.getSpilledeKortIDenneRunde() == træningssession.getAktuelleFlashcards().size())
                {
                    // Starter en ny spilrunde
                    træningssession.startNyRundeMedIkkeKorrekteKort();

                    // Opdaterer tællerne
                    opdaterTræningsstatus();

                    // Henter billedet til næste flashcard
                    visFlashcard();
                }
                return;
            }
        }

        // Hvis for-løkken gennemføres uden return, så ved vi at alle flashcards er korrekte
        // og det betyder at spilleren har vundet
        duHarVundet();
    }

    // Metode til at vise brugeren at alle flashcards er færdigspillet og korrekte
    public void duHarVundet()
    {
        // Laver et pop-up vindue
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Godt klaret!");
        alert.setHeaderText("Tillykke!!!");
        alert.setContentText("Du har gennemført alle flashcards og svaret korrekt!");
        alert.showAndWait();

        // Når brugeren klikker på OK starter spillet forfra
        træningssession.startForfra();
        svarFelt.setText("");
        erSvaretVist = false;
        opdaterTræningsstatus();
        visFlashcard();
    }
}