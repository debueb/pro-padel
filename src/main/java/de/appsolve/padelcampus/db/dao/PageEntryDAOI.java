/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.dao;

import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.Module;
import de.appsolve.padelcampus.db.model.PageEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author dominik
 */
public interface PageEntryDAOI extends BaseEntityDAOI<PageEntry> {

    public List<PageEntry> findByModule(Module module);

    public Page<PageEntry> findByTitle(String title, Pageable pageable);

    public Page<PageEntry> findByModule(Module module, Pageable pageable);
}
