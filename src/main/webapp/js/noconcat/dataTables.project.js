$(document).ready(function () {

    $('.datatable').DataTable({
        stateSave: true,
        stateDuration: 0
    });
    
    $('.dataTables_wrapper').find('select').selectpicker({
        iconBase: 'fa',
        tickIcon: 'fa-check',
        dropupAuto: false
    });
});