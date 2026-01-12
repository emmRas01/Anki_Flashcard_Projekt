package com.example.anki_flashcard_projekt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Denne klasse skal kunne gemmes på en fil -> derfor skal Serializable implementeres
public class Træningssession implements Serializable
{
    private static final long serialVersionUID = 2L; // ID til serialisering skal være unikt for hver klasse

    // Definition af listen der holder kortene
    private List<Flashcard> flashcards;

    // Gemmer alle kort, så de kan hentes når man starter forfra
    private List<Flashcard> alleFlashcards;

    // Holder styr på hvilket kort der vises lige nu
    private int nuværendeFlashcardDerVises;

    // Holder styr på brugerens vurdering af svar
    private int korrekt = 0;
    private int næstenKorrekt = 0;
    private int delvisKorrekt = 0;
    private int ikkeKorrekt = 0;

    // Konstruktør
    public Træningssession(List<Flashcard> flashcards)
    {
        this.flashcards = new ArrayList<>(flashcards);
        this.alleFlashcards = new ArrayList<>(flashcards);
        this.nuværendeFlashcardDerVises = 0;
    }

    // Getter og setter metoder
    public List<Flashcard> getFlashcards()
    {
        return flashcards;
    }

    public List<Flashcard> getAlleFlashcards()
    {
        return alleFlashcards;
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
        flashcards = new ArrayList<>(alleFlashcards);

        // Blander flashcards
        Collections.shuffle(flashcards);

        // Nulstiller tællerne
        nuværendeFlashcardDerVises = 0;
        korrekt = 0;
        næstenKorrekt = 0;
        delvisKorrekt = 0;
        ikkeKorrekt = 0;
    }
}
