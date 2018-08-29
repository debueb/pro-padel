<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-lg-10 col-lg-offset-1">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/players"><fmt:message key="Players"/></a></li>
            <li><a href="/admin/players/edit/${Player.id}">${Player}</a></li>
            <li class="active"><fmt:message key="Balance"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="BalanceOf"><fmt:param value="${Player}"/></fmt:message></h4>
            </div>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered">
                        <thead>
                        <th><fmt:message key="Date"/></th>
                        <th><fmt:message key="Comment"/></th>
                        <th class="text-align-right"><fmt:message key="Amount"/></th>
                        </thead>
                        <tbody>
                            <c:forEach var="Transaction" items="${Transactions}">
                                <c:set var="editUrl" value="/admin/players/${Player.UUID}/transactions/${Transaction.id}"/>
                                <tr>
                                    <td><a class="block" href="${editUrl}"><joda:format value="${Transaction.date}" pattern="EE, dd. MM yyyy"/></a></td>
                                    <td><a class="block" href="${editUrl}">${Transaction.comment}</a></td>
                                    <td class="text-align-right"><a class="block" href="${editUrl}"><fmt:formatNumber value="${Transaction.amount}" minIntegerDigits="2" minFractionDigits="2" maxFractionDigits="2"/></a></td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td></td>
                                <td class="text-align-right"><b><fmt:message key="BalanceTotal"/></b></td>
                                <td class="text-align-right"><b><fmt:formatNumber value="${Player.balance}" minIntegerDigits="2" minFractionDigits="2" maxFractionDigits="2"/><b></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <a href="/admin/players/${Player.UUID}/transactions/add" class="btn btn-primary btn-block unit"><fmt:message key="AddBalanceEntry"/></a>
            </div>
        </div>
    </div>
</div>


<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>