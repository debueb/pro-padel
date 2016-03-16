<%@include file="/jsp/include/include.jsp"%>
<fmt:message key="AllGames" var="msg"/>
<div class="list-item-text">
    <div class="text">
        <c:forEach var="Participant" items="${Game.participants}" varStatus="status">
            <div style="display:block;">${Participant}</div>
            <c:if test="${not status.last}">
                <div class="list-group-item-vs">vs</div>
            </c:if>
        </c:forEach>
    </div>
    <div class="badge-container">
        <div class="badge">
            <c:choose>
                <c:when test="${not empty GameResultMap[Game]}">
                    ${GameResultMap[Game]}
                </c:when>
                <c:otherwise>
                    -:- -:-
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>