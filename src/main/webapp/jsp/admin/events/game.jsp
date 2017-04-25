<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        
        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/events"><fmt:message key="Events"/></a></li>
            <li><a href="/admin/events/edit/${Event.id}">${Event.name}</a></li>
            <li><a href="/admin/events/edit/${Event.id}/draws"><fmt:message key="Draws"/></a></li>
            <li class="active"><fmt:message key="Game"/></li>
        </ol>
        
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${Event}</h4>
            </div>
            <div class="panel-body">
                <form method="POST" role="form" modelAttribute="Model">
                    <input type="hidden" name="id" value="${Model.id}"/>

                    <c:if test="${fn:length(Model.participantList) ge 1}">
                        <select name="participant1" class="select-simple form-control" data-style="form-top-element" data-container="body">
                            <c:forEach var="participant" items="${Event.participants}">
                                <option value="${participant.id}" ${Model.participantList[0].id eq participant.id ? 'selected' : ''}>${participant}</option>
                            </c:forEach>
                        </select>
                    </c:if>

                    <select name="participant2" class="select-simple form-control" data-style="form-bottom-element" data-container="body">
                        <option value="">- Bye -</option>
                        <c:forEach var="participant" items="${Event.participants}">
                            <option value="${participant.id}" ${fn:length(Model.participantList) ge 2 and Model.participantList[1].id eq participant.id ? 'selected' : ''}>${participant}</option>
                        </c:forEach>
                    </select>

                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>

                </form>
            </div>
        </div>    
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
