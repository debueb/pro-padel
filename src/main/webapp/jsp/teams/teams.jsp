<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${Title}</h4>
            </div>
        </div>


        <div class="list-group">
            <c:forEach var="Participant" items="${Teams}">
                <a href="/teams/team/${Participant.id}" class="list-group-item ajaxify">
                    <div class="list-item-text">${Participant}</div>
                </a>
            </c:forEach>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
