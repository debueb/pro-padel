<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Facilities"/></h4>
            </div>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered">
                        <thead>
                        <th><fmt:message key="Name"/></th>
                        <th><fmt:message key="Offers"/></th>
                        <th class="delete"><fmt:message key="Delete"/></th>
                        </thead>
                        <tbody>
                            <c:forEach var="Model" items="${Models}">
                                <tr>
                                    <td><a class="ajaxify" href="/${moduleName}/edit/${Model.id}">${Model.name}</a></td>
                                    <td>
                                        <c:forEach var="Offer" items="${Model.offers}" varStatus="status">
                                            <a class="ajaxify" href="/${moduleName}/edit/${Model.id}">${Offer}</a>${status.last ? "" : ", "}
                                        </c:forEach>
                                    </td>
                                    <td class="delete"><a href="/${moduleName}/${Model.id}/delete" type="btn btn-primary" class="fa fa-minus-circle ajaxify"></a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <a href="/${moduleName}/add" class="btn btn-primary btn-block unit ajaxify"><fmt:message key="NewFacility"/></a>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
