package com.example.anki_flashcard_projekt;

import java.io.Serializable;
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

    // Holder styr på hvilket kort der vises lige nu
    private int nuværendeFlashcardDerVises;

    // Tæller brugerens vurdering af svar
    private int korrekt = 0;
    private int næstenKorrekt = 0;
    private int delvisKorrekt = 0;
    private int ikkeKorrekt = 0;

    // Spillede kort i denne runde bruges til at holde styr på, hvornår en
    // ny runde skal startes med forkert besvaret kort, eller hvornår
    // brugeren har vundet med 100% korrekte svar
    private int spilledeKortIDenneRunde = 0;

    // Konstruktør
    public Træningssession(List<Flashcard> flashcards)
    {
        this.aktuelleFlashcards = new ArrayList<>(flashcards);
        this.alleFlashcards = new ArrayList<>(flashcards);
        this.nuværendeFlashcardDerVises = 0;
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

    public int getSpilledeKortIDenneRunde()
    {
        return spilledeKortIDenneRunde; // Returnerer hvor mange kort der er spillet i den aktuelle runde
    }

    public void setSpilledeKortIDenneRunde(int spilledeKortIDenneRunde)
    {
        this.spilledeKortIDenneRunde = spilledeKortIDenneRunde; // Sætter hvor mange kort der er spillet i den aktuelle runde
    }

    public int getNuværendeFlashcardDerVises()
    {
        return nuværendeFlashcardDerVises; // Returnerer index på det flashcard der vises lige nu
    }

    public void setNuværendeFlashcardDerVises(int index)
    {
        this.nuværendeFlashcardDerVises = index; // Sætter index på det flashcard, der vises lige nu
    }

    public int getKorrekt()
    {
        return korrekt; // Returnerer antal korrekt besvarede flashcards i den aktuelle runde
    }

    public void setKorrekt(int korrekt)
    {
        this.korrekt = korrekt; // Sætter antal korrekt besvarede flashcards i den aktuelle runde
    }

    public int getNæstenKorrekt()
    {
        return næstenKorrekt; // Returnerer antal næsten korrekt besvarede flashcards i den aktuelle runde
    }

    public void setNæstenKorrekt(int næstenKorrekt)
    {
        this.næstenKorrekt = næstenKorrekt; // Sætter antal næsten korrekt besvarede flashcards i den aktuelle runde
    }

    public int getDelvisKorrekt()
    {
        return delvisKorrekt; // Returnerer antal delvis korrekt besvarede flashcards i den aktuelle runde
    }

    public void setDelvisKorrekt(int delvisKorrekt)
    {
        this.delvisKorrekt = delvisKorrekt; // Sætter antal delvis korrekt besvarede flashcards i den aktuelle runde
    }

    public int getIkkeKorrekt()
    {
        return ikkeKorrekt; // Returnerer antal ikke korrekt besvarede flashcards i den aktuelle runde
    }

    public void setIkkeKorrekt(int ikkeKorrekt)
    {
        this.ikkeKorrekt = ikkeKorrekt; // Sætter antal ikke korrekt besvarede flashcards i den aktuelle runde
    }

    // Metode til at starte træningssessionen forfra
    public void startForfra()
    {
        // Henter alle flashcards
        aktuelleFlashcards = new ArrayList<>(alleFlashcards);

        // Blander flashcards
        Collections.shuffle(aktuelleFlashcards);

        // Gennemgår alle flashcards og fjerner gammel korrekt-vurdering og irrelevant-markering,
        // så et nyt spil ikke arver gamle vurderinger
        for (Flashcard flashcard : alleFlashcards)
        {
            flashcard.setKorrektBesvaretFlashcard(false);
            flashcard.setIrrelevantFlashcard(false);
        }

        // Nulstiller tællerne
        nuværendeFlashcardDerVises = 0;
        korrekt = 0;
        næstenKorrekt = 0;
        delvisKorrekt = 0;
        ikkeKorrekt = 0;
        spilledeKortIDenneRunde = 0;
    }

    // Metode til at starte en ny spilrunde med flashcards brugeren har svaret forkert på
    public void startNyRundeMedIkkeKorrekteKort()
    {
        // Opretter en array liste til at holde på de kort som brugeren har svaret forkert på
        List<Flashcard> flashcardsNyRunde = new ArrayList<>();

        // Gennemgår alle flashcards og tilføjer forkert besvaret flashcards,
        // som ikke er markeret som irrelevante, til array listen
        for (Flashcard flashcard : alleFlashcards)
        {
            if (!flashcard.erFlashcardBesvaretKorrekt() && !flashcard.erFlashcardIrrelevant())
            {
                flashcardsNyRunde.add(flashcard);
            }
        }

        // næsten korrekt, delvis korrekte og ikke korrekte kort sættes ind som de flashcards der skal spilles videre med
        aktuelleFlashcards = flashcardsNyRunde;

        // Bland flashcards
        Collections.shuffle(aktuelleFlashcards);

        // Tællerne nulstilles
        nuværendeFlashcardDerVises = 0;
        næstenKorrekt = 0;
        delvisKorrekt = 0;
        ikkeKorrekt = 0;
        spilledeKortIDenneRunde = 0;
    }
}