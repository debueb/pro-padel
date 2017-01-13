<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        
        <ol class="unit-2 breadcrumb">
            <li><a class="ajaxify" href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a class="ajaxify" href="/admin/bookings"><fmt:message key="Bookings"/></a></li>
            <li><a class="ajaxify" href="/admin/bookings/offers"><fmt:message key="Offers"/></a></li>
            <li class="active"><fmt:message key="Offer"/></li>
        </ol>
        
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Offer"/></h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <spf:input type="hidden" path="id"/>
                    <spf:input type="hidden" path="position"/>
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>

                    <fmt:message var="Name" key="Name"/>
                    <spf:input path="name" type="text" class="form-control form-top-element" placeholder="${Name}"/>

                    <fmt:message var="ShortName" key="ShortName"/>
                    <spf:input path="shortName" type="text" class="form-control form-center-element" placeholder="${ShortName}" maxlength="3"/>

                    <div class="input-group color-picker">
                        <span class="input-group-addon form-center-element"><i></i></span>
                        <span class="relative">
                            <spf:input type="text" path="hexColor" class="form-control form-center-element" />
                            <span class="explanation"><fmt:message key="Color"/></span>
                        </span>
                    </div>

                    <div class="input-group">
                        <span class="input-group-btn">
                            <button type="button" class="btn btn-default btn-plus-minus form-center-element form-control form-left-element" data-type="minus" data-field="maxConcurrentBookings">
                                <span class="fa fa-minus"></span>
                            </button>
                        </span>
                        <span class="relative">
                            <spf:input type="text" path="maxConcurrentBookings" class="form-control text-center input-plus-minus form-center-element" min="1" max="128"/>
                            <span class="explanation"><fmt:message key="MaxConcurrentBookings"/></span>
                        </span>
                        <span class="input-group-btn">
                            <button type="button" class="btn btn-default btn-plus-minus form-center-element form-control form-right-element" data-type="plus" data-field="maxConcurrentBookings">
                                <span class="fa fa-plus"></span>
                            </button>
                        </span>
                    </div>

                    <div class="relative">
                        <spf:select path="offerOptions" class="select-simple form-control" data-style="form-bottom-element" data-container="body">
                            <spf:options items="${OfferOptions}" itemLabel="name" itemValue="id"/>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="OfferOptions"/></span>
                    </div>

                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/admin/include/colorpicker.jsp"/>
<jsp:include page="/jsp/include/footer.jsp"/>
