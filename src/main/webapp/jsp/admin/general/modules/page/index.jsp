<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/general"><fmt:message key="General"/></a></li>
            <li><a href="/admin/general/modules"><fmt:message key="Modules"/></a></li>
            <li><a href="/admin/general/modules/edit/${Module.id}">${Module.title}</a></li>
            <li class="active"><fmt:message key="Entries"/></li>
        </ol>
        
        <div class="page-header"></div>
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Entries"/></h4>
            </div>
            <div class="panel-body">
                <div class="table-responsive">
                <table class="table table-striped table-bordered">
                    <thead>
                    <th></th>
                    <th><fmt:message key="Title"/></th>
                    <th><fmt:message key="Date"/></th>
                    <th class="delete"><fmt:message key="Delete"/></th>
                    </thead>
                    <tbody class="table-sortable">
                        <c:forEach var="Model" items="${Models}">
                            <c:set var="editUrl" value="/admin/general/modules/page/${Module.id}/edit/${Model.id}"/>
                            <tr data-id="${Model.id}">
                                <td class="sortable-handle"><i class="fa fa-arrows-v"></i></td>
                                <td><a class="ajaxify" href="${editUrl}">${Model.title}</a></td>
                                <td><a class="ajaxify" href="${editUrl}">${Model.lastModified}</a></td>
                                <td class="delete"><a href="/admin/general/modules/page/${Module.id}/${Model.id}/delete" class="fa fa-minus-circle ajaxify"></a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                </div>
                <a href="/admin/general/modules/page/${Module.id}/add" class="btn btn-primary btn-block unit ajaxify"><fmt:message key="AddEntry"/></a>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/sortable.jsp"/>
<jsp:include page="/jsp/include/footer.jsp"/>
