/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.db.dao.GameDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author dominik
 */
@Component
public class EventsUtil {

    @Autowired
    GameDAOI gameDAO;

    @Autowired
    TeamDAOI teamDAO;

    @Autowired
    GameUtil gameUtil;

    public SortedMap<Integer, List<Game>> getRoundGameMap(Event event) {
        return getRoundGameMap(event, null);
    }

    public SortedMap<Integer, List<Game>> getRoundGameMap(Event event, Integer round) {
        SortedMap<Integer, List<Game>> roundGames = new TreeMap<>();

        for (Game game : event.getGames()) {
            if (game.getRound() != null) {
                if (round != null && !game.getRound().equals(round)) {
                    continue;
                }
                List<Game> games = roundGames.get(game.getRound());
                if (games == null) {
                    games = new ArrayList<>();
                }
                games.add(game);
                roundGames.put(game.getRound(), games);
            }
        }
        return roundGames;
    }

    public SortedMap<Integer, List<Game>> getGroupGameMap(Event event) {
        return getGroupGameMap(event, null);
    }

    public SortedMap<Integer, List<Game>> getGroupGameMap(Event event, Integer round) {
        SortedMap<Integer, List<Game>> groupGames = new TreeMap<>();

        for (Game game : event.getGames()) {
            if (game.getGroupNumber() != null) {
                if (round != null && !round.equals(game.getRound())) {
                    continue;
                }
                List<Game> games = groupGames.get(game.getGroupNumber());
                if (games == null) {
                    games = new ArrayList<>();
                }
                games.add(game);
                groupGames.put(game.getGroupNumber(), games);
            }
        }
        return groupGames;
    }


    public void createKnockoutGames(Event event, List<Participant> participants) {
        //determine number of games per round
        int numGamesPerRound = Integer.highestOneBit(participants.size() - 1);

        //determine seed positions
        List<Integer> seedingPositions = getSeedPositions(participants);

        //fill up empty spots with bye's
        for (int i = participants.size(); i < numGamesPerRound * 2; i++) {
            participants.add(null);
        }

        //create games
        int round = 0;
        SortedMap<Integer, List<Game>> roundGames = new TreeMap<>();
        while (numGamesPerRound >= 1) {
            List<Game> games = new ArrayList<>();
            for (int i = 0; i < numGamesPerRound; i++) {
                Game game = new Game();
                game.setEvent(event);
                game.setRound(round);
                game = gameDAO.saveOrUpdate(game);

                if (round == 0) {
                    //set participants
                    Set<Participant> gameParticipants = new HashSet<>();
                    addParticipants(gameParticipants, participants.get(seedingPositions.get(i * 2)));
                    addParticipants(gameParticipants, participants.get(seedingPositions.get(i * 2 + 1)));
                    game.setParticipants(gameParticipants);
                } else {
                    //set game chain
                    List<Game> previousRoundGames = roundGames.get(round - 1);
                    Game first = previousRoundGames.get(i * 2);
                    Game second = previousRoundGames.get(i * 2 + 1);
                    first.setNextGame(game);
                    second.setNextGame(game);
                    gameDAO.saveOrUpdate(first);
                    gameDAO.saveOrUpdate(second);


                    if (round == 1) {
                        //advance seeds that have bye's
                        if (first.getParticipants().size() == 1) {
                            game.setParticipants(new HashSet<>(first.getParticipants()));
                        }
                        if (second.getParticipants().size() == 1) {
                            Set<Participant> existingParticipants = game.getParticipants();
                            existingParticipants.addAll(new HashSet<>(second.getParticipants()));
                            game.setParticipants(existingParticipants);
                        }
                    }

                }

                game = gameDAO.saveOrUpdate(game);
                games.add(game);
            }
            roundGames.put(round, games);
            numGamesPerRound = numGamesPerRound / 2;
            round++;
        }
    }

    private void addParticipants(Set<Participant> gameParticipants, Participant p) {
        if (p != null) {
            gameParticipants.add(p);
        }
    }

    private List<Integer> getSeedPositions(List<Participant> participants) {
        Double numberOfDivisionRuns = Math.log(participants.size()) / Math.log(2) - 1;
        List<Integer> seedingPositions = new ArrayList<>();
        seedingPositions.add(0);
        seedingPositions.add(1);
        for (int divisionRun = 0; divisionRun < numberOfDivisionRuns; divisionRun++) {
            int size = seedingPositions.size();
            List<Integer> newSeeedingPositions = new ArrayList<>();
            for (Integer position : seedingPositions) {
                newSeeedingPositions.add(position);
                newSeeedingPositions.add(size * 2 - 1 - position);
            }
            seedingPositions = newSeeedingPositions;
        }
        return seedingPositions;
    }

    public List<Game> createPullGames(Event model) {
        //create teams which do not exist yet
        Set<Team> teams = new HashSet<>();
        for (Player player1 : model.getPlayers()) {
            for (Player player2 : model.getPlayers()) {
                if (!player1.equals(player2)) {
                    Set<Player> players = new HashSet<>();
                    players.add(player1);
                    players.add(player2);
                    Team team = teamDAO.findByPlayers(players);
                    if (team == null) {
                        team = new Team();
                        team.setPlayers(players);
                        team.setName(TeamUtil.getTeamName(team));
                        team = teamDAO.saveOrUpdate(team);
                    } else {
                        //findByPlayers does not set players
                        team.setPlayers(players);
                    }
                    teams.add(team);
                }
            }
        }

        //remove games that have not been played yet
        gameUtil.removeObsoleteGames(model, teams);

        //create matches
        return gameUtil.createMissingPullGames(model, teams);
    }

}
