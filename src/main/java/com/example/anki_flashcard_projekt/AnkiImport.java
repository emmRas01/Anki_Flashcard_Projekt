package com.example.anki_flashcard_projekt;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

// Klasse der importere flashcards fra en tab-separeret anki-fil
public class AnkiImport
{
    // Metoden returnere en array liste af Flashcard objekter, så vi kan bruge listen i Controlleren
    public static List<Flashcard> importerAnkiFil()
    {
        // Opretter en tom liste til objekterne der skal importeres fra filen
        List<Flashcard> flashcards = new ArrayList<>();

        try {
            // Henter filen i ressource mappen
            InputStream is = AnkiImport.class.getResourceAsStream("/com/example/anki_flashcard_projekt/GreatArtistFlashcards.txt");

            // Hvis filen ikke findes, så får brugeren besked og metoden stoppes ved at der returneres en tom liste
            if (is == null)
            {
                System.out.println("Fejl: Kunne ikke finde GreatArtistFlashcards.txt");
                return flashcards;
            }

            // Læser filen linje for linje
            BufferedReader buf = new BufferedReader(new InputStreamReader(is)); // InputStreamReader konvertere bytes til tekst
            String læstLinje; // Indeholder 1 linje tekst af gangen
            String[] felter; // Array til at indeholde tekst-elementerne i kolonner

            // Spring de første 6 linjer over i filen
            for (int i = 0; i < 6; i++)
                læstLinje = buf.readLine(); // Linjerne læses, men bruges ikke aka. de gemmes ikke i arrayet

            // while-løkke der kører så længe der stadig er en linje at læse
            while ((læstLinje = buf.readLine()) != null)
            {
                // Opdeler linjen i kolonner vha. tab-tegnet
                felter = læstLinje.split("\t");

                // Spring linjer over, der ikke har nok felter
                if (felter.length < 9) continue;

                // Renser billedets filnavn
                String billedFilNavn = felter[3].replace("&nbsp;", "")
                        .replace("<img", "").replace("src=", "")
                        .replace("\"", "").replace(">", "")
                        .replace("/", "").trim();

                // Renser kunstner navnet
                String kunstner = felter[4].replace("&nbsp;", "") // Fjerner &nbsp;
                        .replaceAll("<[^>]+>", "") // Fjerner HTML tags <div> <span>
                        .replace("\"", "").trim(); // Fjerner ekstra "

                // Renser titlen på kunstværket
                String title = felter[5].replace("&nbsp;", "") // Fjerner &nbsp;
                        .replaceAll("<[^>]+>", "") // Fjerner HTML tags <div> <i>
                        .replaceAll("<!--.*?-->", "") //Fjerner HTML kommentar
                        .replace("\"", "").trim(); // Fjerner ekstra "

                // Renser årstallet på kunstværket
                String årstal = felter[7].replace("&nbsp;", "") // Fjerner &nbsp;
                        .replaceAll("<[^>]+>", "") // Fjerner HTML tags <span>
                        .replaceAll("<!--.*?-->", "") //Fjerner HTML kommentar
                        .replace("\"", "").trim(); // Fjerner ekstra "

                // Renser tidsalderen på kunstværket
                String tidsalder = felter[8].replace("&nbsp;", "")
                        .replaceAll("<[^>]+>", "") // Fjerner HTML tags <span>
                        .replace("\"", "").trim(); // Fjerner ekstra "

                // Laver svaret
                String svar = kunstner + "\n" + title + "\n" + årstal + "\n" + tidsalder;

                // Laver stien til billedet
                String billedeSti = "/com/example/anki_flashcard_projekt/images/" + billedFilNavn;

                // Opret flashcard og tilføj det til array listen
                flashcards.add(new Flashcard(svar, billedeSti));
            }
            buf.close(); // Når filen er færdig-læst lukkes forbindelsen til filen
        }
        catch (Exception e) // Fanger fejl
        {
            e.printStackTrace(); // Udskriver fejlen
        }
        return flashcards; // Returnere en array liste af Flashcard objekter, så vi kan bruge listen i Controlleren
    }
}