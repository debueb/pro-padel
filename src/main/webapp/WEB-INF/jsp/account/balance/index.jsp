<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-lg-10 col-lg-offset-1">
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="MyBalance" /></h4>
            </div>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered">
                        <thead>
                        <th><fmt:message key="Date"/></th>
                        <th><fmt:message key="Comment"/></th>
                        <th><fmt:message key="Amount"/></th>
                        </thead>
                        <tbody>
                            <c:forEach var="Transaction" items="${Transactions}">
                                <tr>
                                    <td><joda:format value="${Transaction.date}" pattern="EE, dd. MM yyyy"/></td>
                                    <td>${Transaction.comment}</td>
                                    <td><fmt:formatNumber value="${Transaction.amount}" minIntegerDigits="2" minFractionDigits="2" maxFractionDigits="2"/></td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td></td>
                                <td style="text-align: right;"><b><fmt:message key="BalanceTotal"/></b></td>
                                <td><b><fmt:formatNumber value="${Player.balance}" minIntegerDigits="2" minFractionDigits="2" maxFractionDigits="2"/><b></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>


<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>