package com.example.anki_flashcard_projekt;

import javafx.scene.image.Image;
import java.io.Serializable;

// Denne klasse skal kunne gemmes på en fil -> derfor skal Serializable implementeres
public class Flashcard implements Serializable
{
    private static final long serialVersionUID = 1L; // ID til serialisering skal være unikt for hver klasse

    // Definition af attributterne
    private String billedeSti;   // Stien til billedet
    private String svar;      // Svaret: Titlen på kunstværket, kunstnerens navn, årstal

    // boolean til at registerer at et kort er korrekt (true) eller ikke korrekt (false)
    private boolean korrektBesvaretFlashcard = false;

    // Konstruktør
    public Flashcard(String svar, String billedeSti)
    {
        this.svar = svar;
        this.billedeSti = billedeSti;
    }

    // Metode der returnere svaret til det enkelte Flashcard -> til at sætte ind i svar feltet
    public String getSvar()
    {
        return svar;
    }

    // Metode der returnere billedet til det enkelte Flashcard -> til at sætte ind i ImageView
    public Image getBillede()
    {
        return new Image(getClass().getResourceAsStream(billedeSti));
    }

    // Metode til at tjekke om flashcard er besvaret korrekt eller ikke (true/false)
    public boolean erFlashcardBesvaretKorrekt()
    {
        return korrektBesvaretFlashcard;
    }

    // Metode til at sætte et Flashcard til true/korrekt eller false/ikke korrekt
    public void setKorrektBesvaretFlashcard(boolean korrektBesvaret)
    {
        this.korrektBesvaretFlashcard = korrektBesvaret;
    }
}