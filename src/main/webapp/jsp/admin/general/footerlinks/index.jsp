<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header">
            <h1><fmt:message key="FooterLinks"/></h1>
        </div>

        <div class="table-responsive">
            <table class="table table-striped table-bordered">
                <thead>
                    <th><fmt:message key="Title"/></th>
                    <th class="delete"><fmt:message key="Delete"/></th>
                </thead>
                <tbody>
                    <c:forEach var="Model" items="${Models}">
                        <c:set var="editUrl" value="/admin/general/footerlinks/edit/${Model.id}"/>
                        <tr>
                            <td><a class="ajaxify" href="${editUrl}">${Model.title}</a></td>
                            <td class="delete"><a href="/admin/general/footerlinks/${Model.id}/delete" class="fa fa-minus-circle ajaxify"></a></td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <a href="/admin/general/footerlinks/add" class="btn btn-primary unit ajaxify"><fmt:message key="AddEntry"/></a>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
