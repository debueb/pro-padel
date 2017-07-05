<script>
    var loadHighCharts = function(callback){
        if (!window.Highcharts){
            $.getScript('/static/js/noconcat/highcharts.js', function(){
                $.getScript('/static/js/noconcat/highcharts.heatmap.js', function(){
                    Highcharts.setOptions({
                        credits: {
                            enabled: false
                        }
                    });
                    callback();
                });
            });
        } else {
            callback();
        }
    };
</script>