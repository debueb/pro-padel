<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="TopPlayers"/></h4>
            </div>
        </div>

        <div id="players" class="unit"></div>
    </div>
</div>
<script>
    var app = app || {};
    app.chartData = ${chartData}
</script>
<jsp:include page="/jsp/admin/reports/include/highcharts.jsp"/>
<jsp:include page="/jsp/include/footer.jsp"/>