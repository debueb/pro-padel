<%@ page pageEncoding="UTF-8" contentType="text/html" %> 
<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="BookingSettings"/></h4>
            </div>
            <div class="panel-body">


                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <div class="alert alert-danger"><spf:errors path="*" cssClass="error"/></div>
                    <spf:input path="id" type="hidden"/>

                    <%-- Start Datum --%>
                    <div class="datepicker-container">
                        <div class="datepicker-text-container form-top-element">
                            <div class="datepicker-label"><fmt:message key="Start"/></div>
                            <span class="fa fa-calendar datepicker-icon"></span>
                            <div class="datepicker-text"></div>
                        </div>
                        <spf:input type="hidden" path="startDate" class="datepicker-input form-control" />
                        <div class="datepicker" data-show-on-init="false" data-allow-past="true"></div>
                    </div>

                    <%-- End Datum --%>
                    <div class="datepicker-container">
                        <div class="datepicker-text-container form-center-element">
                            <div class="datepicker-label"><fmt:message key="End"/></div>
                            <span class="fa fa-calendar datepicker-icon"></span>
                            <div class="datepicker-text"></div>
                        </div>
                        <spf:input type="hidden" path="endDate" class="datepicker-input form-control" />
                        <div class="datepicker" data-show-on-init="false" data-allow-past="true"></div>
                    </div>

                    <%-- Wochentage --%>
                    <jsp:include page="/jsp/admin/bookings/include/weekdays-input.jsp"/>

                    <%-- Feiertage --%>
                    <div class="relative">
                        <spf:select path="holidayKey" class="select-multiple form-control" data-style="form-center-element" data-container="body">
                            <spf:options items="${HolidayKeys}"/>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="Holidays"/></span>
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
                        <spf:select path="startTimeMinute" class="select-simple form-right-element form-center-element" data-container="body">
                            <c:forEach var="minute" begin="0" end="55" step="5">
                                <fmt:formatNumber value="${minute}" minIntegerDigits="2" var="minute"/>
                                <spf:option value="${minute}"/>
                            </c:forEach>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="FromMinute"/></span>
                    </span>

                    <%-- Bis --%>
                    <span class="relative input-hour">
                        <spf:select path="endTimeHour" class="select-simple form-left-element form-center-element" data-container="body">
                            <c:forEach var="hour" begin="0" end="23">
                                <fmt:formatNumber value="${hour}" minIntegerDigits="2" var="hour"/>
                                <spf:option value="${hour}"/>
                            </c:forEach>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="UntilHour"/></span>
                    </span>
                    <span class="relative input-hour">
                        <spf:select path="endTimeMinute" class="select-simple form-right-element form-center-element" data-container="body">
                            <c:forEach var="minute" begin="0" end="55" step="5">
                                <fmt:formatNumber value="${minute}" minIntegerDigits="2" var="minute"/>
                                <spf:option value="${minute}"/>
                            </c:forEach>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="UntilMinute"/></span>
                    </span>

                    <div class="clearfix"></div>

                    <%-- Angebote --%>
                    <div class="relative">
                        <spf:select path="offers" class="select-multiple form-control" data-style="form-center-element" data-container="body">
                            <spf:options items="${Offers}" itemValue="id"/>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="Offers"/></span>
                    </div>

                    <%-- Min Dauer --%>
                    <div class="relative"> 
                        <spf:select path="minDuration" class="select-simple form-center-element form-control" data-container="body">
                            <spf:option value="10">10 <fmt:message key="Minutes"/></spf:option>
                            <spf:option value="15">15 <fmt:message key="Minutes"/></spf:option>
                            <spf:option value="30">30 <fmt:message key="Minutes"/></spf:option>
                            <spf:option value="45">45 <fmt:message key="Minutes"/></spf:option>
                            <spf:option value="60">60 <fmt:message key="Minutes"/></spf:option>
                            <spf:option value="90">90 <fmt:message key="Minutes"/></spf:option>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="MinDuration"/></span>
                    </div>

                    <%-- Min Interval --%>
                    <div class="relative"> 
                        <spf:select path="minInterval" class="select-simple form-center-element form-control" data-container="body">
                            <spf:option value="10">10 <fmt:message key="Minutes"/></spf:option>
                            <spf:option value="15">15 <fmt:message key="Minutes"/></spf:option>
                            <spf:option value="30">30 <fmt:message key="Minutes"/></spf:option>
                            <spf:option value="45">45 <fmt:message key="Minutes"/></spf:option>
                            <spf:option value="60">60 <fmt:message key="Minutes"/></spf:option>
                            <spf:option value="90">90 <fmt:message key="Minutes"/></spf:option>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="MinInterval"/></span>
                    </div>

                    <%-- Zahlungsmethoden --%>
                    <div class="relative">
                        <spf:select path="paymentMethods" class="select-multiple form-control" data-style="form-center-element" data-container="body">
                            <spf:options items="${PaymentMethods}"/>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="PaymentMethods"/></span>
                    </div>

                    <%-- Price --%>
                    <div class="relative"> 
                        <spf:input path="basePrice" type="text" class="form-control form-bottom-element text-center" placeholder="20.00" data-valid-chars="[0-9\.]"/>
                        <span class="explanation"><fmt:message key="Price"/></span>
                    </div>
                    <spf:input type="hidden" path="currency" value="EUR"/>

                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
