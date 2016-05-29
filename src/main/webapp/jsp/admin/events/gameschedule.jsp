<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="GameSchedule"/> ${Event.name}</h4>
            </div>
        </div>    

        <c:choose>
            <c:when test="${empty GroupGameMap}">
                <div class="alert alert-info"><fmt:message key="ConfigureGroupDrawsFirst"/></div>
            </c:when>
            <c:otherwise>
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>
                    <c:forEach var="GroupGameMapEntry" items="${GroupGameMap}">
                        <c:set var="GroupNumber" value="${GroupGameMapEntry.key}"/>
                        <div class="panel panel-info">
                            <div class="panel-heading"><fmt:message key="Group"/> ${GroupNumber+1}</div>
                            <div class="panel-body">
                                <div class="game-schedule">
                                    <span class="game-schedule-title"></span>
                                    <span class="game-schedule-hour" class="relative"><fmt:message key="Hour"/></span>
                                    <span class="game-schedule-minute" class="relative"><fmt:message key="Minute"/></span>
                                </div>
                                <c:forEach var="Game" items="${Model.list}" varStatus="status">
                                    <c:if test="${Game.groupNumber == GroupNumber}">
                                        <div class="game-schedule">
                                            <spf:input type="hidden" path="list[${status.index}].id" value="${Game.id}"/>
                                            <span class="game-schedule-title">${Game}</span>
                                            <span class="game-schedule-hour">
                                                <spf:select path="list[${status.index}].startTimeHour" class="select-simple" data-container="body">
                                                    <c:forEach var="hour" begin="0" end="23">
                                                        <fmt:formatNumber value="${hour}" minIntegerDigits="2" var="hour"/>
                                                        <spf:option value="${hour}"/>
                                                    </c:forEach>
                                                </spf:select>
                                            </span>
                                            <span class="game-schedule-minute">
                                                <spf:select path="list[${status.index}].startTimeMinute" class="select-simple" data-container="body">
                                                    <c:forEach var="minute" begin="0" end="55" step="5">
                                                        <fmt:formatNumber value="${minute}" minIntegerDigits="2" var="minute"/>
                                                        <spf:option value="${minute}"/>
                                                    </c:forEach>
                                                </spf:select>
                                            </span>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </div>
                    </c:forEach>
                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>