<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <h1><fmt:message key="BookWithoutLogin"/></h1>
        </div>
        
        <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
            <div class="alert alert-danger"><spf:errors path="*" cssClass="error"/></div>
            <spf:input path="id" type="hidden"/>
            <jsp:include page="/jsp/include/player-input.jsp"/>
            <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="ContineWithBooking"/></button>
        </spf:form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
