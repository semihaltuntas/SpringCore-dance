package be.vdab.dance.exceptions;

public class BoekingNietGevondenException extends RuntimeException{
    private final long id;

    public BoekingNietGevondenException(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
