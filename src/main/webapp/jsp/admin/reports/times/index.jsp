<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        
        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/reports"><fmt:message key="Reports"/></a></li>
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
<jsp:include page="/jsp/admin/reports/include/highcharts.jsp"/>
<script>
    loadHighCharts(function(){
        var days = [];
        var times = [];
        var array = [];

        var data = ${chartData};
        for(var index in data){
            var obj = data[index];
            days.push(obj.dayOfWeek);
            times.push(obj.time);
        }

        function onlyUnique(value, index, self) { 
            return self.indexOf(value) === index;
        }

        days = days.filter(onlyUnique);
        times = times.filter(onlyUnique);
        times.reverse();

        for (var dayindex in days){
            var dayOfWeek = days[dayindex];
            for (var timeindex in times){
                var time = times[timeindex];
                var count = null;
                for (var itemindex in data){
                    var obj = data[itemindex];
                    if (obj.dayOfWeek === dayOfWeek && obj.time === time){
                        count = obj.count;
                        break;
                    }
                }
                array.push([parseInt(dayindex), parseInt(timeindex), count]);
            }
        }

        new Highcharts.Chart({

            chart: {
                renderTo: 'times',
                type: 'heatmap',
                marginTop: 20,
                marginBottom: 80,
                height: 600
            },

            title: {
                text: null
            },

            xAxis: {
                categories: days,
                labels: {
                    rotation: 90
                }
            },

            yAxis: {
                categories: times,
                title: null
            },

            colorAxis: {
                min: 0,
                minColor: '#FFFFFF',
                maxColor: Highcharts.getOptions().colors[0]
            },

            legend: {
                align: 'right',
                layout: 'vertical',
                margin: 0,
                verticalAlign: 'top',
                y: 25,
                symbolHeight: 320
            },

            tooltip: {
                formatter: function () {
                    var value = (this.point.value === null) ? 0 : this.point.value;
                    return '<b>' + this.series.xAxis.categories[this.point.x] + ' ' + this.series.yAxis.categories[this.point.y] + '</b>: ' + value;
                }
            },

            series: [{
                name: '',
                borderWidth: 1,
                data: array,
                dataLabels: {
                    enabled: true,
                    color: 'black',
                    style: {
                        textShadow: 'none',
                        HcTextStroke: null
                    }
                }
            }]
        });
    });
</script>
<jsp:include page="/jsp/include/footer.jsp"/>
