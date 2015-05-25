<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="${empty Model.id ? 'NewMatchOffer' : 'EditMatchOffer'}"/></h4></div>
            <div class="panel-body">
                <c:if test="${empty Model.id}"><p>
                        <fmt:message key="NewMatchOfferDesc"/></p>
                    </c:if>
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

                    <%-- Start --%>
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

                    <%-- Dauer --%>
                    <div class="relative"> 
                        <spf:select path="duration" class="select-simple form-center-element form-control">
                            <spf:option value="60">60 <fmt:message key="Minutes"/></spf:option>
                            <spf:option value="90">90 <fmt:message key="Minutes"/></spf:option>
                            <spf:option value="120">120 <fmt:message key="Minutes"/></spf:option>
                            <spf:option value="150">150 <fmt:message key="Minutes"/></spf:option>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="Duration"/></span>
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

                    <%-- Min. Teilnehmer --%>
                    <div class="input-group">
                        <span class="input-group-btn">
                            <button type="button" class="btn btn-default btn-plus-minus form-control form-center-element form-left-element" data-type="minus" data-field="minPlayersCount">
                                <span class="fa fa-minus"></span>
                            </button>
                        </span>
                        <span class="relative">
                            <spf:input type="text" path="minPlayersCount" class="form-control text-center input-plus-minus form-center-element" min="4" max="8"/>
                            <span class="explanation"><fmt:message key="MinPlayersCount"/></span>
                        </span>
                        <span class="input-group-btn">
                            <button type="button" class="btn btn-default btn-plus-minus form-control form-center-element form-right-element " data-type="plus" data-field="minPlayersCount">
                                <span class="fa fa-plus"></span>
                            </button>
                        </span>
                    </div>

                    <%-- Max. Teilnehmer --%>
                    <div class="input-group">
                        <span class="input-group-btn">
                            <button type="button" class="btn btn-default btn-plus-minus form-control form-center-element form-left-element" data-type="minus" data-field="maxPlayersCount">
                                <span class="fa fa-minus"></span>
                            </button>
                        </span>
                        <span class="relative">
                            <spf:input type="text" path="maxPlayersCount" class="form-control text-center input-plus-minus form-center-element" min="4" max="8"/>
                            <span class="explanation"><fmt:message key="MaxPlayersCount"/></span>
                        </span>
                        <span class="input-group-btn">
                            <button type="button" class="btn btn-default btn-plus-minus form-control form-center-element form-right-element" data-type="plus" data-field="maxPlayersCount">
                                <span class="fa fa-plus"></span>
                            </button>
                        </span>
                    </div>

                    <%-- Teilnehmer --%>
                    <span class="relative block">
                        <fmt:message key="Participants" var="Participants"/>
                        <spf:select path="players" class="select-multiple show-tick form-control" data-style="form-bottom-element" title="${Participants}" multiple="true" data-live-search="true">
                            <spf:options items="${Players}" itemValue="id"/>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="Participants"/></span>
                    </span>

                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                    <a class="btn btn-primary btn-block unit" href="/matchoffers"><fmt:message key="Cancel"/></a>
                    <c:if test="${not empty Model.id}">
                        <a class="btn btn-primary btn-block unit" href="/matchoffers/${Model.id}/delete"><fmt:message key="Delete"/></a>
                    </c:if>
                </spf:form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
