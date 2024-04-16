package be.vdab.dance.boeking;

import be.vdab.dance.exceptions.FestivalNietGevondenException;
import be.vdab.dance.festival.Festival;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@JdbcTest
@Import(BoekingRepository.class)
@Sql({"/festivals.sql","/boekingen.sql"})
class BoekingRepositoryTest {
    private static final String BOEKINGEN_TABLE = "boekingen";
    private final BoekingRepository boekingRepository;
    private final JdbcClient jdbcClient;

    public BoekingRepositoryTest(BoekingRepository boekingRepository, JdbcClient jdbcClient) {
        this.boekingRepository = boekingRepository;
        this.jdbcClient = jdbcClient;
    }

    private long idVanTestFestival1() {
        var sql = """
                select id from festivals where naam = 'test1'
                """;
        return jdbcClient.sql(sql)
                .query(Long.class)
                .single();
    }

    @Test
    void createVoegtEenBoekingToe() {
        var festivalId = idVanTestFestival1();
        boekingRepository.create(new Boeking(0, "test", 2, festivalId));
        var aantalRecords = JdbcTestUtils.countRowsInTableWhere(jdbcClient, BOEKINGEN_TABLE,
                "naam = 'test' and aantalTickets = 2 and festivalId = " + festivalId);
        assertThat(aantalRecords).isOne();
    }

    @Test
    void findBoekingenMetFestivalsVindtDeJuisteData() {
        var boekingen = boekingRepository.findBoekingenMetFestival();
        assertThat(boekingen).hasSize(JdbcTestUtils.countRowsInTable(jdbcClient, BOEKINGEN_TABLE))
                .extracting(boeking -> boeking.id()).isSorted();
        var rijMetTest1Boeking = boekingen.stream()
                .filter(boeking -> boeking.boekingNaam().equals("test2"))
                .findFirst()
                .get();
        assertThat(rijMetTest1Boeking.aantalTickets()).isEqualTo(2);
        assertThat(rijMetTest1Boeking.festivalNaam()).isEqualTo("test2");

    }

}