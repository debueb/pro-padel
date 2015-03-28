<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header">
            <h1>${title}</h1>
            <c:if test="${not empty subtitle}">
                <h1>${subtitle}</h1>
            </c:if>
        </div>

        <div class="list-group">
            <c:forEach var="Game" items="${Games}">
                <a href="/games/game/${Game.id}" class="list-group-item ajaxify">
                    <c:set var="Game" value="${Game}" scope="request"/>
                    <jsp:include page="/jsp/games/game-result.jsp"/>  
                </a>
            </c:forEach>
        </div>
        
            <c:if test="${not empty Event}">
                <div class="unit list-group">
                    <a href="/scores/event/${Event.id}" class="list-group-item ajaxify">
                        <div class="list-item-text"><fmt:message key="ResultsIn"><fmt:param value="${Event.name}"/></fmt:message></div>
                    </a>
                </div>

            </c:if>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
