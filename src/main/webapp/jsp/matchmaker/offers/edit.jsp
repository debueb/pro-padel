<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header">
            <h1><fmt:message key="${empty Model.id ? 'NewMatchOffer' : 'EditMatchOffer'}"/></h1>
        </div>

        <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
            <spf:input type="hidden" path="id"/>
            <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>

            <%-- Datum --%>
            <div class="datepicker-container">
                <div class="datepicker-text-container form-top-element">
                    <div class="datepicker-label"><fmt:message key="Date"/></div>
                    <span class="fa fa-calendar datepicker-icon"></span>
                    <div class="datepicker-text"></div>
                </div>
                <spf:input type="hidden" path="startDate" class="datepicker-input form-control" value="${Model.startDate}" />
                <div class="datepicker" data-show-on-init="false" data-allow-past="false"></div>
            </div>

            <%-- Von --%>
            <span class="relative input-hour">
                <spf:select path="startTimeHour" class="select-simple form-left-element form-center-element">
                    <c:forEach var="hour" begin="0" end="23">
                        <fmt:formatNumber value="${hour}" minIntegerDigits="2" var="hour"/>
                        <spf:option value="${hour}"/>
                    </c:forEach>
                </spf:select>
                <span class="explanation-select"><fmt:message key="Hour"/></span>
            </span>
            <div>
                <span class="relative input-hour">
                    <spf:select path="startTimeMinute" class="select-simple form-right-element form-center-element">
                        <c:forEach var="minute" begin="0" end="55" step="5">
                            <fmt:formatNumber value="${minute}" minIntegerDigits="2" var="minute"/>
                            <spf:option value="${minute}"/>
                        </c:forEach>
                    </spf:select>
                    <span class="explanation-select"><fmt:message key="Minute"/></span>
                </span>

                <div class="clearfix"></div>
            </div>
                
            <%-- Spielstärken --%>
            <span class="relative block">
                <fmt:message key="SkillLevel" var="SkillLevel"/>
                <spf:select path="skillLevels" class="select-multiple show-tick form-control" data-style="form-center-element" title="${SkillLevel}" multiple="true">
                    <c:forEach var="SkillLevel" items="${SkillLevels}">
                        <c:set var="selected" value="${fn:contains(Model.skillLevels, SkillLevel) ? 'selected': 'false'}"/>
                        <spf:option value="${SkillLevel}"><fmt:message key="${SkillLevel}"/></spf:option>
                    </c:forEach>
                </spf:select>
                <span class="explanation-select"><fmt:message key="SkillLevel"/></span>
            </span>

            <%-- Teilnehmer --%>
             <span class="relative block">
                <fmt:message key="Participants" var="Participants"/>
                <spf:select path="players" class="select-multiple show-tick form-control" data-style="form-bottom-element" title="${Participants}" multiple="true">
                    <spf:options items="${Players}" itemValue="id"/>
                </spf:select>
                <span class="explanation-select"><fmt:message key="Participants"/></span>
            </span>

            <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
            <a class="btn btn-primary btn-block unit" href="/matchmaker"><fmt:message key="Cancel"/></a>
            <c:if test="${not empty Model.id}">
                <a class="btn btn-primary btn-block unit" href="/matchmaker/offers/${Model.id}/delete"><fmt:message key="Delete"/></a>
            </c:if>
        </spf:form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
