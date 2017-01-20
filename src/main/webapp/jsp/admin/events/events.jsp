<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-lg-10 col-lg-offset-1">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <ol class="unit-2 breadcrumb">
            <li><a class="ajaxify" href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a class="ajaxify" href="/admin/events"><fmt:message key="Events"/></a></li>
            <li class="active"><fmt:message key="${status}Events"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="${status}Events"/></h4>
            </div>
            <div class="panel-body">
                <jsp:include page="/jsp/admin/include/search.jsp"/>
                <div class="table-responsive unit-2">
                    <table class="table table-striped table-bordered">
                        <thead>
                        <th><fmt:message key="Name"/></th>
                        <th><fmt:message key="StartDate"/></th>
                        <th><fmt:message key="EventType"/></th>
                        <th class="text-center"><fmt:message key="Participants"/></th>
                        <th class="text-center"><fmt:message key="MaxNumberOfParticipants"/></th>
                        <th class="text-center"><fmt:message key="SendMail"/></th>
                        <th class="delete"><fmt:message key="Delete"/></th>
                        </thead>
                        <tbody>
                            <c:forEach var="Event" items="${Page.content}">
                                <c:set var="url" value="/admin/events/edit/${Event.id}"/>
                                <tr>
                                    <td><a class="ajaxify" href="${url}">${Event.name}</a></td>
                                    <td><a class="ajaxify" href="${url}"><joda:format value="${Event.startDate}" pattern="yyyy-MM-dd"/></a></td>
                                    <td><a class="ajaxify" href="${url}"><fmt:message key="${Event.eventType}"/></a></td>
                                    <td class="text-center">${fn:length(Event.participants)}</td>
                                    <td class="text-center">${Event.maxNumberOfParticipants}</td>
                                    <td><a class="block text-center" href="mailto:${Event.mailTo}"><i class="fa fa-envelope"></i></a></td>
                                    <td class="delete"><a href="/admin/events/${Event.id}/delete" type="btn btn-primary" class="fa fa-minus-circle ajaxify"></a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <jsp:include page="/jsp/admin/include/pagination.jsp"/>
                <a href="/admin/events/add" class="btn btn-primary btn-block unit ajaxify"><fmt:message key="NewEvent"/></a>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/datatables.jsp"/>
<jsp:include page="/jsp/include/footer.jsp"/>