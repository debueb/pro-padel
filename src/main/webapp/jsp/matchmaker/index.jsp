<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="MatchMaker"/></h4></div>
            <div class="panel-body"><fmt:message key="MatchMakerInfo"/></div>
        </div>
        <div class="list-group unit">
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/account/profile"/>
                <jsp:param name="key" value="MyProfile"/>
                <jsp:param name="icon" value="wrench"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/matchmaker/notificationsettings"/>
                <jsp:param name="key" value="NotificationSettings"/>
                <jsp:param name="icon" value="envelope-o"/>
            </jsp:include>
            <c:if test="${not empty PersonalOffers}">
                <jsp:include page="/jsp/include/list-group-item.jsp">
                    <jsp:param name="href" value="/matchmaker/offers"/>
                    <jsp:param name="key" value="MyOffers"/>
                    <jsp:param name="icon" value="list"/>
                </jsp:include>
            </c:if>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/matchmaker/offers/edit"/>
                <jsp:param name="key" value="NewMatchOffer"/>
                <jsp:param name="icon" value="plus"/>
            </jsp:include>
        </div>
        <hr>
        <h4><fmt:message key="CurrentMatchOffers"/></h4>
        <div class="list-group unit">
            <c:forEach var="Model" items="${Models}">
                <a href="/matchmaker/offers/edit/${Model.id}" class="list-group-item ajaxify">
                    <div class="list-item-text">${Model}</div>
                </a>
            </c:forEach>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>