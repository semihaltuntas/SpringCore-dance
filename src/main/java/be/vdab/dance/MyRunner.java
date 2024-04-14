package be.vdab.dance;

import be.vdab.dance.festival.Festival;
import be.vdab.dance.festival.FestivalNietGevondenException;
import be.vdab.dance.festival.FestivalService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class MyRunner implements CommandLineRunner {
    private final FestivalService festivalService;

    public MyRunner(FestivalService festivalService) {
        this.festivalService = festivalService;
    }

    @Override
    public void run(String... args) {
//        festivalService.findUitVerkocht().forEach(
//                festival -> System.out.println("uitverkocht festival: "+festival.getNaam())
//        );

//festivalService.create(new Festival(0,"Tomorrowland",3000, BigDecimal.valueOf(2000)));
//        System.out.println("New festival added!");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Festival id: ");
        long id = scanner.nextLong();
        try {
            festivalService.annuleer(id);
            System.out.println("Festival geannuleerd.");
        } catch (FestivalNietGevondenException ex) {
            System.err.println("Festival " + ex.getId() + " niet gevonden");
        }
    }
}
