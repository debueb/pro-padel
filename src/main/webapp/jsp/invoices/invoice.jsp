<%@ page pageEncoding="UTF-8" contentType="text/html" %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><fmt:message key="Invoice"/></title>
    
    <style type="text/css">
        body{
            font-family: Arial;
        }
        .letter{
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }
        .logo{
            float: right;
        }
        .clearfix{
            clear:both;
        }
        .small{
            font-size: 10px;
        }
        .date{
            float:right;
        }
        .margin-bottom{
            margin-bottom: 20px;
        }
        .margin-bottom-2{
            margin-bottom: 40px;
        }
        table.invoice{
            width: 100%;
        }
        table.invoice th, table.invoice td{
            text-align: left;
        }
        .centered{
            text-align: center;
        }
    </style>
</head>

<body>
    <div class="letter">
        <img class="logo margin-bottom" src="/images/logo.png" alt="logo"></img>
        <div class="clearfix"></div>
        <div class="small">${MasterData.companyName}, ${MasterData.street}, ${MasterData.zip} ${MasterData.city}</div>
        <div class="recipient margin-bottom">
            <div>${Booking.player}</div>
            <div>${Booking.player.email}</div>
            <div>${Booking.player.phone}</div>
        </div>
        <div class="date margin-bottom-2">
            <joda:format value="${Booking.bookingDate}" pattern="EEE, dd. MMM. YYYY"/>
        </div>
        <div class="clearfix"></div>
        <div class="margin-bottom-2"><fmt:message key="InvoiceNumber"/>: ${Booking.UUID}</div>
        <div class="margin-bottom"><fmt:message key="Title_${Booking.player.gender}"><fmt:param value="${Booking.player}"/></fmt:message>,</div>
        <div class="margin-bottom-2"><fmt:message key="InvoiceText"/>:</div>
        <div class="margin-bottom">
            <table class="invoice">
                <thead>
                    <th><fmt:message key="Offer"/></th>
                    <th><fmt:message key="Date"/></th>
                    <th><fmt:message key="Time"/></th>
                    <th><fmt:message key="Duration"/></th>
                    <th><fmt:message key="PaymentMethod"/></th>
                    <th><fmt:message key="Price"/></th>
                </thead>
                <tbody>
                    <tr>
                        <td>${Booking.offer}</td>
                        <td><joda:format value="${Booking.bookingDate}" pattern="EEE, dd. MMM. YYYY"/></td>
                        <td><joda:format value="${Booking.bookingTime}" pattern="HH:mm"/></td>
                        <td>${Booking.duration} <fmt:message key="Minutes"/></td>
                        <td><fmt:message key="${Booking.paymentMethod}"/></td>
                        <td>${Booking.currency} ${Booking.amount}</td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="margin-bottom-2">
            <fmt:message key="InvoiceText2"/>
        </div>
        <div class="margin-bottom-2">
            <fmt:message key="InvoiceBye"/><br/>
            ${MasterData.companyName}
        </div>
        <hr>
        <div class="small centered">
            <div>${MasterData.companyName}, ${MasterData.street}, ${MasterData.zip} ${MasterData.city}</div>
            <div><fmt:message key="IBAN"/>: ${MasterData.iban}, <fmt:message key="BIC"/>: ${MasterData.bic}, <fmt:message key="Institute"/>: ${MasterData.institute}</div>
            <div><fmt:message key="CompanyCity"/>: ${MasterData.companyCity}, <fmt:message key="TaxNumber"/>: ${MasterData.taxNumber}, <fmt:message key="TradeId"/>: ${MasterData.tradeId}</div>
            <div><fmt:message key="CEO"/>: ${MasterData.ceo}, <fmt:message key="EmailAddress"/>: <a href="mailto:${MasterData.email}">${MasterData.email}</a></div>
            <div>powererd by <a href="http://pro-padel.de">pro-padel.de</a><div>
        </div>
    </div>
</body>
</html>