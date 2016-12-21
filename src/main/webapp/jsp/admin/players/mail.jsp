<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="MailAllPlayers"/></h4>
            </div>
            <div class="panel-body">
                <div class="alert alert-info"><fmt:message key="MailAllPlayersWarning"/></div>
                <a class="btn btn-primary btn-block unit-2 btn-back"><fmt:message key="Cancel"/></a>
                <a class="btn btn-primary btn-block unit-2" href="mailto:?bcc=${bcc}&body=${body}" >Vaaaamos</a>
            </div>
        </div>
    </div>
</div>


<jsp:include page="/jsp/include/footer.jsp"/>
