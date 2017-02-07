<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${title}</h4>
            </div>
            <div class="panel-body">
                <div class="alert alert-info unit">
                    <fmt:message key="LoginRequiredForThisFeature"/>
                </div>
                <a class="btn btn-primary btn-block unit" href="/login?redirect=${redirectURL}"><fmt:message key="Login"/></a>
                <a class="btn btn-primary btn-block" href="/login/register?redirect=${redirectURL}"><fmt:message key="Register"/></a>
                <a class="btn btn-primary btn-block btn-back"><fmt:message key="Cancel"/></a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>