<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">

        <div class="page-header"></div>

        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="Email"/></h4></div>
            <div class="panel-body">
                <div class="alert alert-success">
                    <div><fmt:message key="EmailWasSentSuccessfully"/></div>
                    <div><fmt:message key="EmailAcceptedCount"/>: ${MailResult.acceptedCount}</div>
                    <div><fmt:message key="EmailRejectedCount"/>: ${MailResult.rejectedCount}</div>
                </div>
                <a class="btn btn-primary btn-block unit-2" href="/home"><fmt:message key="GoToHomepage"/></a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
