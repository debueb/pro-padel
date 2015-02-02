<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-6 col-sm-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <h1><fmt:message key="Profile"/></h1>
        </div>

        <spf:form class="form-signin" role="form" modelAttribute="Model">
            <div class="alert alert-danger"><spf:errors path="*"/></div>
            <spf:input path="id" type="hidden"/>
            <jsp:include page="/jsp/include/player-input.jsp"/>
            <button class="btn btn-primary btn-block unit" type="submit"><fmt:message key="Save"/></button>
      </spf:form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
