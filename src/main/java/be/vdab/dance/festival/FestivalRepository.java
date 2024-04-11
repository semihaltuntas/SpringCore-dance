package be.vdab.dance.festival;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.processing.Generated;
import java.util.List;
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
}
