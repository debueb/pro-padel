$(document).ready(function () {

    $(".dd").nestable({ 
        maxDepth: 2,
        expandBtnHTML: '',
        collapseBtnHTML: ''
    });
    
    $('.dd').on('change', function(event) {
        var items = $('.dd').nestable('serialize');
        app.main.showSpinner();
        $.ajax({
            url: window.location.pathname+'/updateposition',
            type: 'POST',
            cache: false,
            processData: false,
            contentType: "application/json; charset=UTF-8",
            data: JSON.stringify(items),
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
                console.log(textStatus);
                console.log(errorThrown);
                alert("Saving failed. Please reload page to see current state.");
            },
            complete: function(){
                app.main.hideSpinner();
            }
        });
    });
});