package be.vdab.dance.boeking;

import be.vdab.dance.exceptions.BoekingNietGevondenException;
import be.vdab.dance.exceptions.FestivalNietGevondenException;
import be.vdab.dance.exceptions.OnvoldoendeTicketsBeschikbaar;
import be.vdab.dance.festival.FestivalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import({BoekingService.class, BoekingRepository.class, FestivalRepository.class})
@Sql({"/festivals.sql", "/boekingen.sql"})
public class BoekingServiceIntegrationTest {
    private static final String BOEKINGEN_TABLE = "boekingen";
    private static final String FESTIVALS_TABLE = "festivals";
    private final BoekingService boekingService;
    private final JdbcClient jdbcClient;

    public BoekingServiceIntegrationTest(BoekingService boekingService, JdbcClient jdbcClient) {
        this.boekingService = boekingService;
        this.jdbcClient = jdbcClient;
    }

    private long idVanTestFestival1() {
        return jdbcClient.sql("select id from festivals where naam= 'test1'")
                .query(Long.class)
                .single();
    }

    @Test
    void createVoegtEenBoekingToeEnWijzigtAantalBeschikbareTickets() {
        var festivalId = idVanTestFestival1();
        boekingService.create(new Boeking(0, "test", 2, festivalId));
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, BOEKINGEN_TABLE,
                "aantalTickets = 2 and festivalId = " + festivalId)).isOne();
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, FESTIVALS_TABLE,
                "ticketsBeschikbaar = 8 and id =" + festivalId)).isOne();
    }

    @Test
    void boekingMetTeVeelTicketMislukt() {
        var festivalId = idVanTestFestival1();
        assertThatExceptionOfType(OnvoldoendeTicketsBeschikbaar.class).isThrownBy(
                () -> boekingService.create(
                        new Boeking(0, "test", 11, festivalId)));
    }

    @Test
    void boekingMetOnbestaandFestivalMislukt() {
        assertThatExceptionOfType(FestivalNietGevondenException.class).isThrownBy(
                () -> boekingService.create(
                        new Boeking(0, "test", 9, Long.MAX_VALUE)));
    }


    private long idVanTestBoeking1() {
        return jdbcClient.sql("select id from boekingen where naam = 'test1'")
                .query(Long.class)
                .single();
    }

    @Test
    void annuleerVerwijdertDeBoekingEnWijzigtDeBeschikbareTicketsInHetFestival() {
        var boekingId = idVanTestBoeking1();
        var festivalId = idVanTestFestival1();
        boekingService.annuleer(boekingId);
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, BOEKINGEN_TABLE,
                "id = " + boekingId)).isZero();
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, FESTIVALS_TABLE,
                "ticketsBeschikbaar = 11 and id =  " + festivalId))
                .isOne();
    }
    @Test
    void annuleerMetOnbestaandeBoekingIdMislukt(){
        assertThatExceptionOfType(BoekingNietGevondenException.class).isThrownBy(
                ()->boekingService.annuleer(Long.MAX_VALUE)
        );
    }
}
