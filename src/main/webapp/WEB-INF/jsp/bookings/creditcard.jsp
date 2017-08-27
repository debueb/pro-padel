<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="CreditCard"/></h4>
            </div>
            <div class="panel-body">

                <form action="#" class="form-signin paymill-form" method="POST" data-payment-type="creditcard">

                    <div id="error" class="alert alert-danger">${error}</div>
                    <input class="card-amount-int" type="hidden" value="<fmt:formatNumber  value="${Booking.amountInt}" pattern="#"/>" />
                    <input class="card-currency" type="hidden" value="${Booking.currency}" />

                    <fmt:message key="Cardholder" var="placeholder"/>
                    <input class="card-holdername form-control form-top-element" type="text" placeholder="${placeholder}"/>
                    <fmt:message key="CardNumber" var="placeholder"/>
                    <input class="card-number form-control form-center-element" type="tel" maxlength="20" data-valid-chars="[0-9]" placeholder="${placeholder}" />

                    <%-- G�ltig bis --%>
                    <span class="relative input-hour">
                        <select class="card-expiry-month select-simple form-left-element form-center-element" data-container="body">
                            <c:forEach var="month" begin="1" end="12">
                                <fmt:formatNumber value="${month}" minIntegerDigits="2" var="month"/>
                                <option>${month}</option>>
                            </c:forEach>
                        </select>
                        <span class="explanation-select"><fmt:message key="Month"/></span>
                    </span><span class="relative input-hour">
                        <select class="card-expiry-year select-simple form-right-element form-center-element" data-container="body">
                            <c:forEach var="Year" items="${Years}">
                                <option>${Year}</option>
                            </c:forEach>
                        </select>
                        <span class="explanation-select"><fmt:message key="Year"/></span>
                    </span>

                    <fmt:message key="CVC" var="placeholder"/>
                    <input class="card-cvc form-control form-bottom-element" type="tel" maxlength="3" data-valid-chars="[0-9]" placeholder="${placeholder}"/>

                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Book"/></button>
                    <a class="btn btn-primary btn-block unit" href="${Booking.abortUrl}"><fmt:message key="Cancel"/></a>

                </form>
            </div>
        </div>

    </div>
</div>

<jsp:include page="/WEB-INF/jsp/bookings/include/paymill.jsp"/>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>