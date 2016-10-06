<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="letter">
    <a href="/"><img class="logo margin-bottom" src="${sessionScope.customer.touchIconPath}" alt="logo"></img></a>
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
    <div class="margin-bottom-2"><fmt:message key="InvoiceNumber"/>: <joda:format value="${Booking.bookingDate}" pattern="YYYY"/>-<fmt:formatNumber value="${Booking.id}" minIntegerDigits="5" groupingUsed="false"/>-P</div>
    <div class="margin-bottom"><fmt:message key="Title_${Booking.player.gender}"><fmt:param value="${Booking.player}"/></fmt:message>,</div>
    <div class="margin-bottom-2"><fmt:message key="InvoiceText"/>:</div>
    <div class="margin-bottom">
        <table class="invoice">
            <thead>
            <th><fmt:message key="Offer"/></th>
            <th><fmt:message key="Date"/></th>
            <th><fmt:message key="Time"/></th>
            <c:if test="${not empty Booking.duration}">
                <th><fmt:message key="Duration"/></th>
            </c:if>
            <th><fmt:message key="PaymentMethod"/></th>
            <th><fmt:message key="Price"/></th>
            </thead>
            <tbody>
                <tr>
                    <td>${Booking.name}</td>
                    <td><joda:format value="${Booking.bookingDate}" pattern="EEE, dd. MMM. YYYY"/></td>
                    <td><joda:format value="${Booking.bookingTime}" pattern="HH:mm"/></td>
                    <c:if test="${not empty Booking.duration}">
                        <td>${Booking.duration} <fmt:message key="Minutes"/></td>
                    </c:if>
                    <td><fmt:message key="${Booking.paymentMethod}"/></td>
                    <td>${Booking.currency} ${Booking.amount}</td>
                </tr>
            </tbody>
        </table>
        <hr/>
        <div class="right"><fmt:message key="Total"/>: ${Booking.currency} ${Booking.amount}</div>
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
        <div>powered by <a href="http://pro-padel.de">pro-padel.de</a><div>
            </div>
        </div>
    </div>
</div>