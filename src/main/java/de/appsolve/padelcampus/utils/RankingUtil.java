/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.comparators.GameByStartDateComparator;
import static de.appsolve.padelcampus.constants.Constants.MATCH_PLAY_FACTOR;
import static de.appsolve.padelcampus.constants.Constants.MATCH_WIN_FACTOR;
import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.data.ScoreEntry;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.GameDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.Event;
import de.appsolve.padelcampus.db.model.Game;
import de.appsolve.padelcampus.db.model.GameSet;
import de.appsolve.padelcampus.db.model.Participant;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 * see https://metinmediamath.wordpress.com/2013/11/27/how-to-calculate-the-elo-rating-including-example/
 * 
 */
@Component
public class RankingUtil {

    private static final Logger LOG = Logger.getLogger(RankingUtil.class);

    private static final int ELO_MAX_DAYS = 365;

    private static final BigDecimal ELO_K_FACTOR = new BigDecimal("32");

    private static final BigDecimal ELO_MAGIC_NUMBER = new BigDecimal("400");

    private Map<Participant, BigDecimal> rankingMap;

    @Autowired
    GameDAOI gameDAO;

    @Autowired
    EventDAOI eventDAO;
    
    @Autowired
    TeamDAOI teamDAO;
    
    public Map<Participant, BigDecimal> getTeamRanking(Gender gender) {
        List<Game> games = getGamesInLastYear(gender);
        Map<Participant, BigDecimal> ranking = getRanking(games);
        Set<Team> teams = new HashSet<>();
        for (Game game: games){
            Set<Participant> participants = game.getParticipants();
            for (Participant p : participants){
                if (p instanceof Team){
                    teams.add((Team)p);
                }
            }
        }
        return getTeamRanking(ranking, teams);
    }
    
    public Map<Participant, BigDecimal> getTeamRanking(Gender category, Collection<Team> teams) {
        Map<Participant, BigDecimal> ranking = getRanking(category);
        return getTeamRanking(ranking, teams);
    }
    
    private Map<Participant, BigDecimal> getTeamRanking(Map<Participant, BigDecimal> ranking, Collection<Team> teams) {
        Map<Participant, BigDecimal> teamRanking = new HashMap<>();

        for (Team team : teams) {
            BigDecimal teamScore = BigDecimal.ZERO;
            for (Player player : team.getPlayers()) {
                if (ranking.containsKey(player)) {
                    teamScore = teamScore.add(ranking.get(player));
                } else {
                    teamScore = teamScore.add(player.getInitialRankingAsBigDecimal());
                }
            }
            teamScore = teamScore.divide(new BigDecimal(team.getPlayers().size()));
            teamScore = teamScore.setScale(0, RoundingMode.HALF_UP);
            teamRanking.put(team, teamScore);
        }
        return teamRanking;
    }

    public Map<Participant, BigDecimal> getRanking(Gender gender) {
        List<Game> games = getGamesInLastYear(gender);
        return getRanking(games);
    }
    
    public Map<Participant, BigDecimal> getPlayerRanking(Gender category, Collection<Player> participants) {
        Map<Participant, BigDecimal> ranking = getRanking(category);
        Map<Participant, BigDecimal> eventRanking = new HashMap<>();
        for (Map.Entry<Participant, BigDecimal> entry: ranking.entrySet()){
            Participant participant = entry.getKey();
            if (participant instanceof Player){
                Player player = (Player) participant;
                if (participants.contains(player)){
                    eventRanking.put(player, entry.getValue());
                }
            }
        }
        //add all participants without score
        for (Participant p : participants) {
            if (!eventRanking.containsKey(p)) {
                eventRanking.put(p, p.getInitialRankingAsBigDecimal());
            }
        }
        return eventRanking;
    }
    
