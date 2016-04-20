/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.controller.ranking;

import de.appsolve.padelcampus.comparators.MapValueComparator;
import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.GameDAOI;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.db.model.GameSet;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/ranking")
public class RankingController extends BaseController {
    
    private static final Logger LOG = Logger.getLogger(RankingController.class);
    
    //https://metinmediamath.wordpress.com/2013/11/27/how-to-calculate-the-elo-rating-including-example/
    
    private static final int ELO_MAX_DAYS = 365;
    
    private static final BigDecimal ELO_K_FACTOR = new BigDecimal("32");
    
    private static final BigDecimal ELO_MAGIC_NUMBER = new BigDecimal("400");
    
    private static final BigDecimal ELO_INITIAL_RATING = new BigDecimal("1300");
    
    @Autowired
    GameDAOI gameDAO;
    
    private SortedMap<Participant, BigDecimal> rankingMap;
    
    @RequestMapping
    public ModelAndView getIndex(){
        return new ModelAndView("ranking/index");
    }
    
    @RequestMapping("{category}")
    public ModelAndView getIndex(@PathVariable("category") String category) throws Exception{
        rankingMap = new TreeMap<>();
        ModelAndView mav = new ModelAndView("ranking/ranking");
        List<Game> games = gameDAO.findAllWithPlayers();
        LocalDate today = LocalDate.now();
        for (Game game: games){
            LocalDate endDate = game.getEvent().getEndDate();
            int days = Days.daysBetween(today, endDate).getDays();
            if (days > ELO_MAX_DAYS){
                LOG.warn("Skipping game "+game+ " as it is older than "+ELO_MAX_DAYS+" days");
                continue;
            }
            
            Set<Participant> participants = game.getParticipants();
            if (participants.size() != 2){
                LOG.warn("Skipping game "+game+" as it does not have 2 participants");
                continue;
            }
            if (game.getGameSets().isEmpty()){
                //LOG.debug("Skipping game "+game+" as no game sets have been played");
                continue;
            }
            
            Iterator<Participant> iterator = participants.iterator();
            Participant p1 = iterator.next();
            Participant p2 = iterator.next();
            
            switch (category){
                case "male":
                case "female":
                    mav.addObject("urlPath", "players/player");
                    if (p1 instanceof Player && p2 instanceof Player){
                        updateRanking(game, p1, p2);
                    } else if (p1 instanceof Team && p2 instanceof Team){
                        Team t1 = (Team) p1;
                        Team t2 = (Team) p2;
                        Set<Player> players = new HashSet<>();
                        players.addAll(t1.getPlayers());
                        players.addAll(t2.getPlayers());
                        boolean genderMatches = true;
                        for (Player player: players){
                            if (!player.getGender().equals(Gender.valueOf(category))){
                                genderMatches = false;
                                break;
                            }
                        }
                        if (genderMatches){
                            updateRanking(game, p1, p2, t1.getPlayers(), t2.getPlayers());
                        }
                    }
                case "teams":
                    mav.addObject("urlPath", "teams/team");
                    if (p1 instanceof Team && p2 instanceof Team){
                        updateRanking(game, p1, p2);
                    }
                    break;
                default:
                    throw new Exception("Unsupported category");
            }
        }
        Iterator<Entry<Participant, BigDecimal>> iterator = rankingMap.entrySet().iterator();
        while (iterator.hasNext()){
            Entry<Participant, BigDecimal> entry = iterator.next();
            BigDecimal value = entry.getValue();
            value = value.setScale(2, RoundingMode.HALF_UP);
            entry.setValue(value);
        }
        
        SortedMap<Participant, BigDecimal> sortedMap = new TreeMap<>(new MapValueComparator<>(rankingMap));
        sortedMap.putAll(rankingMap);
        mav.addObject("Rankings", sortedMap);
        return mav;
    }

