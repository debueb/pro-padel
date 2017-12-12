/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.tasks;

import de.appsolve.padelcampus.reporting.ErrorReporter;
import de.appsolve.padelcampus.utils.RankingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author dominik
 */
@Component
public class RankingTask {

    private static final Logger LOG = LoggerFactory.getLogger(RankingTask.class);

    @Autowired
    RankingUtil rankingUtil;

    @Autowired
    ErrorReporter errorReporter;

    @Scheduled(cron = "0 2 2 * * *") //second minute hour day month year, * = any, */5 = every 5
    public void updateRanking() {
        try {
            rankingUtil.updateRanking();
        } catch (Throwable t) {
            LOG.error(t.getMessage(), t);
            errorReporter.notify(t);
        }
    }

}
