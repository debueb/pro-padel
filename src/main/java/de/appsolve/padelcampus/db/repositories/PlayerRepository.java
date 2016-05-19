/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.db.repositories;

import de.appsolve.padelcampus.db.model.Player;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public interface PlayerRepository extends PagingAndSortingRepository<Player, Long>{
    
}
