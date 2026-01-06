package com.example.anki_flashcard_projekt;

import java.io.Serializable;

// Denne klasse skal kunne gemmes på en fil -> derfor skal Serializable implementeres
public class Flashcard implements Serializable
{
    private static final long serialVersionUID = 1L; // ID til serialisering skal være unikt for hver klasse

    // Definition af attributterne
    private String question;
    private String answer;
    private String imagePath;

    // Konstruktør
    public Flashcard(String question, String answer, String imagePath)
    {
        this.question = question;
        this.answer = answer;
        this.imagePath = imagePath;
    }

    public String getQuestion()
    {
        return question;
    }

    public String getAnswer()
    {
        return answer;
    }

    public String getImagePath()
    {
        return imagePath;
    }
}
