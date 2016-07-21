<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Modules"/></h4>
            </div>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                        <th></th>
                        <th><fmt:message key="Title"/></th>
                        <th class="text-center"><fmt:message key="Type"/></th>
                        <th class="text-center"><fmt:message key="ShowOnHomepage"/></th>
                        <th class="text-center"><fmt:message key="ShowInMenu"/></th>
                        <th class="text-center"><fmt:message key="ShowInFooter"/></th>
                        <th class="delete"><fmt:message key="Delete"/></th>
                        </thead>
                        <tbody class="table-sortable">
                            <c:forEach var="Model" items="${Models}">
                                <c:set var="editUrl" value="/admin/general/modules/edit/${Model.id}"/>
                                <tr data-id="${Model.id}">
                                    <td class="sortable-handle"><i class="fa fa-arrows-v"></i></td>
                                    <td><a class="ajaxify" href="${editUrl}">${Model.title}</a></td>
                                    <td class="text-center"><a class="ajaxify" href="${editUrl}">${Model.moduleType}</a></td>
                                    <td class="text-center"><a class="ajaxify" href="${editUrl}"><i class="fa fa-${Model.showOnHomepage ? 'check' : 'remove'}"/></a></td>
                                    <td class="text-center"><a class="ajaxify" href="${editUrl}"><i class="fa fa-${Model.showInMenu ? 'check' : 'remove'}"/></a></td>
                                    <td class="text-center"><a class="ajaxify" href="${editUrl}"><i class="fa fa-${Model.showInFooter ? 'check' : 'remove'}"/></a></td>
                                    <td class="delete"><a href="/admin/general/modules/${Model.id}/delete" class="fa fa-minus-circle ajaxify"></a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <a href="/admin/general/modules/add" class="btn btn-primary btn-block unit ajaxify"><fmt:message key="Add"/></a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
