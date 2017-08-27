<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        
        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/reports"><fmt:message key="Reports"/></a></li>
            <li class="active"><fmt:message key="Utilization"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Utilization"/></h4>
            </div>
            <div class="panel-body">
                <jsp:include page="/WEB-INF/jsp/admin/include/daterange.jsp"/>

                <div id="utilization" class="unit"></div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/admin/reports/include/highcharts.jsp"/>
<script>
    loadHighCharts(function(){
        var myData = [];
        var data = ${chartData};
        for(var k in data){
            myEntry = [];
            myEntry.push(parseInt(k, 10));
            myEntry.push(data[k]);
            myData.push(myEntry);
        }

        new Highcharts.Chart({
            chart: {
                    renderTo: 'utilization',
                    type: 'heatmap',
                    marginTop: 20,
                    marginBottom: 40,
                    height: 500
                },
            title: {
                text: null
            },
            xAxis: {
                type: 'datetime'
            },
            yAxis: {
                min: 0,
                title: {
                    text: null
                }
            },
            legend: {
                enabled: false
            },
            plotOptions: {
                area: {
                    fillColor: {
                        linearGradient: {
                            x1: 0,
                            y1: 0,
                            x2: 0,
                            y2: 1
                        },
                        stops: [
                            [0, Highcharts.getOptions().colors[0]],
                            [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                        ]
                    },
                    marker: {
                        radius: 2
                    },
                    lineWidth: 1,
                    states: {
                        hover: {
                            lineWidth: 1
                        }
                    },
                    threshold: null
                }
            },

            series: [{
                type: 'area',
                name: 'Bookings',
                data: myData
            }]
        });
    });
</script>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
