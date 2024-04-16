package be.vdab.dance;

import be.vdab.dance.boeking.Boeking;
import be.vdab.dance.boeking.BoekingService;
import be.vdab.dance.exceptions.BoekingNietGevondenException;
import be.vdab.dance.exceptions.FestivalNietGevondenException;
import be.vdab.dance.exceptions.OnvoldoendeTicketsBeschikbaar;
import be.vdab.dance.festival.FestivalService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MyRunner implements CommandLineRunner {
    private final FestivalService festivalService;
    private final BoekingService boekingService;

    public MyRunner(FestivalService festivalService, BoekingService boekingService) {
        this.festivalService = festivalService;
        this.boekingService = boekingService;
    }

    @Override
    public void run(String... args) {
        // 1.code
//        festivalService.findUitVerkocht().forEach(
//                festival -> System.out.println("uitverkocht festival: "+festival.getNaam())
//        );

        // code create
//festivalService.create(new Festival(0,"Tomorrowland",3000, BigDecimal.valueOf(2000)));
//        System.out.println("New festival added!");


        // 2. Code = for annuleer
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Festival id: ");
//        long id = scanner.nextLong();
//        try {
//            festivalService.annuleer(id);
//            System.out.println("Festival geannuleerd.");
//        } catch (FestivalNietGevondenException ex) {
//            System.err.println("Festival " + ex.getId() + " niet gevonden");
//        }

        //Last code:
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Naam:");
//        var naam = scanner.nextLine();
//        System.out.println("Aantal Tickets:");
//        var aantalTickets = scanner.nextInt();
//        System.out.println("Festival ID:");
//        var festivalId = scanner.nextInt();
//        try {
//            var boeking = new Boeking(0, naam, aantalTickets, festivalId);
//            boekingService.create(boeking);
//            System.out.println("Boeking Ok!");
//        } catch (IllegalArgumentException ex) {
//            System.err.println(ex.getMessage());
//        } catch (FestivalNietGevondenException ex) {
//            System.err.println("Festival " + ex.getId() + " niet gevonden.");
//        } catch (OnvoldoendeTicketsBeschikbaar ex) {
//            System.err.println("Onvoldoende tickets beschikbaar");
//        }

        //4.code
//        boekingService.findBoekingenFestivals().forEach(System.out::println);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Boeking İd :");
        var id = scanner.nextLong();
        try {
            boekingService.annuleer(id);
            System.out.println("boeking id= " + id + " geannuleerd!");
        } catch (BoekingNietGevondenException ex) {
            System.err.println("Boeking niet gevonden");
        }

    }
}
