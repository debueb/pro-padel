<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header">
            <h1><fmt:message key="AdminGroups"/></h1>
        </div>

        <div class="table-responsive">
            <table class="table table-striped table-bordered">
                <thead>
                    <th><fmt:message key="GroupName"/></th>
                    <th><fmt:message key="Members"/></th>
                    <th class="delete"><fmt:message key="Delete"/></th>
                </thead>
                <tbody>
                    <c:forEach var="AdminGroup" items="${Models}">
                    <tr>
                        <td><a class="ajaxify" href="/admin/admingroups/edit/${AdminGroup.id}">${AdminGroup.name}</a></td>
                        <td>
                            <c:forEach var="Player" items="${AdminGroup.players}" varStatus="status">
                                <a class="ajaxify" href="/admin/players/edit/${Player.id}">${Player.displayName}</a>${status.last ? "" : ", "}
                            </c:forEach>
                        </td>
                        <td class="delete"><a href="/admin/admingroups/${AdminGroup.id}/delete" class="fa fa-minus-circle ajaxify"></a></td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <a href="/admin/admingroups/add" class="btn btn-primary unit ajaxify"><fmt:message key="NewAdminGroup"/></a>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
