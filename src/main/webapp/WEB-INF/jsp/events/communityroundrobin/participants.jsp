<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        
        <jsp:include page="/WEB-INF/jsp/events/include/info.jsp"/>
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Communities"/></h4>
            </div>
            <div class="panel-body">
                <div class="list-group">
                    <c:if test="${empty Model.communities}">
                        <div class="alert alert-info"><fmt:message key="NoParticipants"/></div>
                    </c:if>
                    <c:forEach var="Community" items="${Model.communities}">
                        <table class="table table-fixed table-bordered table-striped table-hover table-condensed">
                            <thead>
                                <th class="text-center"><a href="/admin/communities/edit/${Community.id}?redirectUrl=/events/event/${Model.id}/communities">${Community.name}</a></th>
                            </thead>
                            <tbody>
                                <c:forEach var="Player" items="${Community.players}">
                                    <tr>
                                        <td><a href="/players/player/${Player.UUID}">${Player}</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
