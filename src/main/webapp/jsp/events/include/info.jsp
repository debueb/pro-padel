<%@include file="/jsp/include/include.jsp"%>

<c:if test="${not empty Model}">
    <div class="panel panel-info">
        <div class="panel-heading">
            <h4> ${Model.name}</h4>
        </div>
        <div class="panel-body">
            <div class="container-fluid">
                <div class="col-xs-4 text-right">
                    <fmt:message key="Date"/>:
                </div>
                <div class="col-xs-8">
                    <joda:format value="${Model.startDate}" pattern="EEE, dd. MMM yyyy"/>
                    <c:if test="${Model.startDate ne Model.endDate}">
                        - <joda:format value="${Model.endDate}" pattern="EEE, dd. MMM yyyy"/>
                    </c:if>
                </div>
                <div class="col-xs-4 text-right">
                    <fmt:message key="Category"/>:
                </div>
                <div class="col-xs-8">
                    <fmt:message key="Mode_${Model.gender}"/>
                </div>
                <div class="col-xs-4 text-right">
                    <fmt:message key="Mode"/>:
                </div>
                <div class="col-xs-8">
                    <fmt:message key="${Model.eventType}"/>
                </div>
                <c:if test="${not empty Model.location}">
                    <div class="col-xs-4 text-right">
                        <fmt:message key="Location"/>:
                    </div>
                    <div class="col-xs-8">
                        <a href="https://www.google.com/maps/search/${Model.location}" target="blank">${Model.location}</a>
                    </div>
                </c:if>
                <c:if test="${not empty Model.description and Model.description ne '<p><br></p>'}">
                    <div class="col-xs-4 text-right">
                        <fmt:message key="Description"/>:
                    </div>
                    <div class="col-xs-8">
                        ${Model.description}
                    </div>
                </c:if>
            </div>
            <div class="unit-2">
                <div class="list-group">
                    <c:if test="${Model.calendarConfig ne null}">
                        <jsp:include page="/jsp/include/list-group-item.jsp">
                            <jsp:param name="href" value="/events/event/${Model.id}/participate"/>
                            <jsp:param name="key" value="Participate"/>
                            <jsp:param name="icon" value="user-plus"/>
                        </jsp:include>
                    </c:if>
                    <c:choose>

                        <c:when test="${Model.eventType eq 'SingleRoundRobin'}">
                            <jsp:include page="/jsp/include/list-group-item.jsp">
                                <jsp:param name="href" value="/players/event/${Model.id}"/>
                                <jsp:param name="key" value="Players"/>
                                <jsp:param name="icon" value="user"/>
                            </jsp:include>
                            <jsp:include page="/jsp/include/list-group-item.jsp">
                                <jsp:param name="href" value="/teams/event/${Model.id}"/>
                                <jsp:param name="key" value="Teams"/>
                                <jsp:param name="icon" value="group"/>
                            </jsp:include>
                            <jsp:include page="/jsp/include/list-group-item.jsp">
                                <jsp:param name="href" value="/games/event/${Model.id}"/>
                                <jsp:param name="key" value="Games"/>
                                <jsp:param name="icon" value="dot-circle-o"/>
                            </jsp:include>
                            <jsp:include page="/jsp/include/list-group-item.jsp">
                                <jsp:param name="href" value="/scores/event/${Model.id}"/>
                                <jsp:param name="key" value="Score"/>
                                <jsp:param name="icon" value="list-ol"/>
                            </jsp:include>
                        </c:when>

                        <c:when test="${Model.eventType eq 'CommunityRoundRobin'}">
                            <jsp:include page="/jsp/include/list-group-item.jsp">
                                <jsp:param name="href" value="/events/event/${Model.id}/communities"/>
                                <jsp:param name="key" value="Communities"/>
                                <jsp:param name="icon" value="signing"/>
                            </jsp:include>
                            <jsp:include page="/jsp/include/list-group-item.jsp">
                                <jsp:param name="href" value="/events/event/${Model.id}/communitygames"/>
                                <jsp:param name="key" value="Games"/>
                                <jsp:param name="icon" value="list-ol"/>
                            </jsp:include>
                            <jsp:include page="/jsp/include/list-group-item.jsp">
                                <jsp:param name="href" value="/events/event/${Model.id}/score"/>
                                <jsp:param name="key" value="Score"/>
                                <jsp:param name="icon" value="trophy"/>
                            </jsp:include>
                        </c:when>
                        
                        <c:when test="${Model.eventType eq 'PullRoundRobin'}">
                            <jsp:include page="/jsp/include/list-group-item.jsp">
                                <jsp:param name="href" value="/players/event/${Model.id}"/>
                                <jsp:param name="key" value="Players"/>
                                <jsp:param name="icon" value="user"/>
                            </jsp:include>
                            <jsp:include page="/jsp/include/list-group-item.jsp">
                                <jsp:param name="href" value="/games/event/${Model.id}/all"/>
                                <jsp:param name="key" value="Games"/>
                                <jsp:param name="icon" value="dot-circle-o"/>
                            </jsp:include>
                            <jsp:include page="/jsp/include/list-group-item.jsp">
                                <jsp:param name="href" value="/scores/event/${Model.id}"/>
                                <jsp:param name="key" value="Score"/>
                                <jsp:param name="icon" value="list-ol"/>
                            </jsp:include>
                        </c:when>

                        <c:when test="${Model.eventType eq 'Knockout'}">
                            <jsp:include page="/jsp/include/list-group-item.jsp">
                                <jsp:param name="href" value="/events/event/${Model.id}/participants"/>
                                <jsp:param name="key" value="Participants"/>
                                <jsp:param name="icon" value="group"/>
                            </jsp:include>
                            <jsp:include page="/jsp/include/list-group-item.jsp">
                                <jsp:param name="href" value="/events/event/${Model.id}/knockoutgames"/>
                                <jsp:param name="key" value="KnockoutGames"/>
                                <jsp:param name="icon" value="trophy"/>
                            </jsp:include>
                        </c:when>

                        <c:when test="${Model.eventType eq 'GroupKnockout'}">
                            <jsp:include page="/jsp/include/list-group-item.jsp">
                                <jsp:param name="href" value="/events/event/${Model.id}/participants"/>
                                <jsp:param name="key" value="Participants"/>
                                <jsp:param name="icon" value="group"/>
                            </jsp:include>
                            <jsp:include page="/jsp/include/list-group-item.jsp">
                                <jsp:param name="href" value="/events/event/${Model.id}/groupgames"/>
                                <jsp:param name="key" value="GroupGames"/>
                                <jsp:param name="icon" value="list-ol"/>
                            </jsp:include>
                            <jsp:include page="/jsp/include/list-group-item.jsp">
                                <jsp:param name="href" value="/events/event/${Model.id}/knockoutgames"/>
                                <jsp:param name="key" value="KnockoutGames"/>
                                <jsp:param name="icon" value="trophy"/>
                            </jsp:include>
                        </c:when>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</c:if>