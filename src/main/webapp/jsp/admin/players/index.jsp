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

                <div class="">
                    <table class="table table-striped table-bordered datatable">
                        <thead>
                        <th><fmt:message key="FirstName"/></th>
                        <th><fmt:message key="LastName"/></th>
                        <th><fmt:message key="EmailAddress"/></th>
                        <th><fmt:message key="PhoneNumber"/></th>
                        <th><fmt:message key="Registered"/></th>
                        <th class="delete"><fmt:message key="Delete"/></th>
                        </thead>
                        <tbody>
                            <c:forEach var="Player" items="${Models}">
                                <c:set var="editUrl" value="/admin/players/edit/${Player.id}"/>
                                <tr>
                                    <td><a class="block ajaxify" href="${editUrl}">${Player.firstName}</a></td>
                                    <td><a class="block ajaxify" href="${editUrl}">${Player.lastName}</a></td>
                                    <td><a class="block ajaxify" href="${editUrl}">${Player.email}</a></td>
                                    <td><a class="block ajaxify" href="${editUrl}">${Player.phone}</a></td>
                                    <td class="text-center"><a type="btn btn-primary" class="fa ajaxify ${empty Player.passwordHash ? 'fa-close' : 'fa-check'}" href="${editUrl}"></a></td>
                                    <td class="delete"><a href="/admin/players/${Player.id}/delete" type="btn btn-primary" class="fa fa-minus-circle ajaxify"></a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <a href="/admin/players/add" class="btn btn-primary unit ajaxify"><fmt:message key="NewPlayer"/></a>
            </div>
        </div>
    </div>
</div>


<jsp:include page="/jsp/include/footer.jsp"/>
