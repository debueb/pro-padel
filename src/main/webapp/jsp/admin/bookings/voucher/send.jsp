<%@ page pageEncoding="UTF-8" contentType="text/html" %> 
<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <div class="page-header">
            <h1><fmt:message key="SendVoucher"/></h1>
        </div>
        <spf:form method="POST" class="form-signin" modelAttribute="Model">
            <div class="row-fluid">
                <div class="col-xs-12 col-sm-10 col-sm-offset-1">
                    <div class="alert alert-danger"><spf:errors path="*"/></div>
                    <div class="relative">
                        <select name="events" class="select-multiple form-control" data-style="form-top-element" multiple="true">
                            <c:forEach items="${Events}" var="Event">
                                <option value="${Event.id}">${Event}</option>
                            </c:forEach>
                        </select>
                        <span class="explanation-select"><fmt:message key="Events"/></span>
                    </div>
                    <span class="relative block">
                        <spf:select path="offers" class="select-multiple form-control" data-style="form-center-element" multiple="true">
                            <spf:options items="${Offers}" itemValue="id"/>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="Offers"/></span>
                    </span>
                    
                    <span class="relative block">
                        <spf:select path="duration" class="select-simple form-control" data-style="form-center-element">
                            <c:forEach var="Duration" items="${Durations}">
                                <option value="${Duration}" ${Duration == duration ? 'selected' : ''}>${Duration} <fmt:message key="Minutes"/></option>
                            </c:forEach>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="Duration"/></span>
                    </span>
                    
                    <div class="datepicker-container">
                        <div class="datepicker-text-container form-center-element">
                            <div class="datepicker-label"><fmt:message key="ValidUntil"/></div>
                            <span class="fa fa-calendar datepicker-icon"></span>
                            <div class="datepicker-text"></div>
                        </div>
                        <joda:format value="${validUntil}" pattern="yyyy-MM-dd" var="validUntil"/>
                        <spf:input type="hidden" path="validUntil" class="datepicker-input" value="${validUntil}" />
                        <div class="datepicker" data-show-on-init="false"></div>
                    </div>
                        
                    <%-- Wochentage --%>
                    <jsp:include page="/jsp/admin/bookings/include/weekdays-input.jsp"/>
                    
                    <%-- Von Uhrzeit--%>
                    <span class="relative input-hour">
                        <spf:select path="validFromHour" class="select-simple form-left-element form-center-element">
                             <c:forEach var="hour" begin="0" end="23">
                                 <fmt:formatNumber value="${hour}" minIntegerDigits="2" var="hour"/>
                                 <spf:option value="${hour}"/>
                             </c:forEach>
                         </spf:select>
                        <span class="explanation-select"><fmt:message key="FromHour"/></span>
                    </span>
                    <span class="relative input-hour">
                        <spf:select path="validFromMinute" class="select-simple form-right-element form-center-element">
                            <c:forEach var="minute" begin="0" end="55" step="5">
                                <fmt:formatNumber value="${minute}" minIntegerDigits="2" var="minute"/>
                                <spf:option value="${minute}"/>
                            </c:forEach>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="FromMinute"/></span>
                    </span>

                    <%-- Bis Uhrzeit --%>
                    <span class="relative input-hour">
                        <spf:select path="validUntilHour" class="select-simple form-left-element form-bottom-element">
                            <c:forEach var="hour" begin="0" end="23">
                                <fmt:formatNumber value="${hour}" minIntegerDigits="2" var="hour"/>
                                <spf:option value="${hour}"/>
                            </c:forEach>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="UntilHour"/></span>
                    </span>
                    <span class="relative input-hour">
                        <spf:select path="validUntilMinute" class="select-simple form-right-element form-bottom-element">
                            <c:forEach var="minute" begin="0" end="55" step="5">
                                <fmt:formatNumber value="${minute}" minIntegerDigits="2" var="minute"/>
                                <spf:option value="${minute}"/>
                            </c:forEach>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="UntilMinute"/></span>
                    </span>
                    
                    <div class="clearfix"></div>
                        <div class="unit">
                        <c:choose>
                            <c:when test="${empty Vouchers}">
                                <spf:button type="submit" class="btn btn-primary btn-block"><fmt:message key="SendVoucher"/></spf:button>
                            </c:when>
                            <c:otherwise>
                                <h4><fmt:message key="GeneratedVouchers"/>:</h4>
                                <textarea class="unit form-control" rows="${fn:length(Vouchers)}" varStatus="status"><c:forEach var="Voucher" items="${Vouchers}">${Voucher.UUID}${not status.last ? '&#13;&#10;' : ''}</c:forEach></textarea>
                                <a href="/admin/bookings/voucher/add" class="btn btn-primary unit"><fmt:message key="GenerateMore"/></a>
                                <a href="/admin/bookings/voucher" class="btn btn-primary unit ajaxify" style="margin-left: 10px;"><fmt:message key="ToOverview"/></a>
                            </c:otherwise>
                        </c:choose>
                    </div>
        
                </div>
            </div>
        </spf:form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
