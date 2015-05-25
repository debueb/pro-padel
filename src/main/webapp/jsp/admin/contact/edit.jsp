<%@ page pageEncoding="UTF-8" contentType="text/html" %> 
<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Contact"/></h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <spf:errors path="*" cssClass="error"/>
                    <spf:input path="id" type="hidden"/>
                    <fmt:message key="EmailAddress" var="EmailAddress"/>
                    <fmt:message key="Name" var="Name"/>
                    <spf:input path="emailDisplayName" type="text" class="form-control form-top-element" placeholder="${Name}"/>
                    <spf:input path="emailAddress" type="email"  class="form-control form-bottom-element" placeholder="${EmailAddress}"/>
                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
