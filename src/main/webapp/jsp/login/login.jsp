<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-6 col-lg-4 col-sm-offset-3 col-lg-offset-4">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <h1><fmt:message key="Login"/></h1>
        </div>

        <spf:form class="form-signin" role="form" modelAttribute="Model">
            <div class="alert alert-danger"><spf:errors path="*"/></div>
            <fmt:message key="EmailAddress" var="EmailAddress"/>
            <fmt:message key="Password" var="Password"/>
            <spf:input path="email" type="email"  class="form-control form-top-element" placeholder="${EmailAddress}"></spf:input>
            <spf:input path="password" type="password" class="form-control form-bottom-element" placeholder="${Password}"></spf:input>
            <input type="checkbox" name="stay-logged-in" id="stay-logged-in" checked/>
            <label class="checkbox" for="stay-logged-in"><fmt:message key="StayLoggedIn"/></label>

            <button class="btn btn-primary btn-block btn-form-submit" type="submit" data-href="/login"><fmt:message key="Login"/></button>
            <button class="btn btn-primary btn-block btn-form-submit" type="submit" data-href="/login/pre-register"><fmt:message key="Register"/></a>
            <button class="btn btn-primary btn-block btn-form-submit" type="submit" data-href="/login/forgot-password"><fmt:message key="ForgotPassword"/></a>
      </spf:form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
