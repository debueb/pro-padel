/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.pro;

import de.appsolve.padelcampus.controller.ranking.RankingController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author dominik
 */
@Controller
@RequestMapping("/pro/ranking")
public class ProRankingController extends RankingController {
    
   @Override
   public String getPath(){
       return "pro/";
   }
}
