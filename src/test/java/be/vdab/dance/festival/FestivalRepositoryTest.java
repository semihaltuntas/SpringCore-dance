package be.vdab.dance.festival;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(FestivalRepository.class)
@Sql("/festivals.sql")
public class FestivalRepositoryTest {
    private final FestivalRepository festivalRepository;
    private final JdbcClient jdbcClient;
    private static final String FESTIVALS_TABLE = "festivals";

    public FestivalRepositoryTest(FestivalRepository festivalRepository, JdbcClient jdbcClient) {
        this.festivalRepository = festivalRepository;
        this.jdbcClient = jdbcClient;
    }

    @Test
    void findAllGeeftAlleFestivalsGesorteerdOpNaam() {
        var aantalRecords = JdbcTestUtils.countRowsInTable(jdbcClient, FESTIVALS_TABLE);
        assertThat(festivalRepository.findAll())
                .hasSize(aantalRecords)
                .extracting(Festival::getNaam)
                .isSorted();
    }

    @Test
    void deleteVervijdertEenFestival() {
        var id = festivalRepository.idVanTestFestival1();
        festivalRepository.delete(id);
        var aantalRecordsMetDeIdVanDeVervijderdeFestival = JdbcTestUtils.countRowsInTableWhere(
                jdbcClient, FESTIVALS_TABLE, "id=" + id);
        assertThat(aantalRecordsMetDeIdVanDeVervijderdeFestival).isZero();
    }

    @Test
    void createVoegtEenFestivalToe() {
        var id = festivalRepository.create(new Festival(0, "Tomorrowland", 500, BigDecimal.TEN));
        assertThat(id).isPositive();
        var aantalRecordsMetDeIdVanDeToegevoegdeFest = JdbcTestUtils.countRowsInTableWhere(
                jdbcClient, FESTIVALS_TABLE, "id=" + id);
        assertThat(aantalRecordsMetDeIdVanDeToegevoegdeFest).isOne();
    }

    @Test
    void indUitverkochtGeeftDeUitverkochteFestivalsGesorteerdOpNaam() {
        var aantalRecords = JdbcTestUtils.countRowsInTableWhere(
                jdbcClient, FESTIVALS_TABLE, "ticketsBeschikbaar = 0");
        assertThat(festivalRepository.findUitVerkocht())
                .hasSize(aantalRecords)
                .extracting(Festival::getNaam)
                .isSortedAccordingTo(String::compareToIgnoreCase);
    }

    @Test
    void findAndLockByIdMetEenBestaandeIdVindtEenFestival() {
        assertThat(festivalRepository.findAndLockById(festivalRepository.idVanTestFestival1()))
                .hasValueSatisfying(festival ->
                        assertThat(festival.getNaam()).isEqualTo("test1"));
    }

    @Test
    void findAndLockByIdMetEenOnbestaandeIdVindtGeenFestival() {
        assertThat(festivalRepository.findAndLockById(Long.MAX_VALUE)).isEmpty();
    }

    @Test
    void findAantalVindtHetJuisteAantalFestivals() {
        var aantalRecords = JdbcTestUtils.countRowsInTable(jdbcClient, FESTIVALS_TABLE);
        assertThat(festivalRepository.findAantal()).isEqualTo(aantalRecords);
    }

    @Test
    void verhoogBudgetVerhoogtHetBudgetVanEenFestival() {
        festivalRepository.verhoogBudget(BigDecimal.TEN);
        var id = festivalRepository.idVanTestFestival1();
        var aantalRecords = JdbcTestUtils.countRowsInTableWhere(jdbcClient, FESTIVALS_TABLE,
                "reclameBudget = 110 and id =" + id);
        assertThat(aantalRecords).isOne();
    }

}
