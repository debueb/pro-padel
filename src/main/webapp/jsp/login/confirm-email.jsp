<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-6 col-sm-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <h1><fmt:message key="RegistrationEmailVerified"/></h1>
        </div>

        <div class="alert alert-success"><fmt:message key="EmailSuccessfullyVerified"/></div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
