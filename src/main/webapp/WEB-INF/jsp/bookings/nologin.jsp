<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="BookWithoutLogin"/></h4></div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <div class="alert alert-danger"><spf:errors path="*"/></div>
                    <c:if test="${showResetPasswordLink}">
                        <div class="alert alert-info">
                            <a href="/login/forgot-password"><fmt:message key="ResetPassword"/></a>
                        </div>
                        <div class="unit-2"></div>
                    </c:if>
                    <spf:input path="id" type="hidden"/>
                    <jsp:include page="/WEB-INF/jsp/include/player-input.jsp"/>
                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="ContineWithBooking"/></button>
                </spf:form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
