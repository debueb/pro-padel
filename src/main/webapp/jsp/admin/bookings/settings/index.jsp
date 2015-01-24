<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-md-10 col-md-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header">
            <h1><fmt:message key="BookingSettings"/></h1>
        </div>

        <div class="table-responsive unit">
            <table class="table table-bordered table-centered">
                <thead>
                    <tr>
                        <th class="text-center"><fmt:message key="Priority"/></th>
                        <th class="text-center"><fmt:message key="WeekDay"/></th>
                        <th class="text-center"><fmt:message key="StartDate"/></th>
                        <th class="text-center"><fmt:message key="EndDate"/></th>
                        <th class="text-center"><fmt:message key="StartTime"/></th>
                        <th class="text-center"><fmt:message key="EndTime"/></th>
                        <th class="text-center"><fmt:message key="NumberOfCourts"/></th>
                        <th class="text-center"><fmt:message key="MinDuration"/></th>
                        <th class="text-center"><fmt:message key="MinInterval"/></th>
                        <th class="text-center"><fmt:message key="PricePerMinDuration"/></th>
                        <th class="text-center"><fmt:message key="Delete"/></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${Models}" var="Model">
                        <tr>
                            <c:set var="urlEdit" value="/admin/bookings/settings/edit/${Model.id}"/>
                            <td><a class="ajaxify" href="${urlEdit}">${Model.priority}</a></td>
                            <td>
                                <a class="ajaxify" href="${urlEdit}">
                                    <c:forEach var="WeekDay" items="${Model.calendarWeekDays}" varStatus="status">
                                        <fmt:message key="${WeekDay}"/><c:if test="${not status.last}">, </c:if>
                                    </c:forEach>
                                </a>
                            </td>
                            <td><a class="ajaxify" href="${urlEdit}"><joda:format value="${Model.startDate}" pattern="yyyy-MM-dd" /></a></td>
                            <td><a class="ajaxify" href="${urlEdit}"><joda:format value="${Model.endDate}" pattern="yyyy-MM-dd" /></a></td>
                            <td><a class="ajaxify" href="${urlEdit}"><fmt:formatNumber value="${Model.startTimeHour}" minIntegerDigits="2"/>:<fmt:formatNumber value="${Model.startTimeMinute}" minIntegerDigits="2"/></a></td>
                            <td><a class="ajaxify" href="${urlEdit}"><fmt:formatNumber value="${Model.endTimeHour}" minIntegerDigits="2"/>:<fmt:formatNumber value="${Model.endTimeMinute}" minIntegerDigits="2"/></a></td>
                            <td><a class="ajaxify" href="${urlEdit}">${Model.courtCount}</a></td>
                            <td><a class="ajaxify" href="${urlEdit}">${Model.minDuration}</a></td>
                            <td><a class="ajaxify" href="${urlEdit}">${Model.minInterval}</a></td>
                            <td><a class="ajaxify" href="${urlEdit}">${Model.basePrice}</td>
                            <td><a class="ajaxify fa fa-minus-circle" href="/admin/bookings/settings/${Model.id}/delete"></a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <a href="/admin/bookings/settings/add" class="btn btn-primary unit ajaxify"><fmt:message key="AddRule"/></a>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
