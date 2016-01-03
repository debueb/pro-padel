<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="ResetPassword"/></h4>
            </div>
        </div>


        <spf:form class="form-signin" role="form" modelAttribute="Model">
            <div class="alert alert-danger"><spf:errors path="*"/></div>
            <spf:input path="email" type="hidden"/>
            <fmt:message key="NewPassword" var="placeholder"/>
            <spf:input path="password" type="password"  class="form-control" placeholder="${placeholder}"/>
            <button class="btn btn-primary btn-block unit" type="submit"><fmt:message key="ResetPassword"/></a>
            </spf:form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
