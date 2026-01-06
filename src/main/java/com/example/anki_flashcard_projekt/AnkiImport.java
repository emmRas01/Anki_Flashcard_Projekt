package com.example.anki_flashcard_projekt;

import java.io.BufferedReader;
import java.io.FileReader;

public class AnkiImport
{
    public void Importer()
    {
        try {
            BufferedReader buf = new BufferedReader(new FileReader("GreatArtistFlashcards.txt"));
            String læstLinje = null;
            String[] felter; // Array til oplysningerne i en linje

            // Spring de første 6 linjer over i filen - dem skal vi ikke bruge
            for (int i=0;i<6;i++)
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
                    // Nu indeholder arrayet felter[] informationen
                    // fra den læste linje. Vælg de felter du skal bruge og
                    // rens dem.
                }
            }
            buf.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}