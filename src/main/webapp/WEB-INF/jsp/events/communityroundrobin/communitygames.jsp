<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <jsp:include page="/WEB-INF/jsp/events/include/info.jsp"/>


        <div class="panel panel-info unit">
            <div class="panel-heading">
                <h4><fmt:message key="Games"/></h4>
            </div>
            <div class="panel-body">
                <c:set var="redirectUrl" value="events/event/${Model.id}/communitygames"/>
                <a href="/events/edit/${Model.id}/addcommunitygame?redirectUrl=${redirectUrl}" class="btn btn-primary btn-block unit-2"><fmt:message key="AddGame"/></a>
                <c:forEach var="CommunityGameMapEntry" items="${CommunityGameMap}">
                    <c:set var="Communities" value="${CommunityGameMapEntry.key}"/>
                    <c:set var="GamesResultMap" value="${CommunityGameMapEntry.value}"/>
                    <div class="table-responsive unit-4">
                        <table class="table table-fixed table-bordered table-striped table-hover table-condensed">
                            <thead>
                                <th class="text-center">${Communities[0]}</th>
                                <th class="text-center"><fmt:message key="GameResult"/></th>
                                <th class="text-center">${Communities[1]}</th>
                                <th class="delete"><fmt:message key="Delete"/></th>
                            </thead>
                            <tbody>
                                <c:forEach var="GameResultMapEntry" items="${GamesResultMap}">
                                    <c:set var="Game" value="${GameResultMapEntry.key}"/>
                                    <c:set var="GameResult" value="${GameResultMapEntry.value}"/>
                                    <tr>
                                        <c:forEach var="Participant" items="${Game.participants}">
                                            <c:if test="${Participant.community eq Communities[0]}">
                                                <td class="text-center">${Participant}</td>
                                            </c:if>
                                        </c:forEach>
                                        <td class="text-center">${GameResult}</td>
                                        <c:forEach var="Participant" items="${Game.participants}">
                                            <c:if test="${Participant.community eq Communities[1]}">
                                                <td class="text-center">${Participant}</td>
                                            </c:if>
                                        </c:forEach>
                                        <td class="text-center"><a href="/admin/events/${Model.id}/game/${Game.id}/delete?redirectUrl=${redirectUrl}"><i class="fa fa-minus-circle"></i></a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
