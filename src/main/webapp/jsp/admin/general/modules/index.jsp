<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Modules"/></h4>
            </div>
        </div>


        <div class="table-responsive">
            <table class="table table-striped table-bordered">
                <thead>
                <th></th>
                <th><fmt:message key="Title"/></th>
                <th><fmt:message key="Type"/></th>
                <th><fmt:message key="ShowInMenu"/></th>
                <th><fmt:message key="ShowInFooter"/></th>
                <th class="delete"><fmt:message key="Delete"/></th>
                </thead>
                <tbody class="table-sortable">
                    <c:forEach var="Model" items="${Models}">
                        <c:set var="editUrl" value="/admin/general/modules/edit/${Model.id}"/>
                        <tr data-id="${Model.id}">
                            <td class="sortable-handle"><i class="fa fa-arrows-v"></i></td>
                            <td><a class="ajaxify" href="${editUrl}">${Model.title}</a></td>
                            <td><a class="ajaxify" href="${editUrl}">${Model.moduleType}</a></td>
                            <td><a class="ajaxify" href="${editUrl}"><i class="fa fa-${Model.showInMenu ? 'check' : 'remove'}"/></a></td>
                            <td><a class="ajaxify" href="${editUrl}"><i class="fa fa-${Model.showInFooter ? 'check' : 'remove'}"/></a></td>
                            <td class="delete"><a href="/admin/general/modules/${Model.id}/delete" class="fa fa-minus-circle ajaxify"></a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <a href="/admin/general/modules/add" class="btn btn-primary unit ajaxify"><fmt:message key="Add"/></a>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
