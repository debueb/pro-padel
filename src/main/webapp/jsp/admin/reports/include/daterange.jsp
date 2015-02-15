<%@include file="/jsp/include/include.jsp"%>
<spf:form method="POST" modelAttribute="DateRange">
    <%-- Start Datum --%>
    <div class="datepicker-container">
        <div class="datepicker-text-container form-top-element">
            <div class="datepicker-label"><fmt:message key="GameDate"/> <fmt:message key="Start"/></div>
            <span class="fa fa-calendar datepicker-icon"></span>
            <div class="datepicker-text"></div>
        </div>
        <spf:input type="hidden" path="startDate" class="datepicker-input form-control" value="${DateRange.startDate}"/>
        <div class="datepicker" data-show-on-init="false" data-allow-past="true"></div>
    </div>

    <%-- End Datum --%>
    <div class="datepicker-container">
        <div class="datepicker-text-container form-bottom-element">
            <div class="datepicker-label"><fmt:message key="GameDate"/> <fmt:message key="End"/></div>
            <span class="fa fa-calendar datepicker-icon"></span>
            <div class="datepicker-text"></div>
        </div>
        <spf:input type="hidden" path="endDate" class="datepicker-input form-control"/>
        <div class="datepicker" data-show-on-init="false" data-allow-past="true" value="${DateRange.endDate}"></div>
    </div>
    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Select"/></button>
</spf:form>