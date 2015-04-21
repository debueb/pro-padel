<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <h1><fmt:message key="Account"/></h1>
        </div>

        <div class="list-group">
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/account/profile"/>
                <jsp:param name="key" value="MyProfile"/>
                <jsp:param name="icon" value="wrench"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/account/bookings"/>
                <jsp:param name="key" value="MyBookings"/>
                <jsp:param name="icon" value="book"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/account/changepassword"/>
                <jsp:param name="key" value="ChangePassword"/>
                <jsp:param name="icon" value="lock"/>
            </jsp:include>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
