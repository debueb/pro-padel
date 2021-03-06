/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.comparators.GameByEventComparator;
import de.appsolve.padelcampus.comparators.GameByParticipantComparator;
import de.appsolve.padelcampus.comparators.GameByStartDateComparator;
import de.appsolve.padelcampus.comparators.ParticipantByGameResultComparator;
import de.appsolve.padelcampus.db.dao.GameDAOI;
import de.appsolve.padelcampus.db.dao.TeamDAOI;
import de.appsolve.padelcampus.db.model.*;
import jersey.repackaged.com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static de.appsolve.padelcampus.constants.Constants.FIRST_SET;

/**
 * @author dominik
 */
@Component
public class GameUtil {

    @Autowired
    GameDAOI gameDAO;

    @Autowired
    TeamDAOI teamDAO;

    public Set<Game> getGames(Player player) {
        List<Team> teams = teamDAO.findByPlayer(player);
        return getGames(player, teams);
    }

    public Set<Game> getGames(Player player, List<Team> teams) {
        Set<Game> games = new TreeSet<>(gameDAO.findByParticipant(player));
        for (Team team : teams) {
            games.addAll(gameDAO.findByParticipant(team));
        }
        return games;
    }

    public Map<Game, String> getGameResultMap(Game game) {
        Map<Game, String> map = new HashMap<>();
        map.put(game, getGameResult(game, null, false));
        return map;
    }

    public Map<Game, String> getGameResultMap(Collection<Game> games) {
        Map<Game, String> map = new HashMap<>();
        for (Game game : games) {
            map.put(game, getGameResult(game, null, false));
        }
        return map;
    }

    public Map<Game, String> getGameResultMap(Collection<Game> games, Comparator<Game> comparator) {
        Map<Game, String> map = new TreeMap<>(comparator);
        for (Game game : games) {
            map.put(game, getGameResult(game, null, false));
        }
        return map;
    }

    public String getGameResult(Game game, final Participant sortByParticipant, final Boolean reverseGameResult) {
        StringBuilder result = new StringBuilder();

        List<GameSet> gameSets = new ArrayList<>(game.getGameSets());
        Collections.sort(gameSets);

        int gameSetsDisplayed = 0;
        for (int set = FIRST_SET; set < gameSets.size(); set++) {
            int participantId = 0;
            Set<Participant> participants = game.getParticipants();
            if (sortByParticipant != null) {
                SortedSet<Participant> sortedParticipants = new TreeSet<>(new ParticipantByGameResultComparator(sortByParticipant, reverseGameResult));
                sortedParticipants.addAll(participants);
                participants = sortedParticipants;
            }

            for (Participant participant : participants) {
                String setGames = "-";
                for (GameSet gs : gameSets) {
                    if (gs.getSetNumber() == set && gs.getParticipant().equals(participant)) {
                        setGames = gs.getSetGames() + "";
                        gameSetsDisplayed++;
                        break;
                    }
                }
                result.append(setGames);
                result.append(participantId % 2 == 0 ? ":" : " "); //separate games by colon, sets by space
                participantId++;
            }
            if (gameSetsDisplayed == gameSets.size()) {
                break; //do not display third set if it was not played
            }
        }

        return result.toString();
    }

    public void removeObsoleteGames(Event event) {
        removeObsoleteGames(event, event.getParticipants());
    }

    public void removeObsoleteGames(Event event, Set<? extends ParticipantI> newParticipants) {
        if (event.getId() != null) {
            List<Game> eventGames = gameDAO.findByEvent(event);
            Iterator<Game> eventGameIterator = eventGames.iterator();
            while (eventGameIterator.hasNext()) {
                Game game = eventGameIterator.next();
                //only remove if game participant no longer participates in event
                if (!newParticipants.containsAll(game.getParticipants())) {
                    eventGameIterator.remove();
                    gameDAO.deleteById(game.getId());
                }
            }
        }
    }

