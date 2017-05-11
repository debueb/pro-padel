var app = app || {};
app.charts = {};

(function () {

    var self = this;
    
    self.setDefaultChartOptions = function(){
        Highcharts.setOptions({
            credits: {
                enabled: false
            }
        });
    };

    self.showPlayers = function(data) {
        if (document.getElementById('players')){
            var keys = [];
            var values = [];
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
        }
    };
    
    self.showTimes = function(data){
        if (document.getElementById('times')){
            var days = [];
            var times = [];
            var array = [];
            
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
            console.log(array);
            
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
        }
    };
    
    self.showUtilization = function(data){
        if (document.getElementById('utilization')){
            var myData = [];
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
        }
    };

    self.setDefaultChartOptions();
    self.showPlayers(app.chartData);
    self.showTimes(app.chartData);
    self.showUtilization(app.chartData);
    
    return app;
}).apply(app.charts);

