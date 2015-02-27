/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import java.util.List;

/**
 *
 * @author dominik
 */
public interface TeamDAOI extends GenericDAOI<Team>{
    
    Team findByIdFetchWithPlayers(Long id);
    
    List<Team> findByPlayer(Player player);
    
    List<Team> findAllFetchWithPlayers();
}
