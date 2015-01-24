<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-md-10 col-md-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header">
            <h1><fmt:message key="DeleteWarning"/></h1>
        </div>

        <div class="alert alert-danger">${error}</div>
        
        <h4><fmt:message key="AreYouSureYouWantToDelete"><fmt:param value="${Model.displayName}"/></fmt:message></h4>
        
        <form method="POST">
            <a class="btn btn-primary btn-back unit ajaxify"><fmt:message key="Cancel"/></a>
            <button class="btn btn-primary unit" style="margin-left: 10px;"><fmt:message key="Delete"/></button>
        </form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
