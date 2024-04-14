package be.vdab.dance.festival;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.processing.Generated;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class FestivalRepository {
    private final JdbcClient jdbcClient;

    public FestivalRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Festival> findAll() {
        String sql = """
                select id,naam,ticketsBeschikbaar,reclameBudget
                from festivals
                order by naam
                """;
        return jdbcClient.sql(sql)
                .query(Festival.class)
                .list();
    }

    public List<Festival> findUitVerkocht() {
        String sql = """
                select id,naam,ticketsBeschikbaar,reclameBudget
                from festivals
                where ticketsBeschikbaar = 0
                order by naam
                """;
        return jdbcClient.sql(sql)
                .query(Festival.class)
                .list();
    }

    public void delete(long id) {
        var sql = """
                delete from festivals
                where id = ?
                """;
        jdbcClient.sql(sql)
                .param(id)
                .update();
    }

    public long create(Festival festival) {
        var sql = """
                insert into festivals(naam,ticketsBeschikbaar,reclameBudget)
                values(?,?,?)
                """;
        var keyholder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .params(festival.getNaam(), festival.getTicketsBeschikbaar(), festival.getReclameBudget())
                .update(keyholder);
        return keyholder.getKey().longValue();
    }

    public long idVanTestFestival1() {
        return jdbcClient.sql("select id from festivals where naam = 'test1'")
                .query(Long.class)
                .single();
    }

    public Optional<Festival> findAndLockById(Long id) {
        String sql = """
                select id,naam,ticketsBeschikbaar,reclameBudget
                from festivals
                where id = ?
                for update
                      """;
        return jdbcClient.sql(sql)
                .param(id)
                .query(Festival.class)
                .optional();
    }

    public long findAantal() {
        String sql = """
                select count(*) as aantalFestivals
                from festivals
                """;
        return jdbcClient.sql(sql)
                .query(Long.class)
                .single();
    }

    public void verhoogBudget(BigDecimal bedrag) {
        String sql = """
                update festivals
                set reclameBudget = reclameBudget + ?
                """;
        jdbcClient.sql(sql)
                .param(bedrag)
                .update();
    }
}
