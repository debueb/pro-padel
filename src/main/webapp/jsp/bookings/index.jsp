<%@ page pageEncoding="UTF-8" contentType="text/html" %> 
<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="BookCourt"/></h4></div>
            <div class="panel-body">

            <form method="GET" class="ajaxify" action="${contextPath}/bookings">
                <div class="datepicker-container ">
                    <div class="datepicker-text-container ${empty RangeMap ? '' : 'form-top-element'}">
                        <div class="datepicker-label"><fmt:message key="Date"/></div>
                        <span class="fa fa-calendar datepicker-icon"></span>
                        <div class="datepicker-text"></div>
                    </div>
                    <input type="hidden" name="date" class="datepicker-input" class="form-control" value="${Day}" />
                    <div class="datepicker" data-show-on-init="false" data-redirect-on-select="/bookings/{date}/{time}" data-day-config='${dayConfigs}' data-max-date='${maxDate}'></div>
                </div>
                <c:if test="${not empty RangeMap}">
                    <div class="relative">
                        <select name="time" class="select-simple form-control form-center-element" data-container="body">
                            <option value=""><fmt:message key="AllStartTimes"/></option>
                            <c:forEach var="TimeRange" items="${RangeMap}">
                                <c:if test="${TimeRange.offersAvailable}">
                                    <option ${selectedTime == TimeRange.startTime ? 'selected' : ''}>
                                        <joda:format value="${TimeRange.startTime}" pattern="HH:mm"/>
                                    </option>
                                </c:if>
                            </c:forEach>
                        </select>
                        <span class="explanation-select"><fmt:message key="StartTime"/></span>
                    </div>

                    <div class="relative">
                        <select name="offers" class="select-multiple form-control form-bottom-element" multiple="true" data-container="body">
                            <c:forEach var="Offer" items="${Offers}">
                                <option value="${Offer.id}" ${as:contains(SelectedOffers, Offer) ? 'selected="selected"' : ''}>
                                    ${Offer.name}
                                </option>
                            </c:forEach>
                        </select>
                        <span class="explanation-select"><fmt:message key="Offers"/></span>
                    </div>
                </c:if>
                
                <button type="submit" class="btn btn-primary unit stretch"><fmt:message key="Refresh"/></button>
            </form>
            </div>
        </div>
                        
        <c:choose>
            <c:when test="${empty RangeMap}">
                <div class="alert alert-danger unit"><fmt:message key="NoBookableTimeSlotsAvailable"/></div>
            </c:when>
            <c:otherwise>
                <div class="unit-2 table-responsive unit">
                    <table class="table table-booking table-fixed">
                        <thead>
                            <tr>
                                <th class="text-center"><fmt:message key="TimeShort"/></th>
                                    <c:forEach var="WeekDay" items="${WeekDays}">
                                    <th class="text-center ${Day == WeekDay ? 'booking-selected-date' : ''}">
                                        <a href="/bookings/?date=${WeekDay}" class="ajaxify">
                                            <fmt:message key="DayShort-${WeekDay.dayOfWeek}"/>
                                            <br /><joda:format value="${WeekDay}" pattern="dd.MM."/>
                                        </a>
                                    </th>
                                </c:forEach>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="TimeRange" items="${RangeMap}">
                                <c:if test="${TimeRange.offersAvailable}">
                                    <joda:format value="${TimeRange.startTime}" pattern="HH:mm" var="startTime"/>
                                    <joda:format value="${TimeRange.endTime}" pattern="HH:mm" var="endTime"/>
                                    <c:if test="${selectedTime == null or selectedTime == TimeRange.startTime}">
                                        <tr>
                                            <td class="booking-time">${startTime}<span> - </span>${endTime}</td>

                                            <c:forEach var="WeekDay" items="${WeekDays}">
                                                <c:set var="offerCount" value="0"/>

                                                <c:forEach var="TimeSlot" items="${TimeRange.timeSlots}">
                                                    <c:if test="${TimeSlot.date.dayOfWeek == WeekDay.dayOfWeek}">
                                                        <c:set var="offerCount" value="${offerCount + fn:length(TimeSlot.availableOffers)}"/>
                                                    </c:if> 
                                                </c:forEach>

                                                <c:choose>
                                                    <c:when test="${offerCount>0}">
                                                        <td class="booking-bookable">
                                                            <div class="booking-offer-container">
                                                                <c:forEach var="TimeSlot" items="${TimeRange.timeSlots}">
                                                                    <c:if test="${TimeSlot.date.dayOfWeek == WeekDay.dayOfWeek}">
                                                                        <c:set var="urlDetail" value="/bookings/${TimeSlot.date}/${startTime}"/>
                                                                        <c:forEach var="Offer" items="${TimeSlot.availableOffers}">
                                                                            <c:if test="${as:contains(SelectedOffers, Offer)}">
                                                                                <div class="booking-offer-row ${TimeSlot.date == Day ? 'booking-offer-row-selected' : ''}">
                                                                                    <a class="ajaxify booking-offer" href="${urlDetail}/offer/${Offer.id}" title="${Offer.name} ${startTime}" style="background-color: #00FF00; height: ${100/offerCount}%;">
                                                                                        <span>${Offer.name}</span> 
                                                                                        <span>${TimeSlot.config.currency.symbol}${TimeSlot.config.basePrice}</span>
                                                                                    </a>
                                                                                </div>
                                                                            </c:if>
                                                                        </c:forEach>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </div>
                                                        </td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td class="booking-disabled"></td>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </tr>
                                    </c:if>
                                </c:if>
                            </c:forEach>
                            <tr>
                                <td></td>
                                <c:forEach var="WeekDay" items="${WeekDays}">
                                    <td class="text-center ${Day == WeekDay ? 'booking-selected-date' : ''}">
                                        <a href="/bookings/${WeekDay}" class="ajaxify">
                                            <fmt:message key="DayShort-${WeekDay.dayOfWeek}"/>
                                            <br /><joda:format value="${WeekDay}" pattern="dd.MM."/>
                                        </a>
                                    </td>
                                </c:forEach>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
            
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
