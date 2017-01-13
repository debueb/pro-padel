<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <ol class="unit-2 breadcrumb">
            <li><a class="ajaxify" href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a class="ajaxify" href="/admin/events"><fmt:message key="Events"/></a></li>
            <li><a class="ajaxify" href="/admin/events/edit/${Event.id}">${Event.name}</a></li>
            <li class="active"><fmt:message key="GameSchedule"/></li>
        </ol>

        <div class="page-header"></div>
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="GameSchedule"/> ${Event.name}</h4>
            </div>
            <spf:form method="POST" role="form" modelAttribute="Model" action="/admin/events/edit/${Event.id}/schedule/gameschedule">
                <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>
                <div class="panel-body">
                    <div class="game-schedule">
                        <span class="game-schedule-title"></span>
                        <span class="game-schedule-date" class="relative"><fmt:message key="Date"/></span>
                        <span class="game-schedule-hour" class="relative"><fmt:message key="Hour"/></span>
                        <span class="game-schedule-minute" class="relative"><fmt:message key="Minute"/></span>
                    </div>
                    <c:choose>
                        <c:when test="${empty Model.list}">
                            <div class="text-center"><fmt:message key="NoGamesYet"/></div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="Game" items="${Model.list}" varStatus="status">
                                <div class="game-schedule">
                                    <spf:input type="hidden" path="list[${status.index}].id" value="${Game.id}"/>
                                    <span class="game-schedule-title">${Game}</span>
                                    <span class="game-schedule-date">
                                        <div class="datepicker-container">
                                            <div class="datepicker-text-container form-control">
                                                <span class="fa fa-calendar datepicker-icon"></span>
                                                <div class="datepicker-text"></div>
                                            </div>
                                            <spf:input type="hidden" path="list[${status.index}].startDate" class="datepicker-input form-control" value="${Game.startDate eq null ? Event.startDate : Game.startDate}"/>
                                            <div class="datepicker" data-show-on-init="false" data-allow-past="true"></div>
                                        </div>
                                    </span>

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
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    <button class="btn btn-primary btn-block btn-form-submit unit-2" type="submit"><fmt:message key="Save"/></button>
                    <a href="/admin/events" class="ajaxify btn btn-primary btn-block unit-2"><fmt:message key="Skip"/></a>
                </div>
            </spf:form>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
