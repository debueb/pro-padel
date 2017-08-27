<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        
        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/reports"><fmt:message key="Reports"/></a></li>
            <li class="active"><fmt:message key="Players"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>20 <fmt:message key="TopPlayers"/></h4>
            </div>
            <div class="panel-body">
                <div id="players" class="unit"></div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/admin/reports/include/highcharts.jsp"/>
<script>
    loadHighCharts(function(){
        var keys = [];
        var values = [];
        var data = ${chartData};
        for(var k in data){
            keys.push(k);
            values.push(data[k]);
        }
        new Highcharts.Chart({
            chart: {
                renderTo: 'players',
                type: 'bar',
                marginTop: 20,
                marginBottom: 70
            },
            title: {
                text: null
            },
            subtitle: {
                text: null
            },
            xAxis: {
                categories: keys,
                title: {
                    text: null
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: null
                },
                labels: {
                    overflow: 'justify'
                }
            },
            plotOptions: {
                bar: {
                    dataLabels: {
                        enabled: true
                    }
                }
            },
            series: [{
                name: 'Buchungen',
                data: values
            }]
        });
    });
</script>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>