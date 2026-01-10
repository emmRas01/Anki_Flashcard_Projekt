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

    // Konstruktør
    public Flashcard(String svar, String billedeSti)
    {
        this.svar = svar;
        this.billedeSti = billedeSti;
    }

    public String getSvar()
    {
        return svar;
    }

    public Image getBilledeSti()
    {
        return new Image(getClass().getResourceAsStream(billedeSti));
    }
}