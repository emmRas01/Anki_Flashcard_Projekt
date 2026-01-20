package com.example.anki_flashcard_projekt;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Denne klasse skal kunne gemmes på en fil -> derfor skal Serializable implementeres
public class Træningssession implements Serializable
{
    private static final long serialVersionUID = 2L; // ID til serialisering skal være unikt for hver klasse

    // Gemmer alle flashcards fra anki-filen, så de kan hentes hvis man starter spillet forfra
    private List<Flashcard> alleFlashcards;

    // Indeholder de flashcards der spilles med i den aktuelle spilrunde
    private List<Flashcard> aktuelleFlashcards;

    // Konstruktør
    public Træningssession(List<Flashcard> flashcards)
    {
        this.aktuelleFlashcards = new ArrayList<>(flashcards);
        this.alleFlashcards = new ArrayList<>(flashcards);
    }

    // Getter og setter metoder
    public List<Flashcard> getAktuelleFlashcards()
    {
        return aktuelleFlashcards; // Returnerer listen med de flashcards der spilles med i den aktuelle spilrunde
    }

    public List<Flashcard> getAlleFlashcards()
    {
        return alleFlashcards; // Returnerer listen over alle flashcards fra Anki-filen
    }

    // Metode til at sætte et flashcard som irrelevant og fjerne det
    public void fjernIrrelevantFlashcard(Flashcard flashcard)
    {
        // Tjekker at der findes et flashcard der vises lige nu, hvis ja -> markeres det som irrelevant
        if (flashcard != null)
        {
            // Markerer det flashcard der vises lige nu som irrelevant
            flashcard.setIrrelevantFlashcard(true);

            // Fjerner flashcardet fra listen med flashcards i den aktuelle træning
            aktuelleFlashcards.remove(flashcard);
        }
    }

    // Metode der finder hvilket flashcard der skal vises næste gang
    public Flashcard findDetNæsteFlashcardDerErKlarTilVisning()
    {
        // Henter den dato og det klokkeslæt det er lige nu
        LocalDateTime nu = LocalDateTime.now();

        // Laver en ny array-liste, så jeg kan blande de kort der er klar til visning -> så de kommer tilfældigt
        List<Flashcard> kortDerErKlarTilVisningBlandet = new ArrayList<>();

        // For-løkke der kører alle flashcards igennem
        for (Flashcard flashcard : alleFlashcards)
        {
            // Hvis et flashcard er relevant, besvaret forkert og ikke er låst af tid -> så tilføjes det til array-listen
            if (!flashcard.erFlashcardIrrelevant() && !flashcard.erFlashcardBesvaretKorrekt()
                    && !flashcard.getNæsteVisning().isAfter(nu))
            {
                kortDerErKlarTilVisningBlandet.add(flashcard);
            }
        }

        // Hvis der ikke findes noget kort, der er klar pga. tidslås, returneres null -> så får brugeren besked på at vente i visFlashcard()
        if (kortDerErKlarTilVisningBlandet.isEmpty())
        {
            return null;
        }

        // Blander alle flashcards i Arraylisten, så de kommer tilfældigt
        Collections.shuffle(kortDerErKlarTilVisningBlandet);

        // Returnere et tilfældigt flashcard der opfylder de 3 krav
        return kortDerErKlarTilVisningBlandet.get(0);
    }

    // Metode der tjekker om brugeren har vundet
    public boolean tjekOmAlleSvarErKorrekte()
    {
        // For-løkke der kører alle flashcards igennem
        for (Flashcard flashcard : alleFlashcards)
        {
            // Hvis et flashcard er relevant og besvaret forkert -> så har brugeren ikke svaret korrekt på alle kort
            if (!flashcard.erFlashcardIrrelevant() && !flashcard.erFlashcardBesvaretKorrekt())
            {
                return false; // Brugeren har ikke vundet
            }
        }
        // Hvis ingen flashcards er forkerte eller irrelevant -> brugeren har vundet
        return true;
    }

    // Metode til at starte træningssessionen forfra
    public void startForfra()
    {
        // Henter alle flashcards igen
        aktuelleFlashcards = new ArrayList<>(alleFlashcards);

        // Blander flashcards, så de ikke altid kommer i samme rækkefølge
        Collections.shuffle(aktuelleFlashcards);

        // Gennemgår alle flashcards og nulstiller gamle vurderinger, irrelevant-markering og tidslås,
        // så et nyt spil ikke arver disse
        for (Flashcard flashcard : alleFlashcards)
        {
            flashcard.setBesvaretFlashcard(false);
            flashcard.setKorrektBesvaretFlashcard(false);
            flashcard.setNæstenKorrektBesvaretFlashcard(false);
            flashcard.setDelvisKorrektBesvaretFlashcard(false);
            flashcard.setIkkeKorrektBesvaretFlashcard(false);
            flashcard.setIrrelevantFlashcard(false);
            flashcard.setNæsteVisning(LocalDateTime.now()); // Nulstiller tidslås
        }
    }
}