    public void createMissingGames(Event event, Set<Participant> participants) {
        createMissingGames(event, participants, null, null);
    }

    public void createMissingGames(Event event, Set<Participant> participants, Integer groupNumber, Integer roundNumber) {
        List<Game> existingGames = gameDAO.findByEvent(event);
        for (Participant firstParticipant : participants) {
            for (Participant secondParticipant : participants) {
                if (!firstParticipant.equals(secondParticipant)) {
                    boolean gameExists = false;
                    for (Game game : existingGames) {
                        if (roundNumber != null && !roundNumber.equals(game.getRound())) {
                            continue;
                        }
                        if (game.getParticipants().contains(firstParticipant) && game.getParticipants().contains(secondParticipant)) {
                            gameExists = true;
                            break;
                        }
                    }
                    if (!gameExists) {
                        existingGames.add(createGame(event, firstParticipant, secondParticipant, groupNumber, roundNumber));
                    }
                }
            }
        }
    }

    public List<Game> createMissingPullGames(Event event, Set<Team> participants) {
        List<Game> existingGames = gameDAO.findByEvent(event);
        for (Team team1 : participants) {
            for (Team team2 : participants) {
                if (!team1.equals(team2)) {
                    if (!hasMatch(existingGames, team1) && !hasMatch(existingGames, team2)) {
                        //make sure that players are distinct
                        if (Sets.intersection(team1.getPlayers(), team2.getPlayers()).isEmpty()) {
                            existingGames.add(createGame(event, team1, team2));
                        }
                    }
                }
            }
        }
        return existingGames;
    }

    public Map<Participant, Map<Game, String>> getParticipantGameResultMap(Collection<Game> games, Boolean reverseGameResult) {
        Map<Participant, Map<Game, String>> participantGameResultMap = new HashMap<>();
        for (Game game : games) {
            for (Participant p : game.getParticipants()) {
                Map<Game, String> gameResultMap = participantGameResultMap.get(p);
                if (gameResultMap == null) {
                    gameResultMap = new HashMap<>();
                }
                String result = getGameResult(game, p, reverseGameResult);
                gameResultMap.put(game, result);
                participantGameResultMap.put(p, gameResultMap);
            }
        }
        return participantGameResultMap;
    }

    private Game createGame(Event event, Participant firstParticipant, Participant secondParticipant) {
        return createGame(event, firstParticipant, secondParticipant, null, null);
    }

    private Game createGame(Event event, Participant firstParticipant, Participant secondParticipant, Integer groupNumber, Integer roundNumber) {
        Game game = new Game();
        game.setEvent(event);
        game.setGroupNumber(groupNumber);
        game.setRound(roundNumber);
        Set<Participant> gameParticipants = new LinkedHashSet<>();
        gameParticipants.add(firstParticipant);
        gameParticipants.add(secondParticipant);
        game.setParticipants(gameParticipants);
        game = gameDAO.saveOrUpdate(game);
        return game;
    }

    private boolean hasMatch(Collection<Game> existingGames, Team team) {
        for (Game game : existingGames) {
            if (game.getParticipants().contains(team)) {
                return true;
            }
        }
        return false;
    }

    public Map<Game, String> getGameResultMap(Player player, String sortBy) {
        Set<Game> games = getGames(player);
        //filter out games without opponent (e.g. wild card games)
        Iterator<Game> iterator = games.iterator();
        while (iterator.hasNext()) {
            Game game = iterator.next();
            if (game.getParticipants() == null || game.getParticipants().size() < 2) {
                iterator.remove();
            }
        }
        Comparator<Game> comparator;
        switch (sortBy) {
            case "event":
                comparator = new GameByEventComparator();
                break;
            case "participants":
                comparator = new GameByParticipantComparator();
                break;
            default:
                comparator = new GameByStartDateComparator();
        }
        return getGameResultMap(games, comparator);
    }
}
