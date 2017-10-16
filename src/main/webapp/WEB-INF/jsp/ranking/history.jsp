<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/${path}include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <c:forEach var="GenderDateRankingMapEntry" items="${GenderDateRankingMap}">
            <c:set var="Gender" value="${GenderDateRankingMapEntry.key}" />
            <c:set var="DateRankingMap" value="${GenderDateRankingMapEntry.value}" />
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h4><fmt:message key="${Gender}_ranking_history" /> - ${Participant}</h4>
                </div>
                <div class="panel-body">
                    <c:choose>
                        <c:when test="${empty DateRankingMap}">
                            <div class="alert alert-info"><fmt:message key="NoRankingsYet"/></div>
                        </c:when>
                        <c:otherwise>
                            <div id="${Gender}" class="unit"></div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </c:forEach>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Player"/> <fmt:message key="Links"/></h4>
            </div>
           <div class="panel-body">
                <div class="list-group">
                   <a href="/players/player/${Participant.UUID}" class="list-group-item">
                       <div class="list-item-text">${Participant}</div>
                   </a>
               </div>
            </div>
        </div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Ranking"/> <fmt:message key="Links"/></h4>
            </div>
           <div class="panel-body">
                <jsp:include page="/WEB-INF/jsp/ranking/include/links.jsp"/>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/ranking/include/highcharts.jsp"/>
<script>
    loadHighCharts(function(){
        var data = ${ChartMap};
        Object.keys(data).forEach(gender => {
            var myData = [];
            var min;
            var max;

            for(var k in data[gender]){
                var myEntry = [];
                var ranking = data[gender][k];
                min = Math.min(ranking, min ? min : ranking);
                max = Math.max(ranking, max ? max : ranking);
                myEntry.push(parseInt(k, 10));
                myEntry.push(ranking);
                myData.push(myEntry);
            }

            new Highcharts.Chart({
                chart: {
                        renderTo: gender,
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
                    min: min,
                    max: max,
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
                    name: 'Ranking',
                    data: myData
                }]
            });
        });
    });
</script>
<jsp:include page="/WEB-INF/jsp/${path}include/footer.jsp"/>
