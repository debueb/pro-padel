<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        
        <ol class="unit-2 breadcrumb">
            <li><a class="ajaxify" href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a class="ajaxify" href="/admin/bookings"><fmt:message key="Bookings"/></a></li>
            <li class="active"><fmt:message key="PayPal"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>PayPal</h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <spf:input type="hidden" path="id"/>
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>

                    <div class="relative">
                        <spf:select path="payPalEndpoint" class="select-simple form-control" data-style="form-top-element">
                            <spf:options items="${EndPoints}"/>
                        </spf:select>
                        <span class="explanation-select">EndPoint</span>
                    </div>
                    <div class="relative">
                        <spf:input path="clientId" type="text" class="form-control form-center-element"/>
                        <span class="explanation">Client ID</span>
                    </div>
                    <div class="relative">
                        <spf:input path="clientSecret" type="text" class="form-control form-bottom-element"/>
                        <span class="explanation">Client Secret</span>
                    </div>
                    <spf:checkbox path="active" id="active"/>
                    <label class="checkbox" for="active"><fmt:message key="Active"/></label>

                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div></div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
