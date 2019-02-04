<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<div class="row pageentry relative">
    <c:if test="${not PageEntry.fullWidth}"><div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2 relative"></c:if>
    <c:if test="${fn:contains(sessionScope.privileges,'ManageGeneral')}">
        <%-- do not ajaxify edit link because tinymce breaks --%>
       <a class="edit-page" href="/admin/general/modules/page/${Module.id}/edit/${PageEntry.id}"><i class="fa fa-edit"></i></a>
    </c:if>
    ${PageEntry.message}
    <c:if test="${not PageEntry.fullWidth}"></div></c:if>
</div>
<c:if test="${PageEntry.showContactForm}">
    <div class="row pageentry">
        <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">

            <div class="panel panel-info relative">
                <div class="panel-heading">
                    <h4><fmt:message key="Contact"/></h4>
                </div>
                <div class="panel-body">
                    <spf:form method="POST" class="form-signin unit" modelAttribute="Mail">
                        <spf:errors path="*" cssClass="error"/>
                        <div class="relative">
                            <spf:input type="email" path="from" class="form-control form-top-element"/>
                            <div class="explanation">
                                <fmt:message key="EmailAddress"/>
                            </div>
                        </div>
                        <div class="relative">
                            <spf:input type="text" path="subject" class="form-control form-center-element"/>
                            <div class="explanation">
                                <fmt:message key="Subject"/>
                            </div>
                        </div>
                        <div class="relative">
                            <spf:textarea path="body" class="form-control form-bottom-element" style="height: 200px;"/>
                            <div class="explanation">
                                <fmt:message key="Message"/>
                            </div>
                        </div>
                        <button class="btn btn-primary btn-block unit-2" type="submit"><fmt:message key="Send"/></button>
                    </spf:form>
                </div>
            </div>
        </div>
    </div>
    <div class="unit-4"></div>
</c:if>
<c:if test="${PageEntry.showEventCalendar}">
    <div class="row pageentry">
        <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
            <div id="eventcalendar"></div>
            <br><br>
        </div>
    </div>
    <link rel='stylesheet' href='${contextPath}static/css/noconcat/fullcalendar/all.css' />
    <script type="text/javascript">
        window.eventcalendar = window.eventcalendar || {};
        window.eventcalendar.events = ${Events};
    </script>
    <script src='${contextPath}static/js/noconcat/fullcalendar/all.js'></script>
</c:if>

<c:if test="${PageEntry.showEventOverview}">

    <link rel='stylesheet' href='${contextPath}static/css/noconcat/eventoverview/eventoverview.css' />

    <div class="row pageentry">
        <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
            <div id="event-overview">
                <c:forEach var="EventMapEntry" items="${EventMap}">
                    <c:set var="Month" value="${EventMapEntry.key}"/>
                    <c:set var="EventList" value="${EventMapEntry.value}"/>
                    <c:set var="tooLate" value="false" />
                    <div class="month">
                        <div class="month-name"><fmt:message key="Month${Month}" /></div>
                        <c:forEach var="Event" items="${EventList}">
                            <c:if test="${not tooLate and Event.startDate lt Today}">
                                <div class="month-name" style="text-align: left; margin-top: 10px;"><fmt:message key="TooLateFor" />:</div>
                                <c:set var="tooLate" value="true" />
                            </c:if>
                            <a class="month-event ${tooLate ? 'disabled' : ''}" href="/events/event/${Event.id}">
                                <span class="month-event-date"><joda:format value="${Event.startDate}" pattern="EEE, dd." /></span>
                                <span class="month-event-name">${Event.name}</span>
                                <span class="month-event-arrow"><i class="fa fa-chevron-right"></i></span>
                            </a>
                        </c:forEach>
                    </div>
                </c:forEach>
            </div>
            <br><br><br>
        </div>
    </div>

    <script type="text/javascript">
        $(document).ready(function () {
            $('#event-overview').slick({
                infinite: false,
                mobileFirst: true,
                arrows: true,
                adaptiveHeight: true,
                initialSlide: 0,
                responsive: [
                    {
                        breakpoint: 1679,
                        settings: {
                            slidesToShow: 3
                        }
                    }, {
                        breakpoint: 992,
                        settings: {
                            slidesToShow: 2
                        }
                    }, {
                        breakpoint: 480,
                        settings: {
                            slidesToShow: 1
                        }
                    }]
            });
        });
    </script>
</c:if>