<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <ol class="unit-2 breadcrumb">
            <li><a class="ajaxify" href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a class="ajaxify" href="/admin/general"><fmt:message key="General"/></a></li>
            <li class="active"><fmt:message key="AdminGroups"/></li>
        </ol>
        
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="AdminGroups"/></h4>
            </div>
            <div class="panel-body">
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
                                    <td><a class="ajaxify" href="/admin/general/admingroups/edit/${AdminGroup.id}">${AdminGroup.name}</a></td>
                                    <td>
                                        <c:forEach var="Player" items="${AdminGroup.players}" varStatus="status">
                                            <a class="ajaxify" href="/admin/players/edit/${Player.id}">${Player}</a>${status.last ? "" : ", "}
                                        </c:forEach>
                                    </td>
                                    <td class="delete"><a href="/admin/general/admingroups/${AdminGroup.id}/delete" class="fa fa-minus-circle ajaxify"></a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <a href="/admin/general/admingroups/add" class="btn btn-primary btn-block unit ajaxify"><fmt:message key="NewAdminGroup"/></a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
