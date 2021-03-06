<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">

        <div class="page-header"></div>

        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="BookCourt"/></h4></div>
            <div class="panel-body">
                <c:choose>
                    <c:when test="${not empty error}">
                        <div class="alert alert-danger" role="alert">${error}</div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-success" role="alert">
                            <fmt:message key="BookingSuccessMessage"><fmt:param value="${sessionScope.booking.player.email}"/></fmt:message>
                            <c:if test="${sessionScope.booking.paymentMethod == 'Cash'}"><br/><fmt:message key="BookingCashMessage"/></c:if>
                            <c:if test="${sessionScope.booking.paymentMethod == 'ExternalVoucher'}"><br/><fmt:message key="BookingExternalVoucherMessage"/></c:if>
                        </div>
                        <div class="unit-4">
                            <span style="height: 32px; line-height: 32px; padding-right: 10px; float: left;">
                                <fmt:message key="ShareVia"/>
                            </span>
                            <jsp:include page="/WEB-INF/jsp/include/share.jsp"/>
                            <a class="btn btn-primary btn-block unit-2" href="/home"><fmt:message key="GoToHomepage"/></a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
