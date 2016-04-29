<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Facility"/></h4>
            </div>
        </div>


        <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
            <spf:input type="hidden" path="id"/>
            <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>
            <fmt:message var="Name" key="Name"/>
            <spf:input path="name" type="text" class="form-control form-top-element" placeholder="${Name}"/>

            <fmt:message key="Offers" var="OffersMsg"/>
            <spf:select path="offers" class="select-multiple show-tick form-control" data-style="form-bottom-element" title="${OffersMsg}" multiple="true" data-container="body">
                <spf:options items="${Offers}" itemLabel="name" itemValue="id"/>
            </spf:select>

            <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
        </spf:form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
