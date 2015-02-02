<%@ page pageEncoding="UTF-8" contentType="text/html" %> 
<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header">
            <h1><fmt:message key="Allocations"/></h1>
        </div>
        <div class="datepicker-container">
            <div class="datepicker-text-container">
                <span class="fa fa-calendar datepicker-icon"></span>
                <div class="datepicker-text"></div>
            </div>
            <input type="hidden" class="datepicker-input" class="form-control" value="${Day}" />
            <div class="datepicker" data-show-on-init="false" data-redirect-on-select="/admin/bookings/reports/allocations/{date}" data-day-config='${dayConfigs}' data-max-date='${maxDate}'></div>
        </div>

        <c:choose>
            <c:when test="${empty RangeMap}">
                <div class="alert alert-danger unit"><fmt:message key="NoBookingsForSelectedDate"/></div>
            </c:when>
            <c:otherwise>
                <div class="table-responsive unit">
                    <table class="table table-bordered table-booking table-fixed">
                        <thead>
                            <tr>
                                <th class="text-center"><fmt:message key="TimeShort"/></th>
                                <c:forEach var="WeekDay" items="${WeekDays}">
                                    <th class="text-center ${Day == WeekDay ? 'booking-selected-date' : ''}">
                                        <a href="/bookings/${WeekDay}" class="ajaxify">
                                            <fmt:message key="DayShort-${WeekDay.dayOfWeek}"/>
                                            <br /><joda:format value="${WeekDay}" pattern="dd.MM."/>
                                        </a>
                                    </th>
                                </c:forEach>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="Entry" items="${RangeMap}">
                                <tr>
                                    <c:set var="TimeRange" value="${Entry.key}"/>
                                    <c:set var="TimeSlots" value="${Entry.value}"/>
                                    <joda:format value="${TimeRange.startTime}" pattern="HH:mm" var="startTime"/>
                                    <joda:format value="${TimeRange.endTime}" pattern="HH:mm" var="endTime"/>
                                    <td class="booking-time">${startTime}<span>-</span>${endTime}</td>
                                
                                    <c:forEach var="WeekDay" items="${WeekDays}">
                                        <c:set var="containsTimeSlot" value="false"/>
                                        <c:forEach var="TimeSlot" items="${TimeSlots}">
                                            <c:if test="${TimeSlot.date.dayOfWeek == WeekDay.dayOfWeek}">
                                                <c:set var="containsTimeSlot" value="true"/>
                                                <c:set var="urlDetail" value="/admin/bookings/reports/bookinglist/${TimeSlot.date}"/>
                                                <td class="booking-bookable ${fn:length(TimeSlot.bookings) == 0 ? 'booking-bookable-last' : ''}">
                                                    <a class="block ajaxify" href="${urlDetail}">${fn:length(TimeSlot.bookings)}</a>
                                                </td>
                                            </c:if>
                                        </c:forEach>
                                        <c:if test="${containsTimeSlot == false}">
                                            <td class="booking-disabled"></td>
                                        </c:if>
                                    </c:forEach>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
