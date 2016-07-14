<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Customers"/></h4>
            </div>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered">
                        <thead>
                        <th><fmt:message key="Name"/></th>
                        <th><fmt:message key="DomainNames"/></th>
                        <th><fmt:message key="GoogleAnalyticsTrackingId"/></th>
                        <th class="delete"><fmt:message key="Delete"/></th>
                        </thead>
                        <tbody>
                            <c:forEach var="Model" items="${Models}">
                                <tr>
                                    <td><a class="ajaxify" href="/admin/customers/edit/${Model.id}">${Model.name}</a></td>
                                    <td>
                                        <c:forEach var="DomainName" items="${Model.domainNames}" varStatus="status">
                                            <a class="ajaxify" href="/admin/customers/edit/${Model.id}">${DomainName}</a>${status.last ? "" : ", "}
                                        </c:forEach>
                                    </td>
                                    <td><a class="ajaxify" href="/admin/customers/edit/${Model.id}">${Model.googleAnalyticsTrackingId}</a></td>
                                    <td class="delete"><a href="/admin/customers/${Model.id}/delete" type="btn btn-primary" class="fa fa-minus-circle ajaxify"></a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <a href="/admin/customers/add" class="btn btn-primary btn-block unit ajaxify"><fmt:message key="NewCustomer"/></a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
