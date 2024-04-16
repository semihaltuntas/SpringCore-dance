package be.vdab.dance.boeking;

import be.vdab.dance.exceptions.FestivalNietGevondenException;
import be.vdab.dance.festival.FestivalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BoekingService {
    private final BoekingRepository boekingRepository;
    private final FestivalRepository festivalRepository;

    public BoekingService(BoekingRepository boekingRepository, FestivalRepository festivalRepository) {
        this.boekingRepository = boekingRepository;
        this.festivalRepository = festivalRepository;
    }
    @Transactional
    public void create(Boeking boeking){
        var festival = festivalRepository.findAndLockById(boeking.getFestivalId())
                .orElseThrow(()-> new FestivalNietGevondenException(boeking.getFestivalId()));
        festival.boek(boeking.getAantalTickets());
        boekingRepository.create(boeking);
        festivalRepository.update(festival);
    }
    public List<BoekingMetFestival> findBoekingenFestivals(){
        return boekingRepository.findBoekingenMetFestival();
    }
}
