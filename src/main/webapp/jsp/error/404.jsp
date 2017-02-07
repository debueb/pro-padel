<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="container unit">
    <div class="row">
        <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
            <div class="jumbotron">
                <h1><fmt:message key="Oooops"/></h1>
                <p><fmt:message key="MissingContent"/></p>
                <p><a class="btn btn-primary btn-lg" href="/"><fmt:message key="Home"/></a>&nbsp;<a class="btn btn-primary btn-lg" href="/contact"><fmt:message key="Contact"/></a></p>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
