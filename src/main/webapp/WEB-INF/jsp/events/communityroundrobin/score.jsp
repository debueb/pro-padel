<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <jsp:include page="/WEB-INF/jsp/events/include/info.jsp"/>
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="ResultsIn"><fmt:param>${Event.name}</fmt:param></fmt:message></h4>
            </div>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-bordered table-scores">
                        <thead>
                            <th class="text-center">#</th>
                            <th><fmt:message key="Community"/></th>
                            <th class="text-center"><fmt:message key="Matches"/></th>
                            <th class="text-center"><fmt:message key="Sets"/></th>
                            <th class="text-center"><fmt:message key="Games"/></th>
                        </thead>
                        <tbody>
                            <c:forEach var="CommunityScoreMapEntry" items="${CommunityScoreMap}" varStatus="status">
                                <c:set var="Community" value="${CommunityScoreMapEntry.key}"/>
                                <c:set var="ScoreEntry" value="${CommunityScoreMapEntry.value}"/>
                                <tr>
                                    <td class="text-center">${status.index+1}</td>
                                    <td>${Community}</td>
                                    <td class="text-center">${ScoreEntry.matchesWon}:${ScoreEntry.matchesPlayed-ScoreEntry.matchesWon}</td>
                                    <td class="text-center">${ScoreEntry.setsWon}:${ScoreEntry.setsPlayed-ScoreEntry.setsWon}</td>
                                    <td class="text-center">${ScoreEntry.gamesWon}:${ScoreEntry.gamesPlayed-ScoreEntry.gamesWon}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
