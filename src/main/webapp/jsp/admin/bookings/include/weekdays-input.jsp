<%@include file="/jsp/include/include.jsp"%>
<div class="relative">
    <spf:select path="calendarWeekDays" class="select-multiple form-control" data-style="form-center-element" multiple="true">
        <c:forEach items="${WeekDays}" var="WeekDay"><fmt:message var="WeekDayLabel" key="${WeekDay}"/>
            <spf:option value="${WeekDay}" label="${WeekDayLabel}"/>
        </c:forEach>
    </spf:select>
    <span class="explanation-select"><fmt:message key="WeekDays"/></span>
</div>