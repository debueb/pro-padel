<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-6 col-sm-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <h1><fmt:message key="ResetPasswordSuccessfulTitle"/></h1>
        </div>

        <spf:form class="form-signin" role="form" modelAttribute="Model">
            <div class="alert alert-success"><fmt:message key="ResetPasswordSuccessfulMessage"/></div>
        </spf:form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
