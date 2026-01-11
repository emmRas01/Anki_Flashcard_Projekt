package com.example.anki_flashcard_projekt;

import java.io.*;

public class Serialization
{
    // Metode der gemmer et træningssession-objekt i filen Træningssession.dat
    public static void gemTræningssession(Træningssession træningssession) throws IOException
    {
        // Opretter en binær fil kaldet Træningssession.dat
        FileOutputStream fileOutputStream = new FileOutputStream("Træningssession.dat");

        // Opretter en ObjectOutputStream, der kan gemme objekter ned på filen
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        // Skriver træningssession-objektet ned i filen
        objectOutputStream.writeObject(træningssession);

        // Sikre at alle data er gemt ved at lukke strømmen
        objectOutputStream.close();
    }

    // Metode der indlæser et træningssession-objekt fra filen Træningssession.dat
    public static Træningssession indlæsTræningssession() throws IOException, ClassNotFoundException
    {
        // Åbner filen Træningssession.dat, så den kan indlæses
        FileInputStream fileInputStream = new FileInputStream("Træningssession.dat");

        // Opretter ObjectInputStream der kan læse objekter i en fil
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        // Indlæser træningssession-objekt fra filen
        Træningssession træningssession = (Træningssession) objectInputStream.readObject();
        // typecast (Træningssession) fortæller Java at det er et Træningssession-objekt

        // Lukker strømmen
        objectInputStream.close();

        // Returnere det indlæste træningssession-objekt
        return træningssession;
    }
}