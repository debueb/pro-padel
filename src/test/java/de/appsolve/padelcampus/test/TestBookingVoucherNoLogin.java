/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test;

import org.junit.Test;

/**
 *
 * @author dominik
 */
public class TestBookingVoucherNoLogin extends TestBookingVoucher {

    
    @Test
    public void testBookingWorkflowVoucherNoLogin() throws Exception {
        bookViaVoucherAndNoLogin(offer1);
    }
}
