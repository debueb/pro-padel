<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-md-10 col-md-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header">
            <h1><fmt:message key="Vouchers"/></h1>
        </div>

        <div class="unit">
            <table class="table table-bordered table-centered datatable-grouped">
                <thead>
                    <tr>
                        <th><fmt:message key="Comment"/></th>
                        <th><fmt:message key="Voucher"/></th>
                        <th><fmt:message key="ValidUntil"/></th>
                        <th><fmt:message key="Duration"/></th>
                        <th><fmt:message key="Offers"/></th>
                        <th><fmt:message key="Used"/></th>
                        <th><fmt:message key="Delete"/></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${Models}" var="Model">
                        <tr>
                            <td>${Model.comment}</td>
                            <td style="text-align: left;">${Model.UUID}</td>
                            <td><joda:format value="${Model.validUntil}" pattern="yyyy-MM-dd" /></td>
                            <td>${Model.duration}</td>
                            <td>${Model.offers}</td>
                            <td><div class="fa ${Model.used ? 'fa-check' : 'fa-close'}"></div></td>
                            <td class="delete"><a href="/admin/bookings/voucher/${Model.id}/delete" type="btn btn-primary ajaxify" class="fa fa-minus-circle"></a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <a href="/admin/bookings/voucher/add" class="btn btn-primary unit ajaxify"><fmt:message key="AddVoucher"/></a>
    </div>
</div>

<jsp:include page="/jsp/include/datatables.jsp"/>
<jsp:include page="/jsp/include/footer.jsp"/>
