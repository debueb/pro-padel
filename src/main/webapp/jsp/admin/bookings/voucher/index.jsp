<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>
        
        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/bookings"><fmt:message key="Bookings"/></a></li>
            <li class="active"><fmt:message key="Vouchers"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Vouchers"/></h4>
            </div>
            <div class="panel-body">

                <div class="unit">
                    <table class="table table-bordered table-centered datatable-grouped">
                        <thead>
                            <tr>
                                <th><fmt:message key="Comment"/></th>
                                <th><fmt:message key="Voucher"/></th>
                                <th><fmt:message key="Match"/></th>
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
                                    <td style="text-align: left;"><c:if test="${not empty Model.game}"><a href="/games/game/${Model.game.id}">${Model.game}</a></c:if></td>
                                    <td><joda:format value="${Model.validUntil}" pattern="yyyy-MM-dd" /></td>
                                    <td>${Model.duration}</td>
                                    <td>${Model.offers}</td>
                                    <td><div class="fa ${Model.used ? 'fa-check' : 'fa-close'}"></div></td>
                                    <td class="delete"><a href="/admin/bookings/voucher/${Model.id}/delete" type="btn btn-primary" class="fa fa-minus-circle"></a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <a href="/admin/bookings/voucher/add" class="btn btn-primary btn-block unit"><fmt:message key="AddVoucher"/></a>
                <a href="/admin/bookings/voucher/send" class="btn btn-primary btn-block unit"><fmt:message key="SendVoucher"/></a>
            </div>
        </div></div>
</div>
<jsp:include page="/jsp/include/datatables.jsp"/>
<jsp:include page="/jsp/include/footer.jsp"/>
