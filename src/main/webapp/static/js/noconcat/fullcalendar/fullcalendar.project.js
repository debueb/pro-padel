$(document).ready(function () {
  $('#eventcalendar').fullCalendar({
     fixedWeekCount: false,
     height: 'auto',
     timeFormat: 'HH:mm',
     validRange: function(currentDate) {
         return {
           start: currentDate.clone().subtract(1, 'days'),
           end: currentDate.clone().add(2, 'months').endOf('month')
         };
       },
     buttonIcons: {
        prev: '-1',
        next: '-1'
     },
     contentHeight: 50,
     header: {
        left: 'prev',
        center: 'title',
        right: 'next'
     },
     events: window.eventcalendar.events,
     eventAfterAllRender: function(event){
        $(event.el).ajaxify();
     }
  });
});