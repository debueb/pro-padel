<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="CourtReservations"/></h4>
            </div>
            <div class="panel-body">

                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <div class="alert alert-danger"><spf:errors path="*" cssClass="error" htmlEscape="false"/></div>
                    <spf:input path="id" type="hidden"/>

                    <fmt:message key="Comment" var="CommentPlaceholder"/>
                    <div class="relative">
                        <spf:input type="text" path="comment" class="form-control form-top-element" />
                        <div class="explanation">${CommentPlaceholder}</div>
                    </div>

                    <%-- Angebote --%>
                    <div class="relative">
                        <spf:select path="offers" class="select-multiple form-control" data-style="form-center-element" data-container="body">
                            <spf:options items="${Offers}" itemValue="id" itemLabel="name"/>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="Offers"/></span>
                    </div>

                    <%-- Wochentage --%>
                    <div class="relative">
                        <spf:select path="calendarWeekDays" class="select-multiple form-control" data-style="form-center-element" multiple="true" data-container="body">
                            <c:forEach items="${WeekDays}" var="WeekDay"><fmt:message var="WeekDayLabel" key="${WeekDay}"/>
                                <spf:option value="${WeekDay}" label="${WeekDayLabel}"/>
                            </c:forEach>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="WeekDays"/></span>
                    </div>

                    <%-- Start Datum --%>
                    <div class="datepicker-container">
                        <div class="datepicker-text-container form-center-element">
                            <div class="datepicker-label"><fmt:message key="Start"/></div>
                            <span class="fa fa-calendar datepicker-icon"></span>
                            <div class="datepicker-text"></div>
                        </div>
                        <spf:input type="hidden" path="startDate" class="datepicker-input form-control" />
                        <div class="datepicker" data-show-on-init="false"></div>
                    </div>

                    <%-- End Datum --%>
                    <div class="datepicker-container">
                        <div class="datepicker-text-container form-center-element">
                            <div class="datepicker-label"><fmt:message key="End"/></div>
                            <span class="fa fa-calendar datepicker-icon"></span>
                            <div class="datepicker-text"></div>
                        </div>
                        <spf:input type="hidden" path="endDate" class="datepicker-input form-control" />
                        <div class="datepicker" data-show-on-init="false"></div>
                    </div>

                    <%-- Von --%>
                    <span class="relative input-hour">
                        <spf:select path="startTimeHour" class="select-simple form-left-element form-center-element" data-container="body">
                            <c:forEach var="hour" begin="0" end="23">
                                <fmt:formatNumber value="${hour}" minIntegerDigits="2" var="hour"/>
                                <spf:option value="${hour}"/>
                            </c:forEach>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="FromHour"/></span>
                    </span>
                    <span class="relative input-hour">
                        <spf:select path="startTimeMinute" class="select-simple form-right-element form-center-element " data-container="body">
                            <c:forEach var="minute" begin="0" end="55" step="5">
                                <fmt:formatNumber value="${minute}" minIntegerDigits="2" var="minute"/>
                                <spf:option value="${minute}"/>
                            </c:forEach>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="FromMinute"/></span>
                    </span>

                    <%-- Bis --%>
                    <span class="relative input-hour">
                        <spf:select path="endTimeHour" class="select-simple form-bottom-element form-left-element" data-container="body">
                            <c:forEach var="hour" begin="0" end="23">
                                <fmt:formatNumber value="${hour}" minIntegerDigits="2" var="hour"/>
                                <spf:option value="${hour}"/>
                            </c:forEach>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="UntilHour"/></span>
                    </span>
                    <span class="relative input-hour">
                        <spf:select path="endTimeMinute" class="select-simple form-bottom-element form-right-element" data-container="body">
                            <c:forEach var="minute" begin="0" end="55" step="5">
                                <fmt:formatNumber value="${minute}" minIntegerDigits="2" var="minute"/>
                                <spf:option value="${minute}"/>
                            </c:forEach>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="UntilMinute"/></span>
                    </span>
                    <div class="clearfix"></div>
                    <span class="unit">
                        <spf:checkbox path="paymentConfirmed" id="paymentConfirmed"/>
                        <label class="checkbox" for="paymentConfirmed"><fmt:message key="Paid"/></label>
                    </span>
                    
                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                    <a href="/admin/bookings" class="btn btn-primary btn-block unit ajaxify"><fmt:message key="Cancel"/></a>
                </spf:form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
