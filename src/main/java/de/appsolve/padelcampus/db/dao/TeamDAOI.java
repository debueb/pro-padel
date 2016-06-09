/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Team;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author dominik
 */
public interface TeamDAOI extends BaseEntityDAOI<Team>{
    
    public Team findByUUID(String UUID);
    
    public Team findByPlayers(Set<Player> players);
    
    Team findByIdFetchWithPlayers(Long id);
    
    Team findByUUIDFetchWithPlayers(String uuid);
    
    List<Team> findByPlayer(Player player);
    
    Page<Team> findAllFetchWithPlayers(Pageable pageable);
}
