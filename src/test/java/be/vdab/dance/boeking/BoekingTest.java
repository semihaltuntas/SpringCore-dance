package be.vdab.dance.boeking;

import be.vdab.dance.boeking.Boeking;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class BoekingTest {
    @Test
    void eenBoekingDieLukt() {
        new Boeking(0, "mie", 2, 1);
    }

    @Test
    void deNaamIsVerplicht() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Boeking(0, "", 2, 1));
    }

    @Test
    void nullTicketsBoekenMislukt() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Boeking(0, "semih", 0, 1));
    }

    @Test
    void eenNegatiefAantalTicketsBoekenMislukt() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> new Boeking(0, "semih", -1, 1));
    }
    @Test
    void deFestivalIdMoetPositiefZijn(){
        assertThatIllegalArgumentException().isThrownBy(
                ()->new Boeking(0,"semih",1,0));
    }
}
