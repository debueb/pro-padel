<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        <jsp:include page="/jsp/events/include/info.jsp"/>
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="ParticipateChoosePaymentMethod"/></h4>
            </div>
            <div class="panel-body">
                <spf:form class="form-signin" method="POST" modelAttribute="Player">
                    <div class="alert alert-info"><fmt:message key="ParticipateChoosePaymentMethodDesc"/></div>
                    <div class="alert alert-danger unit"><spf:errors path="*"/></div>

                    <div class="relative unit-2">
                        <select class="form-control select-simple" name="paymentMethod" data-container="body">
                        <c:forEach var="PaymentMethod" items="${Model.paymentMethods}">
                            <option value="${PaymentMethod}"><fmt:message key="${PaymentMethod}"/></option>
                        </c:forEach>
                        </select>
                        <span class="explanation-select"><fmt:message key="PaymentMethod"/></span>
                    </div>
                    
                    <div>
                        <button class="btn btn-primary btn-block unit-2" type="submit"><fmt:message key="Participate"/></button>
                    </div>
                </spf:form>         
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>