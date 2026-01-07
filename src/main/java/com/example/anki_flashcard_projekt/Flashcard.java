package com.example.anki_flashcard_projekt;

import javafx.scene.image.Image;

import java.io.Serializable;

// Denne klasse skal kunne gemmes på en fil -> derfor skal Serializable implementeres
public class Flashcard implements Serializable
{
    private static final long serialVersionUID = 1L; // ID til serialisering skal være unikt for hver klasse

    // Definition af attributterne
    private String imagePath;   // Stien til billedet
    private Image image;        // Billedet der fungere som et spørgsmål
    private String answer;      // Titlen på kunstværket, kunstnerens navn, årstal

    // Konstruktør
    public Flashcard(String imagePath, String answer)
    {
        this.imagePath = imagePath;
        this.answer = answer;
        this.image = new Image(getClass().getResourceAsStream(imagePath)); // Henter billedet fra resources
    }

    public String getAnswer()
    {
        return answer;
    }

    public Image getImage()
    {
        return image;
    }
}
