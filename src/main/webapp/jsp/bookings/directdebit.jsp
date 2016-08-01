<%@ page pageEncoding="UTF-8" contentType="text/html" %> 
<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="DirectDebit"/></h4>
            </div>
            <div class="panel-body">
                <form action="#" class="form-signin paymill-form" method="POST" data-payment-type="directdebit">
                    <div id="error" class="alert alert-danger">${error}</div>
                    <input class="card-amount-int" type="hidden" value="<fmt:formatNumber  value="${Booking.amountInt}" pattern="#"/>" />
                    <input class="card-currency" type="hidden" value="${Booking.currency}" />

                    <fmt:message key="Accountholder" var="placeholder"/>
                    <input class="holdername form-control form-top-element" type="text" placeholder="${placeholder}"/>
                    <fmt:message key="IBAN" var="placeholder"/>
                    <input class="iban  form-control form-center-element" type="text" placeholder="${placeholder}" />

                    <fmt:message key="BIC" var="placeholder"/>
                    <input class="bic form-control form-bottom-element" type="text" placeholder="${placeholder}"/>

                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Book"/></button>
                    <a class="btn btn-primary btn-block unit ajaxify" href="/bookings/booking/${Booking.UUID}/abort"><fmt:message key="Cancel"/></a>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/bookings/include/paymill.jsp"/>
<jsp:include page="/jsp/include/footer.jsp"/>