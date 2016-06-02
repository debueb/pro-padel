<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

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
                        <div class="alert alert-success" role="alert"><fmt:message key="BookingSuccessMessage"><fmt:param value="${sessionScope.booking.player.email}"/></fmt:message></div>
                        <c:if test="${sessionScope.booking.paymentMethod == 'Cash'}"><div class="alert alert-success" role="alert"><fmt:message key="BookingCashMessage"/></div></c:if>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
