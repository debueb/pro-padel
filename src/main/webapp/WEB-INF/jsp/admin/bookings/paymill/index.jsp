<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/bookings"><fmt:message key="Bookings"/></a></li>
            <li class="active"><fmt:message key="PayMill"/></li>
        </ol>
        
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>PayMill</h4>
            </div>
            <div class="panel-body">
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
        </div></div></div>


<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
