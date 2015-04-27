<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <fmt:message var="AddEntry" key="AddEntry"/>
            <fmt:message var="EditEntry" key="EditEntry"/>
            <h1>${empty Model.id ? AddEntry : EditEntry}</h1>
        </div>
        
        
        <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model" id="news-form">
            <spf:input type="hidden" path="id"/>
            <spf:input type="hidden" path="position"/>
            <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>
            
            <div class="datepicker-container">
                <div class="datepicker-text-container form-top-element">
                    <div class="datepicker-label"><fmt:message key="Date"/></div>
                    <span class="fa fa-calendar datepicker-icon"></span>
                    <div class="datepicker-text"></div>
                </div>
                <spf:input type="hidden" path="lastModified" class="datepicker-input form-control" value="${Model.newsDate}" />
                <div class="datepicker" data-show-on-init="false"></div>
            </div>
            <fmt:message var="Title" key="Title"/>
            <spf:input path="title" type="text" class="form-control form-center-element" placeholder="${Title}"/>
            <spf:input path="message" type="hidden" id="message"/>
            <div class="form-bottom-element">
                <div id="summernote">${Model.message}</div>
            </div>
            <div class="unit">
                <spf:checkbox path="showOnHomepage" id="showOnHomepage"/><label for="showOnHomepage"><fmt:message key="ShowOnHomepage"/></label>
            </div>
            <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
      </spf:form>
    </div>
</div>
<%-- include summernote in body when requested via ajax, otherwise after footer (where jquery is added) --%>
<c:if test="${not empty header['x-requested-with']}">
    <jsp:include page="/jsp/admin/include/summernote.jsp"/>
</c:if>
<jsp:include page="/jsp/include/footer.jsp"/>
<c:if test="${empty header['x-requested-with']}">
    <jsp:include page="/jsp/admin/include/summernote.jsp"/>
</c:if>