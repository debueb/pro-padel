<%@include file="/WEB-INF/jsp/include/include.jsp"%>

<c:if test="${not empty Model}">
    <div class="panel panel-info">
        <div class="panel-heading">
            <h4> ${Model.name}</h4>
        </div>
        <div class="panel-body">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-xs-4 text-right">
                        <fmt:message key="Date"/>
                    </div>
                    <div class="col-xs-8">
                        <joda:format value="${Model.startDate}" pattern="EEE, dd. MMM yyyy"/>
                        <c:if test="${Model.startDate ne Model.endDate}">
                            - <joda:format value="${Model.endDate}" pattern="EEE, dd. MMM yyyy"/>
                        </c:if>
                    </div>
                </div>
                    
                <c:if test="${not empty Model.startTime}">
                    <joda:format value="${Model.startTime}" pattern="HH:mm" var="startTimeFormatted"/>
                    <c:if test="${not startTimeFormatted eq '00:00'}">
                        <div class="row">
                            <div class="col-xs-4 text-right">
                            <fmt:message key="StartTime"/>
                            </div>
                            <div class="col-xs-8">
                                ${startTimeFormatted}
                            </div>
                        </div>
                    </c:if>
                </c:if>
                <div class="row">    
                    <div class="col-xs-4 text-right">
                        <fmt:message key="Category"/>
                    </div>
                    <div class="col-xs-8">
                        <fmt:message key="Mode_${Model.gender}"/>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-xs-4 text-right">
                        <fmt:message key="Mode"/>
                    </div>
                    <div class="col-xs-8">
                        <fmt:message key="${Model.eventType}"/>
                    </div>
                </div>
                
                <c:if test="${not empty Model.maxNumberOfParticipants and Model.maxNumberOfParticipants ne 0}">
                    <div class="row">
                        <div class="col-xs-4 text-right">
                            <fmt:message key="MaxNumberOfParticipants"/>
                        </div>
                        <div class="col-xs-8">
                            ${Model.maxNumberOfParticipants}
                        </div>
                    </div>
                </c:if>
                
                <c:if test="${not empty Model.location}">
                    <div class="row">
                        <div class="col-xs-4 text-right">
                            <fmt:message key="Location"/>
                        </div>
                        <div class="col-xs-8">
                            <a href="https://www.google.com/maps/search/${Model.location}" target="blank">${Model.location}</a>
                        </div>
                    </div>
                </c:if>
                
                <c:if test="${not empty Model.description and Model.description ne '<p><br></p>'}">
                    <div class="row">
                        <div class="col-xs-4 text-right">
                            <fmt:message key="Description"/>
                        </div>
                        <div class="col-xs-8">
                            ${Model.description}
                        </div>
                    </div>
                </c:if>
            </div>
            <div class="unit-2">
                <div class="list-group">
                    <c:if test="${Model.allowSignup}">
                        <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                            <jsp:param name="href" value="/events/bookings/${Model.id}/participate"/>
                            <jsp:param name="anchor" value="#after-info"/>
                            <jsp:param name="key" value="Participate"/>
                            <jsp:param name="icon" value="user-plus"/>
                        </jsp:include>
                    </c:if>
                    <c:choose>

                        <c:when test="${Model.eventType eq 'SingleRoundRobin'}">
                            <c:if test="${Model.showParticipants}">
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/players/event/${Model.id}"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="Players"/>
                                    <jsp:param name="icon" value="user"/>
                                </jsp:include>
                            </c:if>
                            <c:if test="${Model.showGames}">
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/games/event/${Model.id}"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="Games"/>
                                    <jsp:param name="icon" value="dot-circle-o"/>
                                </jsp:include>
                            </c:if>
                            <c:if test="${Model.showScores}">
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/scores/event/${Model.id}"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="Score"/>
                                    <jsp:param name="icon" value="list-ol"/>
                                </jsp:include>
                            </c:if>
                        </c:when>

                        <c:when test="${Model.eventType eq 'CommunityRoundRobin'}">
                            <c:if test="${Model.showGames}">
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/events/event/${Model.id}/communitygames"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="Games"/>
                                    <jsp:param name="icon" value="list-ol"/>
                                </jsp:include>
                            </c:if>
                            <c:if test="${Model.showScores}">
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/events/event/${Model.id}/score"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="Score"/>
                                    <jsp:param name="icon" value="trophy"/>
                                </jsp:include>
                            </c:if>
                        </c:when>
                        
                        <c:when test="${Model.eventType eq 'PullRoundRobin'}">
                            <c:if test="${Model.showParticipants}">
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/players/event/${Model.id}"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="Players"/>
                                    <jsp:param name="icon" value="user"/>
                                </jsp:include>
                            </c:if>
                            <c:if test="${Model.showGames}">
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/events/event/${Model.id}/pullgames"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="Games"/>
                                    <jsp:param name="icon" value="dot-circle-o"/>
                                </jsp:include>
                            </c:if>
                            <c:if test="${Model.showScores}">
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/scores/event/${Model.id}"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="Score"/>
                                    <jsp:param name="icon" value="list-ol"/>
                                </jsp:include>
                            </c:if>
                        </c:when>

                        <c:when test="${Model.eventType eq 'Knockout'}">
                            <c:if test="${Model.showParticipants}">
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/events/event/${Model.id}/participants"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="Participants"/>
                                    <jsp:param name="icon" value="group"/>
                                </jsp:include>
                            </c:if>
                            <c:if test="${Model.showGames}">
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/events/event/${Model.id}/knockoutgames"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="KnockoutGames"/>
                                    <jsp:param name="icon" value="trophy"/>
                                </jsp:include>
                            </c:if>
                        </c:when>

                        <c:when test="${Model.eventType eq 'GroupKnockout'}">
                            <c:if test="${Model.showParticipants}">
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/events/event/${Model.id}/participants"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="Participants"/>
                                    <jsp:param name="icon" value="group"/>
                                </jsp:include>
                            </c:if>
                            <c:if test="${Model.showGames}">
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/events/event/${Model.id}/groupgames"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="GroupGames"/>
                                    <jsp:param name="icon" value="list-ol"/>
                                </jsp:include>
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/events/event/${Model.id}/knockoutgames"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="KnockoutGames"/>
                                    <jsp:param name="icon" value="trophy"/>
                                </jsp:include>
                            </c:if>
                        </c:when>
                        
                        <c:when test="${Model.eventType eq 'GroupTwoRounds'}">
                            <c:if test="${Model.showParticipants}">
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/events/event/${Model.id}/participants"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="Participants"/>
                                    <jsp:param name="icon" value="group"/>
                                </jsp:include>
                            </c:if>
                            <c:if test="${Model.showGames}">
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/events/event/${Model.id}/groupgames/0"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="GroupGamesRoundOne"/>
                                    <jsp:param name="icon" value="list-ol"/>
                                </jsp:include>
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/events/event/${Model.id}/groupgames/1"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="GroupGamesRoundTwo"/>
                                    <jsp:param name="icon" value="trophy"/>
                                </jsp:include>
                            </c:if>
                        </c:when>
                        
                         <c:when test="${Model.eventType eq 'FriendlyGames'}">
                            <c:if test="${Model.showParticipants}">
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/events/event/${Model.id}/participants"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="Participants"/>
                                    <jsp:param name="icon" value="group"/>
                                </jsp:include>
                            </c:if>
                            <c:if test="${Model.showGames}">
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/events/event/${Model.id}/pullgames"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="Games"/>
                                    <jsp:param name="icon" value="dot-circle-o"/>
                                </jsp:include>
                            </c:if>
                            <c:if test="${Model.showScores}">
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/scores/event/${Model.id}"/>
                                    <jsp:param name="anchor" value="#after-info"/>
                                    <jsp:param name="key" value="Score"/>
                                    <jsp:param name="icon" value="list-ol"/>
                                </jsp:include>
                                <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                                    <jsp:param name="href" value="/ranking"/>
                                    <jsp:param name="key" value="Ranking"/>
                                    <jsp:param name="icon" value="sort-numeric-asc"/>
                                </jsp:include>
                            </c:if>
                        </c:when>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
    <div id="after-info"></div>
</c:if>