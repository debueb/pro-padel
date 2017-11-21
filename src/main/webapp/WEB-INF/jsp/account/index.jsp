<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="Account"/></h4></div>
            <div class="panel-body">
                <div class="list-group">
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/account/profile"/>
                        <jsp:param name="key" value="MyProfile"/>
                        <jsp:param name="icon" value="wrench"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/account/bookings"/>
                        <jsp:param name="key" value="MyBookings"/>
                        <jsp:param name="icon" value="book"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/account/games"/>
                        <jsp:param name="key" value="MyGames"/>
                        <jsp:param name="icon" value="soccer-ball-o"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/ranking/${user.UUID}/history"/>
                        <jsp:param name="key" value="MyRanking"/>
                        <jsp:param name="icon" value="line-chart"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/account/changepassword"/>
                        <jsp:param name="key" value="ChangePassword"/>
                        <jsp:param name="icon" value="lock"/>
                    </jsp:include>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
