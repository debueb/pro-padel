$(document).ready(function () {

    var el = document.getElementById('table-sortable');
    var sortable = Sortable.create(el, {
        handle: '.sortable-handle',
        onUpdate: function(evt){
            $.ajax({
                url: window.location.pathname+'/updatesortorder',
                type: 'POST',
                cache: false,
                processData: false,
                contentType: "application/json; charset=UTF-8",
                data: JSON.stringify(sortable.toArray()),
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log(jqXHR);
                    console.log(textStatus);
                    console.log(errorThrown);
                    alert('Failed to update item order');
                }
            });
        }
    });
});