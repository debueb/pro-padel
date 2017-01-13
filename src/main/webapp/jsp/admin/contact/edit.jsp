<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <ol class="unit-2 breadcrumb">
            <li><a class="ajaxify" href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a class="ajaxify" href="/admin/contact"><fmt:message key="ContactSettings"/></a></li>
            <li class="active"><fmt:message key="Contact"/></li>
        </ol>

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
                    <div class="unit">
                        <spf:checkbox path="notifyOnContactForm"/><label for="notifyOnContactForm1"><fmt:message key="NotifyOnContactForm"/></label>
                    </div>
                    <div class="unit">
                        <spf:checkbox path="notifyOnBooking"/><label for="notifyOnBooking1"><fmt:message key="NotifyOnBooking"/></label>
                    </div>
                    <div class="unit">
                        <spf:checkbox path="notifyOnBookingCancellation"/><label for="notifyOnBookingCancellation1"><fmt:message key="NotifyOnBookingCancellation"/></label>
                    </div>
                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
