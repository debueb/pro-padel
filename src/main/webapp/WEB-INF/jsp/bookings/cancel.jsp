<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="CancelBooking2"/></h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Booking">
                    <div class="alert alert-danger">${error}</div>

                    <jsp:include page="include/booking-data.jsp"/>

                    <c:if test="${empty error}">
                        <div class="row">
                            <div class="col-xs-12 unit">
                                <button class="btn btn-primary btn-block" type="submit"><fmt:message key="CancelBooking2"/></button>
                                <a class="btn btn-primary btn-block btn-back"><fmt:message key="Cancel"/></a>
                            </div>
                        </div>
                    </c:if>
                </spf:form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
