package com.example.anki_flashcard_projekt;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import javafx.util.Duration;
import java.time.LocalDateTime;
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
    private Label labelVenteTekst;

    @FXML
    private Label labelIkkeSpillet;

    // Indeholder den aktuelle træningssession med flashcards og træningsstatus
    private Træningssession træningssession;

    // Bruges til at sikre at brugeren har klikket på "Vis Svar" inden man kan vurdere svaret
    private boolean erSvaretVist = false;

    // Holder styr på hvilket flashcard der vises lige nu
    private Flashcard aktueltFlashcard;

    // JavaFX-timer der bruges til automatisk at tjekke, hvornår næste flashcard er klar til visning i forhold til tidslås
    private Timeline venteTimer;

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

        // 'Vente'-teksten skjules
        labelVenteTekst.setVisible(false);

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

    // Metode der henter og viser billedet til et flashcard
    private void visFlashcard()
    {
        // Henter næste flashcard der er klar til at blive vist
        aktueltFlashcard = træningssession.findDetNæsteFlashcardDerErKlarTilVisning();

        // Hvis ingen flashcards er klar lige nu pga. tidslås, så får brugeren besked på at vente
        if (aktueltFlashcard == null)
        {
            labelVenteTekst.setText("Ingen flashcards er klar til visning lige nu. \n" + "Vent lidt med at spille videre - næste kort vises automatisk.");
            labelVenteTekst.setVisible(true); // Viser 'Vente'-teksten
            billedeFelt.setImage(null);
            tjekOmFlashcardErKlarTilVisning(); // Starter vente timeren, så brugeren automatisk får vist næste flashcard
            return;
        }
        // Hvis et flashcard er klar til visning
        if (venteTimer != null)
        {
            venteTimer.stop(); // Stoppes vente timeren, så den ikke tæller når et flashcard vises
        }

        // Skjuler 'Vente'-teksten
        labelVenteTekst.setVisible(false);
        // Billedet hentes
        billedeFelt.setImage(aktueltFlashcard.getBillede());
        // Og svaret fjernes fra det tidligere flashcard
        svarFelt.setText("");
        erSvaretVist = false;
    }

    // Metode der tjekker 1 gang i sekundet om tidslåste flashcard er klar til visning
    private void tjekOmFlashcardErKlarTilVisning()
    {
        // Hvis der findes en gammel timer, så stoppes den
        if (venteTimer != null)
        {
            venteTimer.stop();
        }

        venteTimer = new Timeline(
                new KeyFrame(Duration.seconds(1), e ->
                {
                    Flashcard flashcardDerErKlar = træningssession.findDetNæsteFlashcardDerErKlarTilVisning();

                    if (flashcardDerErKlar != null)
                    {
                        venteTimer.stop();
                        visFlashcard();
                    }
                })
        );
        venteTimer.setCycleCount(Timeline.INDEFINITE);
        venteTimer.play();
    }

    // Metode til korrekt-knappen
    @FXML
    void handleButtonKorrekt(MouseEvent event)
    {
        // Tjekker om brugeren har klikket vis svar, hvis ikke, så kan svaret ikke vurderes -> metoden stoppes her
        // eller hvis ingen flashcards er klar lige nu pga. tidslås, så kan svaret ikke vurderes -> metoden stoppes her
        //if (!erSvaretVist || aktueltFlashcard == null) {return;}
        if (aktueltFlashcard == null) {return;} // Skal slettes efter tjek!!!!!!!!!!!!!!!!!!!!!!!!!!

        // Markerer flashcard der vises lige nu som korrekt besvaret
        aktueltFlashcard.setKorrektBesvaretFlashcard(true);
        aktueltFlashcard.setNæstenKorrektBesvaretFlashcard(false);
        aktueltFlashcard.setDelvisKorrektBesvaretFlashcard(false);
        aktueltFlashcard.setIkkeKorrektBesvaretFlashcard(false);

        // Markerer flashcard der vises lige nu som besvaret/vurderet
        aktueltFlashcard.setBesvaretFlashcard(true);

        // Dette flashcard kan først vises igen tidligst om 4 dage
        aktueltFlashcard.setNæsteVisning(LocalDateTime.now().plusDays(4));

        // Tjekker om alle flashcards er besvaret korrekt, hvis ja -> brugeren har vundet og metoden stoppes
        if (træningssession.tjekOmAlleSvarErKorrekte())
        {
            duHarVundet();
            return;
        }

        // Henter billedet til næste flashcard
        visFlashcard();

        // Opdatere tællerne
        opdaterTræningsstatus();
    }

    // Metode til næsten-Korrekt-knappen
    @FXML
    void handleButtonNæstenKorrekt(MouseEvent event)
    {
        // Tjekker om brugeren har klikket vis svar, hvis ikke, så kan svaret ikke vurderes -> metoden stoppes her
        // eller hvis ingen flashcards er klar lige nu pga. tidslås, så kan svaret ikke vurderes -> metoden stoppes her
        //if (!erSvaretVist || aktueltFlashcard == null) {return;}
        if (aktueltFlashcard == null) {return;} // Skal slettes efter tjek!!!!!!!!!!!!!!!!!!!!!!!!!!

        // Markerer flashcard der vises lige nu som næsten korrekt besvaret
        aktueltFlashcard.setKorrektBesvaretFlashcard(false);
        aktueltFlashcard.setNæstenKorrektBesvaretFlashcard(true);
        aktueltFlashcard.setDelvisKorrektBesvaretFlashcard(false);
        aktueltFlashcard.setIkkeKorrektBesvaretFlashcard(false);

        // Markerer flashcard der vises lige nu som besvaret/vurderet
        aktueltFlashcard.setBesvaretFlashcard(true);

        // Dette flashcard kan først vises igen tidligst om 10 min
        aktueltFlashcard.setNæsteVisning(LocalDateTime.now().plusMinutes(10));

        // Henter billedet til næste flashcard
        visFlashcard();

        // Opdatere tællerne
        opdaterTræningsstatus();
    }

    // Metode til delvis-korrekt-knappen
    @FXML
    void handleButtonDelvisKorrekt(MouseEvent event)
    {
        // Tjekker om brugeren har klikket vis svar, hvis ikke, så kan svaret ikke vurderes -> metoden stoppes her
        // eller hvis ingen flashcards er klar lige nu pga. tidslås, så kan svaret ikke vurderes -> metoden stoppes her
        //if (!erSvaretVist || aktueltFlashcard == null) {return;}
        if (aktueltFlashcard == null) {return;} // Skal slettes efter tjek!!!!!!!!!!!!!!!!!!!!!!!!!!

        // Markerer flashcard der vises lige nu som delvis korrekt besvaret
        aktueltFlashcard.setKorrektBesvaretFlashcard(false);
        aktueltFlashcard.setNæstenKorrektBesvaretFlashcard(false);
        aktueltFlashcard.setDelvisKorrektBesvaretFlashcard(true);
        aktueltFlashcard.setIkkeKorrektBesvaretFlashcard(false);

        // Markerer flashcard der vises lige nu som besvaret/vurderet
        aktueltFlashcard.setBesvaretFlashcard(true);

        // Dette flashcard kan først vises igen tidligst om 5 min
        aktueltFlashcard.setNæsteVisning(LocalDateTime.now().plusMinutes(5));

        // Henter billedet til næste flashcard
        visFlashcard();

        // Opdatere tællerne
        opdaterTræningsstatus();
    }

    // Metode til ikke-korrekt-knappen
    @FXML
    void handleButtonIkkeKorrekt(MouseEvent event)
    {
        // Tjekker om brugeren har klikket vis svar, hvis ikke, så kan svaret ikke vurderes -> metoden stoppes her
        // eller hvis ingen flashcards er klar lige nu pga. tidslås, så kan svaret ikke vurderes -> metoden stoppes her
        //if (!erSvaretVist || aktueltFlashcard == null) {return;}
        if (aktueltFlashcard == null) {return;} // Skal slettes efter tjek!!!!!!!!!!!!!!!!!!!!!!!!!!

        // Markerer flashcard der vises lige nu som ikke korrekt besvaret
        aktueltFlashcard.setKorrektBesvaretFlashcard(false);
        aktueltFlashcard.setNæstenKorrektBesvaretFlashcard(false);
        aktueltFlashcard.setDelvisKorrektBesvaretFlashcard(false);
        aktueltFlashcard.setIkkeKorrektBesvaretFlashcard(true);

        // Markerer flashcard der vises lige nu som besvaret/vurderet
        aktueltFlashcard.setBesvaretFlashcard(true);

        // Dette flashcard kan først vises igen tidligst om 1 min
        aktueltFlashcard.setNæsteVisning(LocalDateTime.now().plusMinutes(1));

        // Henter billedet til næste flashcard
        visFlashcard();

        // Opdatere tællerne
        opdaterTræningsstatus();
    }

    // Metode der opdaterer tællerne i træningsstatussen
    private void opdaterTræningsstatus()
    {
        // Tæller brugerens vurdering af svar
        int korrekt = 0;
        int næstenKorrekt = 0;
        int delvisKorrekt = 0;
        int ikkeKorrekt = 0;
        int ikkeSpilledeFlashcards = 0;

        for (Flashcard flashcard : træningssession.getAktuelleFlashcards())
        {
            if (flashcard.erFlashcardBesvaretKorrekt())
            {
                korrekt = korrekt + 1;
            }
            else if (flashcard.erFlashcardBesvaretNæstenKorrekt())
            {
                næstenKorrekt = næstenKorrekt + 1;
            }
            else if (flashcard.erFlashcardBesvaretDelvisKorrekt())
            {
                delvisKorrekt = delvisKorrekt + 1;
            }
            else if (flashcard.erFlashcardBesvaretIkkeKorrekt())
            {
                ikkeKorrekt = ikkeKorrekt + 1;
            }

            if (!flashcard.erFlashcardBesvaret())
            {
                ikkeSpilledeFlashcards = ikkeSpilledeFlashcards + 1;
            }
        }

        labelAntalKorrekte.setText(String.valueOf(korrekt));
        labelAntalNæstenKorrekte.setText(String.valueOf(næstenKorrekt));
        labelAntalDelvisKorrekte.setText(String.valueOf(delvisKorrekt));
        labelAntalIkkeKorrekte.setText(String.valueOf(ikkeKorrekt));
        labelIkkeSpillet.setText(String.valueOf(ikkeSpilledeFlashcards));
        labelAntalKort.setText(String.valueOf(træningssession.getAktuelleFlashcards().size()));
    }

    // Metode til irrelevant-flashcard knap
    @FXML
    void handleButtonIrrelevantFlashcard(MouseEvent event)
    {
        // Tjekker at der findes et flashcard der vises lige nu, hvis ja -> markeres det som irrelevant og fjernes
        if (aktueltFlashcard != null)
        {
            // Markerer det som irrelevant og fjerner det fra listen med flashcards i den aktuelle træning
            træningssession.fjernIrrelevantFlashcard(aktueltFlashcard);

            // Fortæller programmet at der ikke længere vises et flashcard
            aktueltFlashcard = null;
        }

        // Opdatere tællerne
        opdaterTræningsstatus();

        // Tjekker om brugeren har vundet, hvis tjekOmAlleSvarErKorrekte() returnere et boolean, true -> så har brugeren vundet
        // sikre at spillet afsluttes når der klikkes irrelevant på sidste flashcard
        if (træningssession.tjekOmAlleSvarErKorrekte())
        {
            duHarVundet(); // Kalder pop-up vinduet med vinder-beskeden
            return; // Metoden stoppes her
        }

        // Henter billedet til næste flashcard
        visFlashcard();
    }

    // Metode til start-forfra-knappen
    @FXML
    void handleButtonStartForfra(MouseEvent event)
    {
        // Hvis der findes en vente-timer, så stoppes den
        if (venteTimer != null)
        {
            venteTimer.stop();
        }

        // Kalder startForfra metoden
        træningssession.startForfra();

        // Fjerner svaret fra det tidligere flashcard
        svarFelt.setText("");
        erSvaretVist = false;

        // Skjuler 'vente'-teksten
        labelVenteTekst.setVisible(false);

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
        // Tjekker at der findes et flashcard der vises lige nu
        if (aktueltFlashcard != null)
        {
            // Skjuler 'Vente'-teksten når der vises et kort
            labelVenteTekst.setVisible(false);
            // Indsætter svaret på det flashcard der vises lige nu
            svarFelt.setText(aktueltFlashcard.getSvar());
            // Registerer at svaret nu er blevet vist
            erSvaretVist = true;
        }
        else // Hvis ikke der findes et flashcard der vises lige nu, så får brugeren besked
        {
            labelVenteTekst.setText("Ingen flashcards er klar til visning lige nu. \n" + "Vent lidt med at spille videre - næste kort vises automatisk.");
            labelVenteTekst.setVisible(true); // Viser 'Vente'-teksten
        }
    }

    // Metode til at vise brugeren at alle flashcards er færdigspillet og korrekte
    public void duHarVundet()
    {
        // Hvis der findes en vente-timer, så stoppes den
        if (venteTimer != null)
        {
            venteTimer.stop();
        }

        // Opretter en Start forfra knap
        ButtonType startForfra = new ButtonType("Start forfra");

        // Laver et pop-up vindue
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Godt klaret!");
        alert.setHeaderText("Tillykke!!!");
        alert.setContentText("Du har gennemført alle flashcards og svaret korrekt!");
        alert.getButtonTypes().setAll(startForfra);
        alert.showAndWait();

        // Når brugeren klikker på OK starter spillet forfra
        træningssession.startForfra();
        svarFelt.setText("");
        erSvaretVist = false;
        labelVenteTekst.setVisible(false);
        opdaterTræningsstatus();
        visFlashcard();
    }
}