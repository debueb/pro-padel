<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="AllEvents"/></h4>
            </div>
        </div>


        <div class="table-responsive">
            <table class="table table-striped table-bordered">
                <thead>
                <th><fmt:message key="Name"/></th>
                <th><fmt:message key="Participants"/></th>
                <th class="delete"><fmt:message key="Delete"/></th>
                </thead>
                <tbody>
                    <c:forEach var="Event" items="${Models}">
                        <tr>
                            <td><a class="ajaxify" href="/admin/events/edit/${Event.id}">${Event.name}</a></td>
                            <td>
                                <c:forEach var="Participant" items="${Event.participants}" varStatus="status">
                                    <c:choose>
                                        <c:when test="${Participant.discriminatorValue == 'player'}">
                                            <c:set var="url_participant" value="/admin/players/edit/${Participant.id}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="url_participant" value="/admin/teams/edit/${Participant.id}"/>
                                        </c:otherwise>
                                    </c:choose>
                                    <a class="ajaxify" href="${url_participant}">${Participant}</a>${status.last ? "" : ", "}
                                </c:forEach>
                            </td>
                            <td class="delete"><a href="/admin/events/${Event.id}/delete" type="btn btn-primary" class="fa fa-minus-circle ajaxify"></a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <a href="/admin/events/add" class="btn btn-primary unit ajaxify"><fmt:message key="NewEvent"/></a>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
