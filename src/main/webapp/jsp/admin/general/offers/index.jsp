<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Offers"/></h4>
            </div>
        </div>


        <div class="table-responsive">
            <table class="table table-striped table-bordered">
                <thead>
                <th><fmt:message key="Name"/></th>
                <th class="delete"><fmt:message key="Delete"/></th>
                </thead>
                <tbody>
                    <c:forEach var="Model" items="${Models}">
                        <tr>
                            <td><a class="ajaxify" href="/admin/general/offers/edit/${Model.id}">${Model.name}</a></td>
                            <td class="delete"><a href="/admin/general/offers/${Model.id}/delete" type="btn btn-primary" class="fa fa-minus-circle ajaxify"></a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <a href="/admin/general/offers/add" class="btn btn-primary unit ajaxify"><fmt:message key="NewOffer"/></a>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
