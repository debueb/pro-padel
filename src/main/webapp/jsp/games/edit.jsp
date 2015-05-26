<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="GameResultIn"><fmt:param value="${Game.event.name}"/></fmt:message></h4>
                </div>
                <div class="panel-body">

                    <div class="alert alert-danger">${error}</div>
                <form method="POST">
                    <table style="width: 100%;" class="table-editgame table-fixed">
                        <thead>
                        <th></th>
                            <c:forEach var="Participant" items="${Game.participants}">
                            <th class="text-center">${Participant}</th>
                            </c:forEach>
                        </thead>
                        <tbody>
                            <c:forEach begin="1" end="3" step="1" var="set">
                                <tr>
                                    <td>${set}. <fmt:message key="Set"/><c:if test="${set == 3}"> Champions TieBreak</c:if></td>
                                    <c:forEach var="Participant" items="${Game.participants}">
                                        <td>
                                            <c:set var="key" value="game-${Game.id}-participant-${Participant.id}-set-${set}"/>
                                            <select name="${key}" class="select-simple form-control">
                                                <option value="-1">-</option>
                                                <c:choose>
                                                    <c:when test="${set < 3}">
                                                        <c:set var="end" value="7"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="end" value="1"/>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:forEach begin="0" end="${end}" step="1" var="current">
                                                    <option value="${current}" ${GamesMap[key].setGames == current ? 'selected' : ''}>${current}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </c:forEach>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <button type="submit" class="btn btn-primary btn-block unit"><fmt:message key="Save"/></button>
                </form>
            </div></div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
