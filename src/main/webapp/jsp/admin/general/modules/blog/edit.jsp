<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-lg-10 col-lg-offset-1">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <fmt:message var="AddBlogEntry" key="AddBlogEntry"/>
        <fmt:message var="EditBlogEntry" key="EditBlogEntry"/>
                
        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/general"><fmt:message key="General"/></a></li>
            <li><a href="/admin/general/modules"><fmt:message key="Modules"/></a></li>
            <li><a href="/admin/general/modules/edit/${Parent.id}">${Parent.title}</a></li>
            <li><a href="/${moduleName}/${Parent.id}"><fmt:message key="BlogEntries"/></a></li>
            <li class="active">${empty Model.id ? AddBlogEntry : EditBlogEntry}</li>
        </ol>

        <div class="page-header"></div>
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${empty Model.id ? AddBlogEntry : EditBlogEntry}</h4>
            </div>
            <div class="panel-body">

                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
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
                    <div class="relative">
                        <spf:textarea path="message" class="form-control form-bottom-element text-editor"/>
                        <div class="explanation"><fmt:message key="Description"/></div>
                    </div>
                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/admin/include/text-editor.jsp"/>