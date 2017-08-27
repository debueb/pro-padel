<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="RegistrationEmailVerified"/></h4>
            </div>
            <div class="panel-body">
                <div class="alert alert-success"><fmt:message key="EmailSuccessfullyVerified"/></div>
            </div>
        </div>


        
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