    private Map<Participant, BigDecimal> getRanking(List<Game> games) {
        rankingMap = new TreeMap<>();
        SortedSet<Game> sortedGames = new TreeSet<>(new GameByStartDateComparator());
        sortedGames.addAll(games);
        for (Game game : sortedGames) {
            Set<Participant> participants = game.getParticipants();
            if (participants.size() != 2) {
                LOG.warn("Skipping game " + game + " as it does not have 2 participants");
                continue;
            }
            if (game.getGameSets().isEmpty()) {
                LOG.debug("Skipping game " + game + " as no game sets have been played");
                continue;
            }

            Iterator<Participant> iterator = participants.iterator();
            Participant p1 = iterator.next();
            Participant p2 = iterator.next();

            if (p1 instanceof Team && p2 instanceof Team){
                updateRanking(game, (Team) p1, (Team) p2);
            } else {
                updateRanking(game, p1, p2);
            }
        }
        Iterator<Map.Entry<Participant, BigDecimal>> iterator = rankingMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Participant, BigDecimal> entry = iterator.next();
            BigDecimal value = entry.getValue();
            value = value.setScale(0, RoundingMode.HALF_UP);
            entry.setValue(value);
        }
        return rankingMap;
    }
    
    public Map<Participant, BigDecimal> getPlayerRanking(Collection<Player> players){
        Set<Player> malePlayers = new HashSet<>();
        Set<Player> femalePlayers = new HashSet<>();
        for (Player player: players){
            if (player.getGender().equals(Gender.male)){
                malePlayers.add(player);
            } else {
                femalePlayers.add(player);
            }
        }
        Map<Participant, BigDecimal> ranking = getPlayerRanking(Gender.male, malePlayers);
        ranking.putAll(getPlayerRanking(Gender.female, femalePlayers));
        return ranking;
    }
    
    public Map<Participant, BigDecimal> getTeamRanking(Collection<Team> teams){
        Set<Team> maleTeams = new HashSet<>();
        Set<Team> femaleTeams = new HashSet<>();
        Set<Team> mixedTeams = new HashSet<>();
        for (Team team: teams){
            Set<Gender> teamGender = new HashSet<>();
            for (Player player: team.getPlayers()){
                teamGender.add(player.getGender());
            }
            if (teamGender.size() > 1){
                mixedTeams.add(team);
            } else if (teamGender.iterator().next().equals(Gender.male)){
                maleTeams.add(team);
            } else {
                femaleTeams.add(team);
            }
        }
        Map<Participant, BigDecimal> ranking = getTeamRanking(Gender.male, maleTeams);
        ranking.putAll(getTeamRanking(Gender.female, femaleTeams));
        ranking.putAll(getTeamRanking(Gender.mixed, mixedTeams));
        return ranking;
    }

    private void updateRanking(Game game, Team team1, Team team2) {
        BigDecimal r1 = getTeamRanking((Team)team1);
        BigDecimal r2 = getTeamRanking((Team)team2);
        
        for (Player player: team1.getPlayers()){
            BigDecimal r1p1 = getRanking(player);
            BigDecimal tr1 = getTransformedRating(r1);
            BigDecimal tr2 = getTransformedRating(r2);
            
            BigDecimal e1 = getExpectedScore(tr1, tr2);
            BigDecimal s1 = getScore(game, team1);
            BigDecimal newR1 = ELO_K_FACTOR.multiply((s1.subtract(e1))).add(r1p1);
            rankingMap.put(player, newR1);
        }
        
        for (Player player: team2.getPlayers()){
            BigDecimal r2p1 = getRanking(player);
            BigDecimal tr1 = getTransformedRating(r2);
            BigDecimal tr2 = getTransformedRating(r1);
            
            BigDecimal e1 = getExpectedScore(tr1, tr2);
            BigDecimal s1 = getScore(game, team2);
            BigDecimal newR1 = ELO_K_FACTOR.multiply((s1.subtract(e1))).add(r2p1);
            rankingMap.put(player, newR1);
        }
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
        if (rating == null) {
            rating = participant.getInitialRankingAsBigDecimal();
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
        for (GameSet gameSet : game.getGameSets()) {
            Participant p1 = gameSet.getParticipant();
            if (p1.equals(participant)) {
                setMapT1.put(gameSet.getSetNumber(), gameSet.getSetGames());
            } else {
                setMapT2.put(gameSet.getSetNumber(), gameSet.getSetGames());
            }
        }
        int setsPlayed = game.getGameSets().size() / 2;

        int setsWon = 0;
        for (Map.Entry<Integer, Integer> entry : setMapT1.entrySet()) {
            Integer set = entry.getKey();
            Integer games = entry.getValue();
            int gamesWonP1 = games == null ? 0 : games;
            int gamesWonP2 = setMapT2.get(set) == null ? 0 : setMapT2.get(set);
            if (gamesWonP1 > gamesWonP2) {
                setsWon++;
            }
        }
        if (setsWon > (setsPlayed - setsWon)) {
            return new BigDecimal(1);
        } else if (setsWon < (setsPlayed - setsWon)) {
            return new BigDecimal(0);
        }
        return new BigDecimal(0.5);
    }

    private BigDecimal getTeamRanking(Team team) {
        BigDecimal ranking = BigDecimal.ZERO;
        for (Player player : team.getPlayers()) {
            ranking = ranking.add(getRanking(player));
        }
        ranking = ranking.divide(new BigDecimal(team.getPlayers().size()));
        return ranking;
    }

    public ScoreEntry getScore(Participant participant, Collection<Game> games) {
        int matchesPlayed = 0;
        int matchesWon = 0;
        int totalSetsPlayed = 0;
        int totalSetsWon = 0;
        int totalGamesPlayed = 0;
        int totalGamesWon = 0;

        List<GameSet> gameSets = new ArrayList<>();
        for (Game game : games) {
            gameSets.addAll(game.getGameSets());
        }

        for (Game game : games) {
            if (game.getParticipants().contains(participant)) {
                int setsPlayed = 0;
                int setsWon = 0;

                Map<Integer, Integer> setMapP1 = getSetMapForParticipant(game, participant, gameSets);
                Map<Integer, Integer> setMapP2 = new HashMap<>();
                for (Participant opponent : game.getParticipants()) {
                    if (!opponent.equals(participant)) {
                        setMapP2 = getSetMapForParticipant(game, opponent, gameSets);
                        break;
                    }
                }

                for (Map.Entry<Integer, Integer> entry : setMapP1.entrySet()) {
                    Integer set = entry.getKey();
                    Integer setGames = entry.getValue();
                    setsPlayed++;
                    totalSetsPlayed++;
                    int gamesWonP1 = setGames == null ? 0 : setGames;
                    int gamesWonP2 = setMapP2.get(set) == null ? 0 : setMapP2.get(set);
                    totalGamesWon += gamesWonP1;
                    setsWon += gamesWonP1 > gamesWonP2 ? 1 : 0;
                    totalSetsWon += gamesWonP1 > gamesWonP2 ? 1 : 0;
                    totalGamesPlayed += gamesWonP1 + gamesWonP2;
                }

                if (setsPlayed > 0) {
                    matchesPlayed++;
                    matchesWon += setsWon > (setsPlayed - setsWon) ? 1 : 0;
                }
            }
        }

        ScoreEntry entry = new ScoreEntry();
        entry.setParticipant(participant);
        entry.setTotalPoints(matchesWon * MATCH_WIN_FACTOR + (matchesPlayed - matchesWon) * MATCH_PLAY_FACTOR);
        entry.setMatchesPlayed(matchesPlayed);
        entry.setMatchesWon(matchesWon);
        entry.setSetsPlayed(totalSetsPlayed);
        entry.setSetsWon(totalSetsWon);
        entry.setGamesWon(totalGamesWon);
        entry.setGamesPlayed(totalGamesPlayed);
        return entry;
    }

    public List<ScoreEntry> getScores(Collection<Participant> participants, Collection<Game> eventGames) {
        List<ScoreEntry> scoreEntries = new ArrayList<>();
        for (Participant p : participants) {
            scoreEntries.add(getScore(p, eventGames));
        }
        Collections.sort(scoreEntries);
        return scoreEntries;
    }
    
    public Map<Participant, BigDecimal> getRankedParticipants(Event model) {
        Map<Participant, BigDecimal> ranking = new HashMap<>();
        if (!model.getParticipants().isEmpty()){
            Participant firstParticipant = model.getParticipants().iterator().next();
            if (firstParticipant instanceof Player){
                ranking = getPlayerRanking(model.getGender(), model.getPlayers());
            } else if (firstParticipant instanceof Team){
                List<Team> teams = new ArrayList<>();
                for (Participant p: model.getParticipants()){
                    Team team = (Team) p;
                    teams.add(teamDAO.findByIdFetchWithPlayers(team.getId()));
                }
                ranking = getTeamRanking(model.getGender(), teams);
            }
        }
        return ranking;
    }
    
    private Map<Integer, Integer> getSetMapForParticipant(Game game, Participant participant, Collection<GameSet> gameSets) {
        Map<Integer, Integer> setMap = new HashMap<>();
        for (GameSet set : gameSets) {
            if (set.getGame().equals(game) && set.getParticipant().equals(participant)) {
                setMap.put(set.getSetNumber(), set.getSetGames());
            }
        }
        return setMap;
    }
    
    private List<Game> getGamesInLastYear(Gender gender) {
        LocalDate date = LocalDate.now();
        date = date.minusDays(ELO_MAX_DAYS);
        List<Game> games = gameDAO.findAllYoungerThanForGenderWithPlayers(date, gender);
        return games;
    }
}
