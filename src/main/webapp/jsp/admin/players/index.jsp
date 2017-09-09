<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-lg-10 col-lg-offset-1">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li class="active"><fmt:message key="Players"/></li>
        </ol>

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
                        <th><fmt:message key="PhoneNumber"/></th>
                        <th><fmt:message key="EmailAddress"/></th>
                        <th class="text-center"><fmt:message key="Registered"/></th>
                        <th class="text-center"><fmt:message key="AllowEmailContact"/></th>
                        <th class="text-center"><fmt:message key="SendMail"/></th>
                        <th class="delete"><fmt:message key="Delete"/></th>
                        </thead>
                        <tbody>
                            <c:forEach var="Player" items="${Page.content}">
                                <c:set var="editUrl" value="/admin/players/edit/${Player.id}"/>
                                <tr>
                                    <td><a class="block" href="${editUrl}">${Player.firstName}</a></td>
                                    <td><a class="block" href="${editUrl}">${Player.lastName}</a></td>
                                    <td><a class="block" href="${editUrl}">${Player.phone}</a></td>
                                    <td><a class="block" href="${editUrl}">${Player.email}</a></td>
                                    <td><a type="btn btn-primary" class="fa block text-center ${empty Player.passwordHash ? 'fa-close' : 'fa-check'}" href="${editUrl}"></a></td>
                                    <td><a type="btn btn-primary" class="fa block text-center ${empty Player.allowEmailContact ? 'fa-close' : Player.allowEmailContact ? 'fa-check' : 'fa-close'}" href="${editUrl}"></a></td>
                                    <td><a class="block text-center" href="/admin/mail/player/${Player.UUID}"><i class="fa fa-envelope"></i></a></td>
                                    <td class="delete"><a href="/admin/players/${Player.id}/delete" type="btn btn-primary" class="fa fa-minus-circle"></a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <jsp:include page="/jsp/admin/include/pagination.jsp"/>
                <a href="/admin/players/add" class="btn btn-primary btn-block unit"><fmt:message key="NewPlayer"/></a>
                <a href="/admin/mail/all" class="btn btn-primary btn-block unit"><fmt:message key="MailAllPlayers"/></a>
            </div>
        </div>
    </div>
</div>


<jsp:include page="/jsp/include/footer.jsp"/>
