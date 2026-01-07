package com.example.anki_flashcard_projekt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;

// Klasse der importere flashcards fra en tab-separeret fil
public class AnkiImport
{
    // Metoden returnere en liste af Flashcard objekter, så vi kan gemme listen i Controlleren og bruge flashkortene
    public static ObservableList<Flashcard> importerAnkiFil(String filnavn) {
        ObservableList<Flashcard> flashcards = null;
        try {
            flashcards = FXCollections.observableArrayList();

            BufferedReader buf = new BufferedReader(new FileReader("GreatArtistFlashcards.txt"));
            String læstLinje;
            String[] felter; // Array til oplysningerne i en linje

            // Spring de første 6 linjer over i filen - dem skal vi ikke bruge
            for (int i = 0; i < 6; i++)
                læstLinje = buf.readLine();

            // Derefter kommer data: En linje per kunstværk
            boolean filslut = false;
            while (!filslut)
            {
                læstLinje = buf.readLine();
                if (læstLinje == null)
                {
                    filslut = true;
                } else {
                    // Læs den opdelte linje ind i arrayet felter
                    felter = læstLinje.split("\t");

                    // Henter titel, kunstner og årstal i anki-filen og gemmer dem som answer
                    String title = felter[0];
                    String kunstner = felter[1];
                    String årstal = felter[2];
                    String answer = title + "/n" + kunstner + "/n" + årstal;

                    // Henter billedets filnavn i anki-filen og gemmer fil stien
                    String billedFilNavn = felter[3];
                    String imagePath = "/com.example.anki_flashcard_projekt/images/" + billedFilNavn;

                    // Flashcard objekt oprettes med billede og svar, og tilføjes til vores ObservableList
                    flashcards.add(new Flashcard(answer, imagePath));
                }
            }
            buf.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return flashcards;
    }
}