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
        return aktuelleFlashcards;
    }

    public List<Flashcard> getAlleFlashcards()
    {
        return alleFlashcards;
    }

    public int getSpilledeKortIDenneRunde()
    {
        return spilledeKortIDenneRunde;
    }

    public void setSpilledeKortIDenneRunde(int spilledeKortIDenneRunde)
    {
        this.spilledeKortIDenneRunde = spilledeKortIDenneRunde;
    }

    public int getNuværendeFlashcardDerVises()
    {
        return nuværendeFlashcardDerVises;
    }

    public void setNuværendeFlashcardDerVises(int index)
    {
        this.nuværendeFlashcardDerVises = index;
    }

    public int getKorrekt()
    {
        return korrekt;
    }

    public void setKorrekt(int korrekt)
    {
        this.korrekt = korrekt;
    }

    public int getNæstenKorrekt()
    {
        return næstenKorrekt;
    }

    public void setNæstenKorrekt(int næstenKorrekt)
    {
        this.næstenKorrekt = næstenKorrekt;
    }

    public int getDelvisKorrekt()
    {
        return delvisKorrekt;
    }

    public void setDelvisKorrekt(int delvisKorrekt)
    {
        this.delvisKorrekt = delvisKorrekt;
    }
    public int getIkkeKorrekt()
    {
        return ikkeKorrekt;
    }

    public void setIkkeKorrekt(int ikkeKorrekt)
    {
        this.ikkeKorrekt = ikkeKorrekt;
    }

    // Metode til at starte træningssessionen forfra
    public void startForfra()
    {
        // Henter alle kortene
        aktuelleFlashcards = new ArrayList<>(alleFlashcards);

        // Blander flashcards
        Collections.shuffle(aktuelleFlashcards);

        // Nulstiller tællerne
        nuværendeFlashcardDerVises = 0;
        korrekt = 0;
        næstenKorrekt = 0;
        delvisKorrekt = 0;
        ikkeKorrekt = 0;
        spilledeKortIDenneRunde = 0;

        // Gennemgår alle flashcards og fjerner gammel korrekt vurdering på dem,
        // så et nyt spil ikke arver gamle korrekt svar
        for (Flashcard flashcard : alleFlashcards)
        {
            flashcard.setKorrektBesvaretFlashcard(false);
        }
    }

    // Metode til at starte en ny spilrunde med flashcards brugeren har svaret forkert på
    public void startNyRundeMedIkkeKorrekteKort()
    {
        // Opretter en arrayliste til at holde på de kort som brugeren har svaret forkert på
        List<Flashcard> flashcardsNyRunde = new ArrayList<>();

        // Gennemgår alle flashcards og adder forkert besvaret flashcards til arraylisten
        for (Flashcard flashcard : alleFlashcards)
        {
            if (!flashcard.erFlashcardBesvaretKorrekt())
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