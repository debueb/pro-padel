<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="DeleteWarning"/></h4>
            </div>
            <div class="panel-body">
                <div class="alert alert-danger">${error}</div>
                <joda:format value="${Model.bookingDate}" pattern="EE" var="date"/>
                <joda:format value="${Model.bookingTime}" pattern="HH:mm" var="time"/>
                <h4><fmt:message key="AreYouSureYouWantToDeleteTheFollowingBookings"/></h4>
                <c:forEach var="BookingToDelete" items="${BookingsToDelete}">
                    <div>${BookingToDelete}</div>
                </c:forEach>
                <form method="POST">
                    <a class="btn btn-primary btn-back unit"><fmt:message key="Cancel"/></a>
                    <button class="btn btn-danger unit" style="margin-left: 10px;"><fmt:message key="Delete"/></button>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
