<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        
        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/bookings"><fmt:message key="Bookings"/></a></li>
            <li><a href="/admin/bookings/offeroptions"><fmt:message key="OfferOptions"/></a></li>
            <li class="active"><fmt:message key="OfferOption"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="OfferOption"/></h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <spf:input type="hidden" path="id"/>
                    <spf:input type="hidden" path="position"/>
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>

                    
                    <div class="relative">
                        <spf:input path="name" type="text" class="form-control form-top-element"/>
                        <div class="explanation"><fmt:message key="Name"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:select path="offerOptionType" class="select-simple form-control" data-style="form-center-element" data-container="body">
                            <spf:options items="${OfferOptionTypes}"/>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="OfferOptionType"/></span>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="description" type="text" class="form-control form-bottom-element"/>
                        <div class="explanation"><fmt:message key="Description"/></div>
                    </div>
                    
                    <button class="btn btn-primary btn-block btn-form-submit unit-2" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/admin/include/colorpicker.jsp"/>