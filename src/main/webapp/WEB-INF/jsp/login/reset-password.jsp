<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="ResetPassword"/></h4>
            </div>
            <div class="panel-body">
                <spf:form class="form-signin" role="form" modelAttribute="Model">
                    <div class="alert alert-danger"><spf:errors path="*"/></div>
                    <spf:input path="email" type="hidden"/>
                    <fmt:message key="NewPassword" var="placeholder"/>
                    <spf:input path="password" type="password"  class="form-control" placeholder="${placeholder}"/>
                    <button class="btn btn-primary btn-block unit" type="submit"><fmt:message key="ResetPassword"/></a>
                </spf:form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
