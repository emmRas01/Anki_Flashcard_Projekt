package com.example.anki_flashcard_projekt;

import javafx.scene.image.Image;
import java.io.Serializable;
import java.time.LocalDateTime;

// Denne klasse skal kunne gemmes på en fil -> derfor skal Serializable implementeres
public class Flashcard implements Serializable
{
    private static final long serialVersionUID = 1L; // ID til serialisering skal være unikt for hver klasse

    // Definition af attributterne
    private String billedeSti;            // Stien til billedet
    private String svar;                  // Svaret: Titlen på kunstværket, kunstnerens navn, årstal
    private LocalDateTime næsteVisning;   // Holder styr på hvornår Flashcardet må vises igen efter forkert besvarrelse

    // boolean til at registerer at et kort er vurderet som korrekt (true) eller ikke (false)
    private boolean korrektBesvaretFlashcard = false;

    // boolean til at registerer at et kort er vurderet som næsten korrekt (true) eller ikke (false)
    private boolean næstenKorrektBesvaretFlashcard = false;

    // boolean til at registerer at et kort er vurderet som delvis korrekt (true) eller ikke (false)
    private boolean delvisKorrektBesvaretFlashcard = false;

    // boolean til at registerer at et kort er vurderet som ikke korrekt (true) eller ikke (false)
    private boolean ikkeKorrektBesvaretFlashcard = false;

    // boolean til at registerer om et kort er irrelevant (true) eller ikke (false)
    private boolean erFlashcardIrrelevant = false;

    // boolean til at registerer om et kort er besvaret/vurderet (true) eller ikke (false)
    private boolean besvaret = false;

    // Konstruktør
    public Flashcard(String svar, String billedeSti)
    {
        this.svar = svar;
        this.billedeSti = billedeSti;
        // Første gang programmet kører er flashcardet altid klar til at blive vist med det samme
        this.næsteVisning = LocalDateTime.now();
    }

    // Metode der returnere hvornår flashcardet skal vises næste gang
    public LocalDateTime getNæsteVisning()
    {
        return næsteVisning;
    }

    // Metode der sætter tidspunktet for hvornår flashcardet skal vises næste gang
    public void setNæsteVisning(LocalDateTime næsteVisning)
    {
        this.næsteVisning = næsteVisning;
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

    // Metode til at tjekke om flashcard er besvaret korrekt (true) eller ikke (false)
    public boolean erFlashcardBesvaretKorrekt()
    {
        return korrektBesvaretFlashcard;
    }

    // Metode til at sætte et flashcard til korrekt (true) eller ikke korrekt (false)
    public void setKorrektBesvaretFlashcard(boolean korrektBesvaret)
    {
        this.korrektBesvaretFlashcard = korrektBesvaret;
    }

    // Metode til at tjekke om flashcard er besvaret næsten korrekt (true) eller ikke (false)
    public boolean erFlashcardBesvaretNæstenKorrekt()
    {
        return næstenKorrektBesvaretFlashcard;
    }

    // Metode til at sætte et flashcard til næsten korrekt (true) eller ikke korrekt (false)
    public void setNæstenKorrektBesvaretFlashcard(boolean næstenKorrektBesvaret)
    {
        this.næstenKorrektBesvaretFlashcard = næstenKorrektBesvaret;
    }

    // Metode til at tjekke om flashcard er besvaret delvis korrekt (true) eller ikke (false)
    public boolean erFlashcardBesvaretDelvisKorrekt()
    {
        return delvisKorrektBesvaretFlashcard;
    }

    // Metode til at sætte et flashcard til delvis korrekt (true) eller ikke korrekt (false)
    public void setDelvisKorrektBesvaretFlashcard(boolean delvisKorrektBesvaretFlashcard)
    {
        this.delvisKorrektBesvaretFlashcard = delvisKorrektBesvaretFlashcard;
    }

    // Metode til at tjekke om flashcard er besvaret ikke korrekt (true) eller ikke (false)
    public boolean erFlashcardBesvaretIkkeKorrekt()
    {
        return ikkeKorrektBesvaretFlashcard;
    }

    // Metode til at sætte et flashcard til ikke korrekt (true) eller ikke (false)
    public void setIkkeKorrektBesvaretFlashcard(boolean ikkeKorrektBesvaretFlashcard)
    {
        this.ikkeKorrektBesvaretFlashcard = ikkeKorrektBesvaretFlashcard;
    }

    // Metode til at tjekke om et flashcard er markeret som irrelevant (true) eller ikke (false)
    public boolean erFlashcardIrrelevant()
    {
        return erFlashcardIrrelevant;
    }

    // Metode til at sætte et flashcard til irrelevant (true) eller ikke (false)
    public void setIrrelevantFlashcard(boolean irrelevantFlashcard)
    {
        this.erFlashcardIrrelevant = irrelevantFlashcard;
    }

    // Metode til at tjekke om et flashcard er besvaret/vurderet (true) eller ikke (false)
    public boolean erFlashcardBesvaret()
    {
        return besvaret;
    }

    // Metode til at sætte et flashcard til besvaret/vurderet (true) eller ikke (false)
    public void setBesvaretFlashcard(boolean besvaret)
    {
        this.besvaret = besvaret;
    }
}