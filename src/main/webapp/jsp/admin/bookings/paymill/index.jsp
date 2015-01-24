<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <h1>PayMill</h1>
        </div>
        
        <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
            <spf:input type="hidden" path="id"/>
            <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>
            <div class="relative">
                <spf:input path="publicApiKey" type="text" class="form-control form-top-element"/>
                <div class="explanation">Public API Key</div>
            </div>
            <div class="relative">
                <spf:input path="privateApiKey" type="text" class="form-control form-bottom-element"/>
                <div class="explanation">Private API Key</div>
            </div>
            <div>
                <spf:checkbox path="enableDirectDebit" id="enableDirectDebit"/>
                <label class="checkbox" for="enableDirectDebit"><fmt:message key="EnableDirectDebit"/></label>
            </div>
            <div>
                <spf:checkbox path="enableCreditCard" id="enableCreditCard"/>
                <label class="checkbox" for="enableCreditCard"><fmt:message key="EnableCreditCard"/></label>
            </div>
            
            <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
      </spf:form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
