enableColorpicker = function(){
    $('.color-picker').colorpicker({'format': 'rgba'});
};
$(window).on('statechangecomplete', enableColorpicker);
$(document).ready(enableColorpicker);