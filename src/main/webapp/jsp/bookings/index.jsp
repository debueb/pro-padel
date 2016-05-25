<%@ page pageEncoding="UTF-8" contentType="text/html" %> 
<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12">
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
                            <select name="time" class="select-simple form-control ${empty Facilities ? 'form-bottom-element' : 'form-center-element'}" data-container="body">
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

                        <c:if test="${not empty Facilities}">
                            <div class="relative">
                                <select name="facilities" class="select-multiple form-control form-bottom-element" multiple="true" data-container="body">
                                    <c:forEach var="Facility" items="${Facilities}">
                                        <option value="${Facility.id}" ${as:contains(SelectedFacilities, Facility) ? 'selected="selected"' : ''}>
                                            ${Facility.name}
                                        </option>
                                    </c:forEach>
                                </select>
                                <span class="explanation-select"><fmt:message key="Offers"/></span>
                            </div>
                        </c:if>
                    </c:if>

                    <button type="submit" class="btn btn-primary unit stretch"><fmt:message key="Refresh"/></button>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="row">    
    <div class="relative">
        <div class="booking-gallery-time">
            <c:forEach var="TimeRange" items="${RangeMap}">
                <joda:format value="${TimeRange.startTime}" pattern="HH:mm" var="startTime"/>
                <c:if test="${selectedTime == null or selectedTime == TimeRange.startTime}">
                    <div>${startTime}</div>
                </c:if>
            </c:forEach>
        </div>
        <div class="booking-slick unit-2">

            <c:forEach var="WeekDay" items="${WeekDays}">
                <div>

                    <div class="booking-gallery-day">
                        <fmt:message key="DayShort-${WeekDay.dayOfWeek}"/>, <joda:format value="${WeekDay}" pattern="dd.MM."/>
                    </div>
                    <table class="table table-booking table-fixed" >
                        <thead>
                            <tr>
                                <c:forEach var="Offer" items="${Offers}">
                                    <c:if test="${fn:contains(SelectedOffers, Offer)}">
                                        <th>${Offer}</th>
                                        </c:if>
                                    </c:forEach>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="TimeRange" items="${RangeMap}">

                                <tr>
                                    <c:forEach var="Offer" items="${Offers}">

                                        <c:if test="${fn:contains(SelectedOffers, Offer)}">
                                            <td>
                                                <c:set var="containsTimeSlot" value="false"/>
                                                <c:forEach var="TimeSlot" items="${TimeRange.timeSlots}">
                                                    <c:if test="${TimeSlot.date.dayOfWeek eq WeekDay.dayOfWeek}">
                                                        <c:set var="containsTimeSlot" value="true"/>
                                                        <c:choose>
                                                            <c:when test="${fn:contains(TimeSlot.availableOffers, Offer)}">
                                                                <joda:format value="${TimeRange.startTime}" pattern="HH:mm" var="startTime"/>
                                                                <c:set var="urlDetail" value="/bookings/${TimeSlot.date}/${startTime}"/>
                                                                <a class="ajaxify booking-gallery-offer" href="${urlDetail}/offer/${Offer.id}" title="${Offer.name} ${startTime}" style="background-color: ${Offer.hexColor};">
                                                                    ${TimeSlot.config.basePrice}
                                                                </a>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="booking-gallery-offer booking-disabled"></span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </c:if>
                                                </c:forEach>

                                                <c:if test="${not containsTimeSlot}">
                                                    <span class="booking-gallery-offer booking-disabled"></span>
                                                </c:if>

                                            </td>
                                        </c:if>
                                    </c:forEach>
                                </tr>
                            </c:forEach>

                            <tr>
                                <c:forEach var="Offer" items="${Offers}">
                                    <c:if test="${fn:contains(SelectedOffers, Offer)}">
                                        <td>${Offer}</td>
                                    </c:if>
                                </c:forEach>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </c:forEach>
        </div>
    </div>


</div>
<script type="text/javascript">
    $(document).ready(function () {
        $('.booking-slick').slick({
            infinite: false,
            mobileFirst: true,
            arrows: true,
            adaptiveHeight: true,
            responsive: [
                {
                    breakpoint: 2240,
                    settings: {
                        slidesToShow: 7
                    }
                },{
                    breakpoint: 1920,
                    settings: {
                        slidesToShow: 6
                    }
                },{
                    breakpoint: 1600,
                    settings: {
                        slidesToShow: 5
                    }
                },{
                    breakpoint: 1280,
                    settings: {
                        slidesToShow: 4
                    }
                },{
                    breakpoint: 960,
                    settings: {
                        slidesToShow: 3
                    }
                }, {
                    breakpoint: 640,
                    settings: {
                        slidesToShow: 2
                    }
                }, {
                    breakpoint: 320,
                    settings: {
                        slidesToShow: 1
                    }
                }]
        });
    });
</script>
<jsp:include page="/jsp/include/footer.jsp"/>
