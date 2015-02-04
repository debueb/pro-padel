$(document).ready(function () {

    $('.datatable').DataTable({
        stateSave: true,
        stateDuration: 0,
        "bPaginate": false
    });
    
   
    $('.datatable-grouped').dataTable({
        stateSave: true,
        stateDuration: 0,
        "bLengthChange": false,
        "bPaginate": false})
        .rowGrouping({
            bExpandableGrouping: true,
            bExpandable: false,
            asExpandedGroups: [""]
        });
    
    $('.dataTables_wrapper').find('select').selectpicker({
        iconBase: 'fa',
        tickIcon: 'fa-check',
        dropupAuto: false
    });
});