<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
         <div class="page-header">
            <h1>${title}</h1>
        </div>
        <div class="alert alert-info unit">
            <fmt:message key="LoginRequiredForThisFeature"/>
        </div>
        <a class="btn btn-primary btn-block unit" href="/login?redirect=${playerURL}"><fmt:message key="Login"/></a>
        <a class="btn btn-primary btn-block" href="/login/register?redirect=${playerURL}"><fmt:message key="Register"/></a>
        <a class="btn btn-primary btn-block btn-back"><fmt:message key="Cancel"/></a>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>