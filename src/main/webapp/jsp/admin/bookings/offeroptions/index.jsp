<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>
        
        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/bookings"><fmt:message key="Bookings"/></a></li>
            <li class="active"><fmt:message key="OfferOptions"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="OfferOptions"/></h4>
            </div>
            <div class="panel-body"> 
                <jsp:include page="/jsp/admin/include/search.jsp"/>
                <div class="table-responsive">
                    <table class="table table-striped table-bordered">
                        <thead>
                        <th></th>
                        <th><fmt:message key="Name"/></th>
                        <th><fmt:message key="Type"/></th>
                        <th class="delete"><fmt:message key="Delete"/></th>
                        </thead>
                        <tbody class="table-sortable">
                            <c:forEach var="Model" items="${Models}">
                                <tr data-id="${Model.id}">
                                    <td class="sortable-handle"><i class="fa fa-arrows-v"></i></td>
                                    <td><a href="/${moduleName}/edit/${Model.id}">${Model.name}</a></td>
                                    <td><a href="/${moduleName}/edit/${Model.id}">${Model.offerOptionType}</a></td>
                                    <td class="delete"><a href="/${moduleName}/${Model.id}/delete" type="btn btn-primary" class="fa fa-minus-circle"></a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <jsp:include page="/jsp/admin/include/pagination.jsp"/>
                <a href="/${moduleName}/add" class="btn btn-primary btn-block unit"><fmt:message key="Add"/></a>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/sortable.jsp"/>
<jsp:include page="/jsp/include/footer.jsp"/>
