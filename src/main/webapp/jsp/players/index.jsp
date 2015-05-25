<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Players"/></h4>
            </div>
        </div>


        <div class="list-group">
            <a href="/players/all" class="list-group-item ajaxify">
                <div class="list-item-text"><fmt:message key="AllPlayers"/></div>
            </a>
            <c:forEach var="Event" items="${Events}">
                <a href="/players/event/${Event.id}" class="list-group-item ajaxify">
                    <div class="list-item-text"><fmt:message key="PlayersIn"><fmt:param>${Event.name}</fmt:param></fmt:message></div>
                        </a>
            </c:forEach>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
