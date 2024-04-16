package be.vdab.dance.festival;

import be.vdab.dance.exceptions.OnvoldoendeTicketsBeschikbaar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class FestivalTest {
    private Festival festival;

    @BeforeEach
    void beforeEach() {
        festival = new Festival(1, "Test", 100, BigDecimal.TEN);
    }

    @Test
    void boekWijzigtHetAantalBeschikbareTickets() {
        festival.boek(100);
        assertThat(festival.getTicketsBeschikbaar()).isZero();
    }

    @Test
    void nullTicketsBoekenMislukt() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> festival.boek(0));
    }

    @Test
    void eenNegatiefAantalTicketsBoekenMislukt() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> festival.boek(-20));
    }

    @Test
    void boekenMisluktBijOnvoldoendeBeschikbareTickets() {
        assertThatExceptionOfType(OnvoldoendeTicketsBeschikbaar.class).isThrownBy(
                () -> festival.boek(101));
    }
}
