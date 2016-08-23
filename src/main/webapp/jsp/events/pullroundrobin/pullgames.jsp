<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <jsp:include page="/jsp/events/include/info.jsp"/>


        <div class="panel panel-info unit">
            <div class="panel-heading">
                <h4><fmt:message key="Games"/></h4>
            </div>
            <div class="panel-body" style="padding: 0;">
                <table class="table table-responsive table-striped table-fixed">
                    <tbody>
                    <c:forEach var="GameResult" items="${GameResultMap}">
                        <c:set var="Game" value="${GameResult.key}"/>
                        <c:set var="Result" value="${GameResult.value}"/>
                        <tr>
                            <td class="text-right vertical-align-middle">${Game}</td>
                            <td class="vertical-align-middle">
                                <c:choose>
                                    <c:when test="${not empty Result}">${Result}</c:when>
                                    <c:when test="${not empty Game.startDate and not empty Game.startTime}"><joda:format value="${Game.startDate}" pattern="dd. MMM" /> <joda:format value="${Game.startTime}" pattern="HH:mm" /> <fmt:message key="oClock"/></c:when>
                                    <c:otherwise><a href="/admin/events/edit/${Model.id}/pullschedule"><fmt:message key="DatePending"/><a/></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
