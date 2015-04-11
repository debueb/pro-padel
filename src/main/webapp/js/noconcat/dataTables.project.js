$(document).ready(function () {

    $('.datatable').livequery(function(){
            $(this).DataTable({
            stateSave: true,
            stateDuration: 0,
            "bPaginate": false
        });
    });
    
   
    $('.datatable-grouped').livequery(function () {
        $(this).dataTable({
            stateSave: true,
            stateDuration: 0,
            "bLengthChange": false,
            "bPaginate": false
        })
        .rowGrouping({
            bExpandableGrouping: true,
            bExpandable: false,
            asExpandedGroups: [""]
        });
    });
    
    $('.dataTables_wrapper').livequery(function () {
        $(this).find('select').selectpicker({
            iconBase: 'fa',
            tickIcon: 'fa-check',
            dropupAuto: false
        });
    });
});