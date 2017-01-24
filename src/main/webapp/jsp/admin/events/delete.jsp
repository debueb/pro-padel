<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <ol class="unit-2 breadcrumb">
            <li><a class="ajaxify" href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a class="ajaxify" href="/admin/events"><fmt:message key="Events"/></a></li>
            <li><a class="ajaxify" href="/admin/events/edit/${Model.id}">${Model.name}</a></li>
            <li class="active"><fmt:message key="DeleteWarning"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="DeleteWarning"/></h4>
            </div>
            <div class="panel-body">
                <div class="alert alert-danger">${error}</div>

                <h4><fmt:message key="AreYouSureYouWantToDelete"><fmt:param value="${Model}"/></fmt:message></h4>

                    <h4><fmt:message key="ThisWillDeleteAllGamesAsWell"/></h4>

                <form method="POST" class="ajaxify">
                    <a class="btn btn-primary btn-back unit ajaxify"><fmt:message key="Cancel"/></a>
                    <button class="btn btn-danger unit" style="margin-left: 10px;"><fmt:message key="Delete"/></button>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
