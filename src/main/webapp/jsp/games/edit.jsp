<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header">
            <h1><fmt:message key="GameResult"/></h1>
        </div>
        <div class="alert alert-danger">${error}</div>
        <div class="row">
            <c:forEach var="Participant" items="${Game.participants}" varStatus="status">
                <div class="col-xs-4 list-group-item-text ${status.first ? 'col-xs-offset-3' : ''}">${Participant.displayName}</div>
            </c:forEach>
        </div>
        <form method="POST">
            <c:forEach begin="1" end="3" step="1" var="set">
                <div class="row row-score">
                    <div class="col-xs-3">${set}. <fmt:message key="Set"/></div>
                    <c:forEach var="Participant" items="${Game.participants}">
                        <div class="col-xs-4">
                            <c:set var="key" value="game-${Game.id}-participant-${Participant.id}-set-${set}"/>
                            <select name="${key}" class="select-simple form-control">
                                <option value="-1">-</option>
                                <c:forEach begin="0" end="7" step="1" var="current">
                                    <option value="${current}" ${GamesMap[key].setGames == current ? 'selected' : ''}>${current}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </c:forEach>
                </div>
            </c:forEach>

            <button type="submit" class="btn btn-primary unit"><fmt:message key="Save"/></button>
        </form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
