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
            <li class="active"><fmt:message key="Draws"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Draws"/> ${Event.name}</h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>

                    <c:forEach var="GroupParticipant" items="${Model.groupParticipants}" varStatus="status">
                        <c:set var="GroupNumber" value="${GroupParticipant.key}"/>
                        <c:set var="ParticipantList" value="${GroupParticipant.value}"/>
                        <fmt:message key="ParticipantsInGroupNumber" var="Participants"><fmt:param value="${GroupNumber+1}"/></fmt:message>
                            <div class="relative">
                            <spf:select path="groupParticipants[${GroupNumber}]" class="select-multiple show-tick form-control" data-style="${fn:length(Model.groupParticipants) eq 1 ? '' : status.first ? 'form-top-element' : status.last ?  'form-bottom-element': 'form-center-element'}" title="${Participants}" multiple="true" data-live-search="true" data-container="body">
                                <spf:options items="${Event.participants}" itemValue="UUID"/>
                            </spf:select>
                            <div class="explanation-select">${Participants}</div>
                        </div>

                    </c:forEach>


                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>    
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
