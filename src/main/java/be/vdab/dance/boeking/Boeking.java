package be.vdab.dance.boeking;

public class Boeking {
    private final long id;
    private final String naam;
    private final int aantalTickets;
    private final long festivalId;

    public Boeking(long id, String naam, int aantalTickets, long festivalId) {
        if (naam.isEmpty()){
            throw new IllegalArgumentException("Naam moet ingevuld zijn.");
        }
        if (aantalTickets <= 0){
            throw new IllegalArgumentException("AantalTickets moet positief zijn");
        }
        if (festivalId <= 0){
            throw new IllegalArgumentException("id moet positief zijn");
        }
        this.id = id;
        this.naam = naam;
        this.aantalTickets = aantalTickets;
        this.festivalId = festivalId;
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public int getAantalTickets() {
        return aantalTickets;
    }

    public long getFestivalId() {
        return festivalId;
    }
}
