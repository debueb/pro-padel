<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="AllPlayers"/></h4>
            </div>
            <div class="panel-body">
                <jsp:include page="/jsp/admin/include/search.jsp"/>
                <div class="table-responsive">
                    <table class="table table-striped table-bordered">
                        <thead>
                        <th><fmt:message key="FirstName"/></th>
                        <th><fmt:message key="LastName"/></th>
                        <th><fmt:message key="EmailAddress"/></th>
                        <th><fmt:message key="PhoneNumber"/></th>
                        <th class="text-center"><fmt:message key="SendMail"/></th>
                        <th class="text-center"><fmt:message key="Registered"/></th>
                        <th class="delete"><fmt:message key="Delete"/></th>
                        </thead>
                        <tbody>
                            <c:forEach var="Player" items="${Page.content}">
                                <c:set var="editUrl" value="/admin/players/edit/${Player.id}"/>
                                <tr>
                                    <td><a class="block ajaxify" href="${editUrl}">${Player.firstName}</a></td>
                                    <td><a class="block ajaxify" href="${editUrl}">${Player.lastName}</a></td>
                                    <td><a class="block ajaxify" href="${editUrl}">${Player.email}</a></td>
                                    <td><a class="block ajaxify" href="${editUrl}">${Player.phone}</a></td>
                                    <td><a class="block text-center" href="mailto:${Player.email}"><i class="fa fa-envelope"></i></a></td>
                                    <td class="text-center"><a type="btn btn-primary" class="fa ajaxify ${empty Player.passwordHash ? 'fa-close' : 'fa-check'}" href="${editUrl}"></a></td>
                                    <td class="delete"><a href="/admin/players/${Player.id}/delete" type="btn btn-primary" class="fa fa-minus-circle ajaxify"></a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <jsp:include page="/jsp/admin/include/pagination.jsp"/>
                <a href="/admin/players/add" class="btn btn-primary btn-block unit ajaxify"><fmt:message key="NewPlayer"/></a>
                <a href="/admin/players/mail" class="btn btn-primary btn-block unit ajaxify"><fmt:message key="MailAllPlayers"/></a>
            </div>
        </div>
    </div>
</div>


<jsp:include page="/jsp/include/footer.jsp"/>
