/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.comparators.GameByStartDateComparator;
import de.appsolve.padelcampus.constants.Gender;
import de.appsolve.padelcampus.data.ScoreEntry;
import de.appsolve.padelcampus.db.dao.GameDAOI;
import de.appsolve.padelcampus.db.dao.RankingDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.*;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author dominik
 * see https://metinmediamath.wordpress.com/2013/11/27/how-to-calculate-the-elo-rating-including-example/
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RankingUtil {

    private static final Logger LOG = Logger.getLogger(RankingUtil.class);

    private static final int ELO_MAX_DAYS = 365;

    private static final BigDecimal ELO_K_FACTOR = new BigDecimal("32");

    private static final BigDecimal ELO_MAGIC_NUMBER = new BigDecimal("400");

    @Autowired
    GameDAOI gameDAO;

    @Autowired
    TeamDAOI teamDAO;

    @Autowired
    RankingDAOI rankingDAO;

    public List<Ranking> getTeamRanking(Gender gender, LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        List<Ranking> rankings = getRanking(gender, date);
        List<Game> games = getGamesInLastYearSince(gender, date);
        Set<Team> teams = new HashSet<>();
        for (Game game : games) {
            Set<Participant> participants = game.getParticipants();
            for (Participant p : participants) {
                if (p instanceof Team) {
                    teams.add((Team) p);
                }
            }
        }
        return getTeamRanking(gender, rankings, teams, date);
    }

    public List<Ranking> getTeamRanking(Gender gender, Collection<Team> teams, LocalDate date) {
        List<Ranking> rankings = getRanking(gender, date);
        return getTeamRanking(gender, rankings, teams, date);
    }

    public List<Ranking> getRanking(Gender gender, LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        List<Ranking> rankings = rankingDAO.findByGenderAndDate(gender, date);
        if (rankings == null || rankings.isEmpty()) {
            rankings = updateRanking(gender, date);
        }
        rankings.forEach(r -> {
            r.setValue(r.getValue().setScale(0, RoundingMode.HALF_UP));
        });
        return rankings;
    }

    @Async
    @Transactional
    public void updateRanking() {
        LocalDate date = LocalDate.now();
        for (Gender gender : Gender.values()) {
            updateRanking(gender, date);
        }
    }

    private List<Ranking> updateRanking(Gender gender, LocalDate date) {
        try {
            LOG.info(String.format("updating ranking for [gender: %s, date: %s] - start", gender, date.toString()));
            List<Game> games = getGamesInLastYearSince(gender, date);
            List<Ranking> rankings = getRanking(games, gender, date);
            List<Ranking> existingRankings = rankingDAO.findByGenderAndDate(gender, date);
            rankingDAO.delete(existingRankings);
            rankingDAO.saveOrUpdate(rankings);
            LOG.info(String.format("updating ranking for [gender: %s, date: %s] - end", gender, date.toString()));
            return rankings;
        } catch (Exception e) {
            LOG.error(e, e);
            return null;
        }
    }

    public List<Ranking> getPlayerRanking(Gender category, Collection<Player> participants) {
        return getPlayerRanking(category, participants, LocalDate.now());
    }

    public List<Ranking> getPlayerRanking(Gender gender, Collection<Player> participants, final LocalDate date) {
        List<Ranking> rankings = getRanking(gender, date);
        List<Ranking> eventRanking = new ArrayList<>();
        rankings.forEach(ranking -> {
            Participant participant = ranking.getParticipant();
            if (participant instanceof Player) {
                Player player = (Player) participant;
                if (participants.contains(player)) {
                    eventRanking.add(ranking);
                }
            }
        });
        participants.forEach(participant -> {
            if (!eventRanking.contains(participant)) {
                Ranking ranking = new Ranking(participant, gender, participant.getInitialRankingAsBigDecimal(), date);
                eventRanking.add(ranking);
            }
        });
        Collections.sort(eventRanking);
        return eventRanking;
    }

    public List<Ranking> getPlayerRanking(Collection<Player> players, LocalDate date) {
        Set<Player> malePlayers = new HashSet<>();
        Set<Player> femalePlayers = new HashSet<>();
        for (Player player : players) {
            if (player.getGender().equals(Gender.male)) {
                malePlayers.add(player);
            } else {
                femalePlayers.add(player);
            }
        }
        List<Ranking> rankings = getPlayerRanking(Gender.male, malePlayers, date);
        rankings.addAll(getPlayerRanking(Gender.female, femalePlayers));
        Collections.sort(rankings);
        return rankings;
    }

    public List<Ranking> getTeamRanking(Collection<Team> teams, LocalDate date) {
        Set<Team> maleTeams = new HashSet<>();
        Set<Team> femaleTeams = new HashSet<>();
        Set<Team> mixedTeams = new HashSet<>();
        for (Team team : teams) {
            Set<Gender> teamGender = new HashSet<>();
            for (Player player : team.getPlayers()) {
                teamGender.add(player.getGender());
            }
            if (teamGender.size() > 1) {
                mixedTeams.add(team);
            } else if (teamGender.iterator().next().equals(Gender.male)) {
                maleTeams.add(team);
            } else {
                femaleTeams.add(team);
            }
        }
        List<Ranking> ranking = getTeamRanking(Gender.male, maleTeams, date);
        ranking.addAll(getTeamRanking(Gender.female, femaleTeams, date));
        ranking.addAll(getTeamRanking(Gender.mixed, mixedTeams, date));
        Collections.sort(ranking);
        return ranking;
    }

    public List<Ranking> getRankedParticipants(Event model) {
        List<Ranking> ranking = new ArrayList<>();
        if (!model.getParticipants().isEmpty()) {
            Participant firstParticipant = model.getParticipants().iterator().next();
            if (firstParticipant instanceof Player) {
                ranking = getPlayerRanking(model.getGender(), model.getPlayers(), LocalDate.now());
            } else if (firstParticipant instanceof Team) {
                List<Team> teams = new ArrayList<>();
                for (Participant p : model.getParticipants()) {
                    Team team = (Team) p;
                    teams.add(teamDAO.findByIdFetchWithPlayers(team.getId()));
                }
                ranking = getTeamRanking(model.getGender(), teams, LocalDate.now());
            }
        }
        Collections.sort(ranking);
        return ranking;
    }

    private List<Ranking> getTeamRanking(Gender gender, List<Ranking> rankings, Collection<Team> teams, LocalDate date) {
        List<Ranking> teamRanking = new ArrayList<>();

        teams.forEach(team -> {
            BigDecimal teamScore = BigDecimal.ZERO;
            for (Player player : team.getPlayers()) {
                Optional<Ranking> ranking = rankings.stream().filter(r -> r.getParticipant().equals(player)).findFirst();
                if (ranking.isPresent()) {
                    teamScore = teamScore.add(ranking.get().getValue());
                } else {
                    teamScore = teamScore.add(player.getInitialRankingAsBigDecimal());
                }
            }
            teamScore = teamScore.divide(new BigDecimal(team.getPlayers().size()));
            teamScore = teamScore.setScale(0, RoundingMode.HALF_UP);
            teamRanking.removeIf(ranking -> ranking.getParticipant().equals(team));
            teamRanking.add(new Ranking(team, gender, teamScore, date));
        });
        Collections.sort(teamRanking);
        return teamRanking;
    }

    private List<Ranking> getRanking(List<Game> games, Gender gender, LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        List<Ranking> rankings = new ArrayList<>();
        Set<Game> sortedGames = new TreeSet<>(new GameByStartDateComparator());
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

            if (p1 instanceof Team && p2 instanceof Team) {
                updateRanking(rankings, game, gender, (Team) p1, (Team) p2, date);
            } else {
                updateRanking(rankings, game, gender, p1, p2, date);
            }
        }
        Collections.sort(rankings);
        return rankings;
    }

    private void updateRanking(List<Ranking> rankingMap, Game game, Gender gender, Team team1, Team team2, LocalDate date) {
        BigDecimal r1 = getTeamRanking(rankingMap, (Team) team1);
        BigDecimal r2 = getTeamRanking(rankingMap, (Team) team2);

        for (Player player : team1.getPlayers()) {
            BigDecimal r1p1 = getRanking(rankingMap, player);
            BigDecimal tr1 = getTransformedRating(r1);
            BigDecimal tr2 = getTransformedRating(r2);

            BigDecimal e1 = getExpectedScore(tr1, tr2);
            BigDecimal s1 = getScore(game, team1);
            BigDecimal newR1 = ELO_K_FACTOR.multiply((s1.subtract(e1))).add(r1p1);
            rankingMap.removeIf(r -> r.getParticipant().equals(player));
            rankingMap.add(new Ranking(player, gender, newR1, date));
        }

        for (Player player : team2.getPlayers()) {
            BigDecimal r2p1 = getRanking(rankingMap, player);
            BigDecimal tr1 = getTransformedRating(r2);
            BigDecimal tr2 = getTransformedRating(r1);

            BigDecimal e1 = getExpectedScore(tr1, tr2);
            BigDecimal s1 = getScore(game, team2);
            BigDecimal newR1 = ELO_K_FACTOR.multiply((s1.subtract(e1))).add(r2p1);
            rankingMap.removeIf(r -> r.getParticipant().equals(player));
            rankingMap.add(new Ranking(player, gender, newR1, date));
        }
    }

    private void updateRanking(List<Ranking> rankingMap, Game game, Gender gender, Participant p1, Participant p2, LocalDate date) {
        BigDecimal r1 = getRanking(rankingMap, p1);
        BigDecimal r2 = getRanking(rankingMap, p2);

        BigDecimal tr1 = getTransformedRating(r1);
        BigDecimal tr2 = getTransformedRating(r2);

        BigDecimal e1 = getExpectedScore(tr1, tr2);
        BigDecimal e2 = getExpectedScore(tr2, tr1);

        BigDecimal s1 = getScore(game, p1);
        BigDecimal s2 = getScore(game, p2);

        BigDecimal newR1 = ELO_K_FACTOR.multiply((s1.subtract(e1))).add(r1);
        BigDecimal newR2 = ELO_K_FACTOR.multiply((s2.subtract(e2))).add(r2);

        rankingMap.removeIf(ranking -> ranking.getParticipant().equals(p1));
        rankingMap.removeIf(ranking -> ranking.getParticipant().equals(p2));
        rankingMap.add(new Ranking(p1, gender, newR1, date));
        rankingMap.add(new Ranking(p2, gender, newR2, date));
    }

    private BigDecimal getRanking(List<Ranking> rankings, Participant participant) {
        Optional<Ranking> ranking = rankings.stream().filter(r -> r.getParticipant().equals(participant)).findFirst();
        if (ranking.isPresent()) {
            return ranking.get().getValue();
        }
        return participant.getInitialRankingAsBigDecimal();
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

    private BigDecimal getTeamRanking(List<Ranking> rankings, Team team) {
        BigDecimal ranking = BigDecimal.ZERO;
        for (Player player : team.getPlayers()) {
            ranking = ranking.add(getRanking(rankings, player));
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

    private Map<Integer, Integer> getSetMapForParticipant(Game game, Participant participant, Collection<GameSet> gameSets) {
        Map<Integer, Integer> setMap = new HashMap<>();
        for (GameSet set : gameSets) {
            if (set.getGame().equals(game) && set.getParticipant().equals(participant)) {
                setMap.put(set.getSetNumber(), set.getSetGames());
            }
        }
        return setMap;
    }

    private List<Game> getGamesInLastYearSince(Gender gender, LocalDate date) {
        LocalDate since = date.minusDays(ELO_MAX_DAYS);
        List<Game> games = gameDAO.findAllYoungerThanForGenderWithPlayers(since, gender);
        return games;
    }
}
