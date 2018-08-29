/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.players.transactions;

import de.appsolve.padelcampus.db.dao.PlayerDAOI;
import de.appsolve.padelcampus.db.dao.TransactionDAOI;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.db.model.Transaction;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import java.math.BigDecimal;
import java.util.List;

import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/players/{playerUUID}/transactions")
public class AdminPlayerTransactionsController {

    @Autowired
    PlayerDAOI playerDAO;

    @Autowired
    TransactionDAOI transactionDAO;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
    }

    @GetMapping
    public ModelAndView get(@PathVariable String playerUUID) {
        Player player = playerDAO.findByUUID(playerUUID);
        List<Transaction> transactions = transactionDAO.findByPlayer(player);
        ModelAndView mav = new ModelAndView("admin/players/transactions/index");
        mav.addObject("Player", player);
        mav.addObject("Transactions", transactions);
        return mav;
    }

    @GetMapping(value = {"/add", "/{transactionId}"})
    public ModelAndView getEdit(
            @PathVariable String playerUUID,
            @PathVariable(required = false) Long transactionId
    ) {
        Player player = playerDAO.findByUUID(playerUUID);
        Transaction transaction = new Transaction();
        transaction.setDate(LocalDate.now());
        if (transactionId != null) {
            transaction = transactionDAO.findById(transactionId);
        }
        return getEditView(player, transaction);
    }

    @PostMapping(value = {"/add", "/{transactionId}"})
    public ModelAndView postEdit(
            @PathVariable String playerUUID,
            @PathVariable(required = false) Long transactionId,
            @Valid @ModelAttribute("Model") Transaction transaction,
            BindingResult bindingResult
    ) {
        Player player = playerDAO.findByUUID(playerUUID);
        if (bindingResult.hasErrors()) {
            return getEditView(player, transaction);
        }
        if (transactionId != null && transactionId != transaction.getId()) {
            throw new BadRequestException("invalid transaction id");
        }
        transaction.setPlayer(player);
        transactionDAO.saveOrUpdate(transaction);
        player.setBalance(getBalance(player));
        playerDAO.saveOrUpdate(player);
        return new ModelAndView(String.format("redirect:/admin/players/%s/transactions", player.getUUID()));
    }

    private BigDecimal getBalance(Player player) {
        List<Transaction> transactions = transactionDAO.findByPlayer(player);
        BigDecimal total = BigDecimal.ZERO;
        for (Transaction tx : transactions) {
            total = total.add(tx.getAmount());
        }
        return total;
    }

    private ModelAndView getEditView(Player player, Transaction transaction) {
        ModelAndView mav = new ModelAndView("admin/players/transactions/edit");
        mav.addObject("Player", player);
        mav.addObject("Model", transaction);
        return mav;
    }
}
