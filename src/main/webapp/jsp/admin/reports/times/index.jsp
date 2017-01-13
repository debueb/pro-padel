<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        
        <ol class="unit-2 breadcrumb">
            <li><a class="ajaxify" href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a class="ajaxify" href="/admin/reports"><fmt:message key="Reports"/></a></li>
            <li class="active"><fmt:message key="TopTimes"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="TopTimes"/></h4>
            </div>
            <div class="panel-body">
                <jsp:include page="/jsp/admin/include/daterange.jsp"/>

                <div id="times" class="unit"></div>
            </div>
        </div>
    </div>
</div>
<script>
    var app = app || {};
    app.chartData = ${chartData};
</script>
<jsp:include page="/jsp/admin/reports/include/highcharts.jsp"/>
<jsp:include page="/jsp/include/footer.jsp"/>
