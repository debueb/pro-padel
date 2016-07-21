<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        <div class="panel panel-info">
            <div class="panel-heading">
                <fmt:message var="AddEntry" key="AddEntry"/>
                <fmt:message var="EditEntry" key="EditEntry"/>
                <h4>${empty Model.id ? AddEntry : EditEntry}</h4>
            </div>
            <div class="panel-body">

                <spf:form method="POST" class="form-signin summernote-form" role="form" modelAttribute="Model">
                    <spf:input type="hidden" path="id"/>
                    <spf:input type="hidden" path="position"/>
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>

                    <div class="datepicker-container">
                        <div class="datepicker-text-container form-top-element">
                            <div class="datepicker-label"><fmt:message key="Date"/></div>
                            <span class="fa fa-calendar datepicker-icon"></span>
                            <div class="datepicker-text"></div>
                        </div>
                        <spf:input type="hidden" path="lastModified" class="datepicker-input form-control" value="${Model.lastModified}" />
                        <div class="datepicker" data-show-on-init="false"></div>
                    </div>
                    <fmt:message var="Title" key="Title"/>
                    <spf:input path="title" type="text" class="form-control form-center-element" placeholder="${Title}"/>
                    <spf:input path="message" type="hidden" id="summernote-input"/>
                    <div class="form-bottom-element">
                        <div id="summernote">${Model.message}</div>
                    </div>
                    <div class="unit">
                        <spf:checkbox path="fullWidth" id="fullWidth"/><label for="fullWidth"><fmt:message key="FullWidth"/></label>
                    </div>
                    <div class="unit">
                        <spf:checkbox path="showOnHomepage" id="showOnHomepage"/><label for="showOnHomepage"><fmt:message key="ShowOnHomepage"/></label>
                    </div>
                    <div class="unit">
                        <spf:checkbox path="showContactForm" id="showContactForm"/><label for="showContactForm"><fmt:message key="ShowContactForm"/></label>
                    </div>
                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/admin/include/summernote.jsp"/>