    private void updateRanking(Game game, Participant p1, Participant p2, Set<Player> players1, Set<Player> players2) {
        BigDecimal r1 = getTeamRanking(players1);
        BigDecimal r2 = getTeamRanking(players2);

        BigDecimal tr1 = getTransformedRating(r1);
        BigDecimal tr2 = getTransformedRating(r2);

        BigDecimal e1 = getExpectedScore(tr1, tr2);
        BigDecimal e2 = getExpectedScore(tr2, tr1);

        BigDecimal s1 = getScore(game, p1);
        BigDecimal s2 = getScore(game, p2);

        BigDecimal newR1 = ELO_K_FACTOR.multiply((s1.subtract(e1))).add(r1);
        BigDecimal newR2 = ELO_K_FACTOR.multiply((s2.subtract(e2))).add(r2);

        updateTeamRanking(players1, newR1);
        updateTeamRanking(players2, newR2);
    }
    
    private void updateRanking(Game game, Participant p1, Participant p2) {
        BigDecimal r1 = getRanking(p1);
        BigDecimal r2 = getRanking(p2);

        BigDecimal tr1 = getTransformedRating(r1);
        BigDecimal tr2 = getTransformedRating(r2);

        BigDecimal e1 = getExpectedScore(tr1, tr2);
        BigDecimal e2 = getExpectedScore(tr2, tr1);

        BigDecimal s1 = getScore(game, p1);
        BigDecimal s2 = getScore(game, p2);

        BigDecimal newR1 = ELO_K_FACTOR.multiply((s1.subtract(e1))).add(r1);
        BigDecimal newR2 = ELO_K_FACTOR.multiply((s2.subtract(e2))).add(r2);

        rankingMap.put(p1, newR1);
        rankingMap.put(p2, newR2);
    }
    
    private BigDecimal getRanking(Participant participant) {
        BigDecimal rating = rankingMap.get(participant);
        if (rating == null){
            rating = ELO_INITIAL_RATING;
        }
        return rating;
    }
    
    private BigDecimal getTransformedRating(BigDecimal rating) {
        BigDecimal pow = rating.divide(ELO_MAGIC_NUMBER);
        Double bla = Math.pow(10d, pow.doubleValue());
        return new BigDecimal(bla, MathContext.DECIMAL128);
    }

    private BigDecimal getExpectedScore(BigDecimal r1, BigDecimal r2) {
        return r1.divide((r1.add(r2)), MathContext.DECIMAL128);
    }

    private BigDecimal getScore(Game game, Participant participant) {
        Map<Integer, Integer> setMapT1 = new HashMap<>();
        Map<Integer, Integer> setMapT2 = new HashMap<>();
        for (GameSet gameSet: game.getGameSets()){
            Participant p1 = gameSet.getParticipant();
            if (p1.equals(participant)){
                setMapT1.put(gameSet.getSetNumber(), gameSet.getSetGames());
            } else {
                setMapT2.put(gameSet.getSetNumber(), gameSet.getSetGames());
            }
        }
        int setsPlayed = game.getGameSets().size()/2;
        
        int setsWon = 0;
        for (Integer set: setMapT1.keySet()){
            int gamesWonP1  = setMapT1.get(set) == null ? 0 : setMapT1.get(set);
            int gamesWonP2  = setMapT2.get(set) == null ? 0 : setMapT2.get(set);
            if (gamesWonP1 > gamesWonP2){
                setsWon++;
            }
        }
        if (setsWon > (setsPlayed-setsWon)){
            return new BigDecimal(1);
        } else if (setsWon < (setsPlayed-setsWon)){
            return new BigDecimal(-1);
        }
        return new BigDecimal(0.5);
    }

    private BigDecimal getTeamRanking(Set<Player> players1) {
        BigDecimal ranking = BigDecimal.ZERO;
        for (Player player: players1){
            ranking = ranking.add(getRanking(player));
        }
        ranking = ranking.divide(new BigDecimal(players1.size()));
        return ranking;
    }

    private void updateTeamRanking(Set<Player> players1, BigDecimal newR1) {
        for (Player player: players1){
            rankingMap.put(player, newR1);
        }
    }
}
