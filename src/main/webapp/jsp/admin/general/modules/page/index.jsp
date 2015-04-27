<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header">
            <h1><fmt:message key="Entries"/></h1>
        </div>

        <div class="table-responsive">
            <table class="table table-striped table-bordered">
                <thead>
                    <th></th>
                    <th><fmt:message key="Date"/></th>
                    <th><fmt:message key="Title"/></th>
                    <th class="delete"><fmt:message key="Delete"/></th>
                </thead>
                <tbody class="table-sortable">
                    <c:forEach var="Model" items="${Models}">
                        <c:set var="editUrl" value="/admin/general/modules/page/${Module.id}/edit/${Model.id}"/>
                        <tr data-id="${Model.id}">
                            <td class="sortable-handle"><i class="fa fa-arrows-v"></i></td>
                            <td><a class="ajaxify" href="${editUrl}">${Model.lastModified}</a></td>
                            <td><a class="ajaxify" href="${editUrl}">${Model.title}</a></td>
                            <td class="delete"><a href="/admin/general/modules/page/${Module.id}/${Model.id}/delete" class="fa fa-minus-circle ajaxify"></a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <a href="/admin/general/modules/page/${Module.id}/add" class="btn btn-primary btn-block unit ajaxify"><fmt:message key="AddEntry"/></a>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
