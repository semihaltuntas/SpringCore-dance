package be.vdab.dance.boeking;

import be.vdab.dance.exceptions.BoekingNietGevondenException;
import be.vdab.dance.exceptions.OnvoldoendeTicketsBeschikbaar;
import be.vdab.dance.festival.Festival;
import be.vdab.dance.festival.FestivalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoekingServiceTest {
    private BoekingService boekingService;
    @Mock
    private BoekingRepository boekingRepository;
    @Mock
    private FestivalRepository festivalRepository;

    @BeforeEach
    void beforeEach() {
        boekingService = new BoekingService(boekingRepository, festivalRepository);
    }

    @Test
    void createVoegtEenBoekingToeEnWijzigtAantalBeschikbareTickets() {
        var festival = new Festival(1, "ballroom", 100, BigDecimal.TEN);
        when(festivalRepository.findAndLockById(1L)).thenReturn(Optional.of(festival));
        var boeking = new Boeking(0, "mie", 2, 1);
        boekingService.create(boeking);
        assertThat(festival.getTicketsBeschikbaar()).isEqualTo(98);
    }

    @Test
    void boekingMetTeveelTicketsMislukt() {
        var festival = new Festival(1, "tomorrowland", 3, BigDecimal.TEN);
        when(festivalRepository.findAndLockById(1L)).thenReturn(Optional.of(festival));
        assertThatExceptionOfType(OnvoldoendeTicketsBeschikbaar.class).isThrownBy(
                () -> boekingService.create(new Boeking(0, "semih", 4, 1))
        );
    }
    @Test
    void annuleerVerwijdertDeBoekingEnWijzigtHetFestival(){
        var boeking = new Boeking(1,"mie",3,1);
        var festival = new Festival(1,"ballroom",2,BigDecimal.TEN);
        when(boekingRepository.findAndLockById(1)).thenReturn(Optional.of(boeking));
        when(festivalRepository.findAndLockById(1L)).thenReturn(Optional.of(festival));
        boekingService.annuleer(1);
        assertThat(festival.getTicketsBeschikbaar()).isEqualTo(5);
    }
    @Test
    void annuleerMetOnbestaandeBoekingIdMislukt(){
        assertThatExceptionOfType(BoekingNietGevondenException.class).isThrownBy(
                ()->boekingService.annuleer(1)
        );
    }
}