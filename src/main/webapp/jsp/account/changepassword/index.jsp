<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-6 col-lg-4 col-sm-offset-3 col-lg-offset-4">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="ChangePassword"/></h4>
            </div>
            <div class="panel-body">
                <spf:form class="form-signin" role="form" modelAttribute="Model" method="POST" >
                    <div class="alert alert-danger"><spf:errors path="*"/></div>
                    <fmt:message key="CurrentPassword" var="CurrentPassword"/>
                    <fmt:message key="NewPassword" var="NewPassword"/>
                    <fmt:message key="NewPasswordRepeat" var="NewPasswordRepeat"/>
                    <spf:input path="oldPass" type="password"  class="form-control form-top-element" placeholder="${CurrentPassword}"></spf:input>
                    <spf:input path="newPass" type="password" class="form-control form-center-element" placeholder="${NewPassword}"></spf:input>
                    <spf:input path="newPassRepeat" type="password" class="form-control form-bottom-element" placeholder="${NewPasswordRepeat}"></spf:input>

                        <button class="btn btn-primary btn-block unit-2" type="submit" ><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
