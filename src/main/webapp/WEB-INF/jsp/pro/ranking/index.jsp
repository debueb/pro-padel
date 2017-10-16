<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/${path}include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-default">
            <div class="panel-heading"><h4><fmt:message key="Ranking"/></h4></div>
            <div class="panel-body"><fmt:message key="RankingDesc"/></div>
        </div>

        <div class="panel panel-default">
            <div class="panel-heading">
                <h4><fmt:message key="Ranking"/></h4>
            </div>
            <div class="panel-body">
                <jsp:include page="/WEB-INF/jsp/${path}ranking/include/links.jsp"/>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/${path}include/footer.jsp"/>
