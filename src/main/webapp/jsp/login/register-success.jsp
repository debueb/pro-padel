<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-6 col-sm-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="RegistrationSuccessfulTitle"/></h4>
            </div>
        </div>


        <spf:form class="form-signin" role="form" modelAttribute="Model">
            <div class="alert alert-success"><fmt:message key="RegistrationSuccessfulMessage"/></div>
        </spf:form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
