<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/${path}include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="${gender}_${category}_ranking"/></h4>
            </div>
            <div class="panel-body">

                <c:choose>
                    <c:when test="${empty Rankings}">
                        <div class="alert alert-info"><fmt:message key="NoRankingsYet"/></div>
                    </c:when>
                    <c:otherwise>
                        <div>
                            <table class="table table-scores">
                                <thead>
                                <th class="text-center">#</th>
                                <th><fmt:message key="Name"/></th>
                                <th class="text-center"><fmt:message key="Points"/></th>
                                </thead>
                                <tbody>
                                    <c:set var="req" value="${pageContext.request}" />
                                    <c:forEach var="Ranking" items="${Rankings}" varStatus="status">
                                        <c:set var="Participant" value="${Ranking.key}"/>
                                        <c:set var="RowClass" value="${not empty SelectedParticipant and Participant eq SelectedParticipant ? 'selected-participant' : ''}"/>
                                        <c:if test="${empty sessionScope.customer}">
                                            <c:set var="hostPrefix" value="${req.scheme}://${Participant.customer.domainName}:${req.serverPort}"/>
                                        </c:if>
                                        <tr>
                                            <td class="text-center ${RowClass}">${status.index+1}</td>
                                            <td class="${RowClass}"><a href="${hostPrefix}${Participant.discriminatorValue == 'Player' ? '/players/player/' : '/teams/team/'}${Participant.UUID}" class="ajaxify">${Participant}</a></td>
                                            <td class="text-center ${RowClass}"><fmt:formatNumber value="${Ranking.value}" minFractionDigits="2" maxFractionDigits="2"/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>


        <jsp:include page="/jsp/ranking/include/links.jsp"/>

    </div>
</div>

<jsp:include page="/jsp/${path}include/footer.jsp"/>
