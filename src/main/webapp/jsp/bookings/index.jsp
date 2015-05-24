<%@ page pageEncoding="UTF-8" contentType="text/html" %> 
<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="BookCourt"/></h4></div>
        </div>
        
        <div class="datepicker-container">
            <div class="datepicker-text-container">
                <span class="fa fa-calendar datepicker-icon"></span>
                <div class="datepicker-text"></div>
            </div>
            <input type="hidden" class="datepicker-input" class="form-control" value="${Day}" />
            <div class="datepicker" data-show-on-init="false" data-redirect-on-select="/bookings/{date}" data-day-config='${dayConfigs}' data-max-date='${maxDate}'></div>
        </div>

        <c:choose>
            <c:when test="${empty RangeMap}">
                <div class="alert alert-danger unit"><fmt:message key="NoBookableTimeSlotsAvailable"/></div>
            </c:when>
            <c:otherwise>
                <jsp:include page="/jsp/bookings/include/leyenda.jsp"/>

                <div class="unit-2 table-responsive unit">
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
                            <c:forEach var="TimeRange" items="${RangeMap}">
                                <c:if test="${TimeRange.offersAvailable}">
                                    <tr>
                                        <joda:format value="${TimeRange.startTime}" pattern="HH:mm" var="startTime"/>
                                        <joda:format value="${TimeRange.endTime}" pattern="HH:mm" var="endTime"/>
                                        <td class="booking-time">${startTime}<span>-</span>${endTime}</td>

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
                                                                        <div class="booking-offer-row">
                                                                            <a class="ajaxify booking-offer" href="${urlDetail}?offer=${Offer.id}" style="background-color: ${Offer.hexColor}; height: ${100/offerCount}%;">
                                                                                ${TimeSlot.config.currency.symbol}${TimeSlot.config.basePrice}
                                                                            </a>
                                                                        </div>
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
                                
                <jsp:include page="/jsp/bookings/include/leyenda.jsp"/>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
