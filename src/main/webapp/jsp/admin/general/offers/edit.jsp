<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <h1><fmt:message key="Offer"/></h1>
        </div>
        
        <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
            <spf:input type="hidden" path="id"/>
            <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>
            
            <fmt:message var="Name" key="Name"/>
            <spf:input path="name" type="text" class="form-control form-top-element" placeholder="${Name}"/>
         
            <div class="input-group">
                <span class="input-group-btn">
                    <button type="button" class="btn btn-default btn-plus-minus form-center-element form-control form-bottom-left-element" data-type="minus" data-field="maxConcurrentBookings">
                        <span class="fa fa-minus"></span>
                    </button>
                </span>
                <span class="relative">
                    <spf:input type="text" path="maxConcurrentBookings" class="form-control text-center input-plus-minus form-center-element" min="1" max="10"/>
                    <span class="explanation"><fmt:message key="MaxConcurrentBookings"/></span>
                </span>
                <span class="input-group-btn">
                    <button type="button" class="btn btn-default btn-plus-minus form-center-element form-control form-bottom-right-element" data-type="plus" data-field="maxConcurrentBookings">
                        <span class="fa fa-plus"></span>
                    </button>
                </span>
            </div>
            
            <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
      </spf:form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
