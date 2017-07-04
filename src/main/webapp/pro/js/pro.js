$( document ).ready(function() {
    //disable duplicate form submission
    $('form').on('submit',function(){
        $(this).attr('disabled', 'disabled');
        $(this).find('button[type="submit"]').attr('disabled', 'disabled');
    });
});