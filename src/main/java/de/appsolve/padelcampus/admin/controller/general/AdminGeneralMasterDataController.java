/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.admin.controller.general;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.db.dao.MasterDataDAOI;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.MasterData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/general/masterdata")
public class AdminGeneralMasterDataController extends AdminBaseController<MasterData> {

    @Autowired
    MasterDataDAOI masterDataDAO;

    @Override
    public String getModuleName() {
        return "admin/general/masterdata";
    }

    @Override
    public BaseEntityDAOI getDAO() {
        return masterDataDAO;
    }

}
