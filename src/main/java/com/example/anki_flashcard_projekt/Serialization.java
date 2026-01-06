package com.example.anki_flashcard_projekt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class Serialization
{
    // Metode der gemmer en liste af Flashcard-objekter i filen Flashcard.txt
    public static void skrivFlashcardObjekterNedIFil(ObservableList<Flashcard> flashcards) throws IOException
    {
        // Opretter en binær fil kaldet flashcards.txt
        FileOutputStream fileOutputStream = new FileOutputStream("flashcards.txt");
        // Opretter en ObjectOutputStream, der kan gemme objekter ned på en fil
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        // I filen starter objectOutputStream.writeInt med at skrive hvor mange flashcards der findes i heltal
        // (gør det lettere at læse filen)
        objectOutputStream.writeInt(flashcards.size());

        // for-løkke der kører alle flashcards igennem
        for (Flashcard f : flashcards)
            objectOutputStream.writeObject(f); // Skriver et Flashcard i filen

        objectOutputStream.flush();
        objectOutputStream.close(); // Sikre at alle data er gemt ved at lukke strømmen
    }

    // Metode der indlæser gemte Flashcards fra filen flashcards.txt
    public static ObservableList<Flashcard> indlæsFlashcardObjekterFraFil() throws IOException, ClassNotFoundException
    {
        // Opretter en tom ObservableList, der senere skal indeholde alle de gemte Flashcard objekter fra filen
        ObservableList<Flashcard> liste = FXCollections.observableArrayList();
        // Åbner filen flashcards.txt så den kan læses
        FileInputStream fileInputStream = new FileInputStream("flashcards.txt");
        // Opretter ObjectInputStream der kan læse objekter i en fil
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        // Først læses heltallet der angiver hvor mange Flashcard objekter filen indeholder
        int antal = objectInputStream.readInt();
        // for-løkken kører alle Flashcard objekterne igennem
        for (int i = 0; i < antal; ++i)
        {
            Flashcard f = (Flashcard) objectInputStream.readObject(); // Læser objektet
            liste.add(f); // Tilføjer objektet til ObservableListen
        }

        objectInputStream.close(); // lukker strømmen for at ungå resource leaks
        return (ObservableList<Flashcard>) liste; // returnere listen, så listen opdateres
    }
}