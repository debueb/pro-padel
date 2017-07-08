<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <fmt:message var="Title" key="${empty Model.id ? 'NewEvent' : 'EditEvent'}"/>

        <div class="page-header"></div>
        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/events"><fmt:message key="Events"/></a></li>
            <li class="active">${Title}</li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${Title}</h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin unit" modelAttribute="Model">
                    <spf:input type="hidden" path="id"/>
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>

                    <div class="relative">
                        <fmt:message key="EventName" var="EventName"/>
                        <spf:input path="name" type="text" class="form-control form-top-element" placeholder="${EventName}"/>
                        <div class="explanation">${EventName}</div>
                    </div>

                    <div class="relative">
                        <spf:select path="eventGroup" class="select-simple form-control" data-style="form-center-element" data-container="body">
                            <spf:options items="${EventGroups}" itemLabel="name" itemValue="id"/>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="EventGroup"/></span>
                    </div>

                    <div class="relative">
                        <spf:textarea path="description" class="form-control form-center-element text-editor"/>
                        <div class="explanation"><fmt:message key="Description"/></div>
                    </div>

                    <div class="relative">
                        <spf:select path="gender" class="select-simple form-control select-toggle" data-style="form-center-element" data-container="body">
                            <c:forEach var="Gender" items="${Genders}">
                                <spf:option value="${Gender}"><fmt:message key="Mode_${Gender}"/></spf:option>
                            </c:forEach>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="Category"/></span>
                    </div>

                    <c:choose>
                        <c:when test="${empty Model.id}">
                            <div class="relative">
                                <spf:select path="eventType" class="select-simple form-control select-toggle" data-style="form-center-element" data-container="body">
                                    <c:forEach var="EventType" items="${EventTypes}">
                                        <spf:option value="${EventType}"><fmt:message key="${EventType}"/></spf:option>
                                    </c:forEach>
                                </spf:select>
                                <span class="explanation-select"><fmt:message key="Mode"/></span>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <spf:input type="hidden" path="eventType" class="select-toggle"/>
                        </c:otherwise>
                    </c:choose>

                    <div class="select-toggle-GroupKnockout select-toggle-GroupTwoRounds relative">
                        <spf:input path="numberOfGroups" type="number" class="form-control form-center-element" min="1"/>
                        <div class="explanation-select"><fmt:message key="NumberOfGroups"/></div>
                    </div>

                    <div class="select-toggle-GroupKnockout relative">
                        <spf:input path="numberOfWinnersPerGroup" type="number" class="form-control form-center-element"/>
                        <div class="explanation-select"><fmt:message key="NumberOfWinnersPerGroups"/></div>
                    </div>
                    
                    <div class="select-toggle-GroupTwoRounds relative">
                        <spf:input path="numberOfGroupsSecondRound" type="number" class="form-control form-center-element" min="1"/>
                        <div class="explanation-select"><fmt:message key="NumberOfGroupsSecondRound"/></div>
                    </div>

                    <div class="datepicker-container">
                        <div class="datepicker-text-container form-center-element">
                            <div class="datepicker-label"><fmt:message key="Start"/></div>
                            <span class="fa fa-calendar datepicker-icon"></span>
                            <div class="datepicker-text"></div>
                        </div>
                        <spf:input type="hidden" path="startDate" class="datepicker-input form-control" value="${Model.startDate}" />
                        <div class="datepicker" data-show-on-init="false" data-allow-past="true"></div>
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
                            <c:forEach var="minute" begin="0" end="30" step="30">
                                <fmt:formatNumber value="${minute}" minIntegerDigits="2" var="minute"/>
                                <spf:option value="${minute}"/>
                            </c:forEach>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="FromMinute"/></span>
                    </span>

                    <div class="clearfix"></div>

                    <div class="datepicker-container">
                        <div class="datepicker-text-container form-center-element">
                            <div class="datepicker-label"><fmt:message key="End"/></div>
                            <span class="fa fa-calendar datepicker-icon"></span>
                            <div class="datepicker-text"></div>
                        </div>
                        <spf:input type="hidden" path="endDate" class="datepicker-input form-control" value="${Model.endDate}"/>
                        <div class="datepicker" data-show-on-init="false" data-allow-past="true"></div>
                    </div>

                    <div class="select-toggle-SingleRoundRobin select-toggle-GroupKnockout select-toggle-GroupTwoRounds select-toggle-Knockout select-toggle-CommunityRoundRobin select-toggle-PullRoundRobin relative">
                        <spf:input path="maxNumberOfParticipants" type="number" class="form-control form-center-element"/>
                        <div class="explanation-select"><fmt:message key="MaxNumberOfParticipants"/></div>
                    </div>
                    <div class="select-toggle-SingleRoundRobin select-toggle-GroupKnockout select-toggle-GroupTwoRounds select-toggle-Knockout select-toggle-CommunityRoundRobin select-toggle-PullRoundRobin relative" data-style="form-center-element">
                        <fmt:message key="CurrentlySelected" var="CurrentlySelected"/>
                        <fmt:message key="PleaseChoose" var="EmptyTitle"/>
                        <fmt:message key="ErrorText" var="ErrorText"/>
                        <fmt:message key="Search" var="SearchPlaceholder"/>
                        <fmt:message key="StatusInitialized" var="StatusInitialized"/>
                        <fmt:message key="SearchNoResults" var="SearchNoResults"/>
                        <fmt:message key="StatusSearching" var="StatusSearching"/>
                        <spf:select 
                            path="participants" 
                            class="form-control form-center-element select-ajax-search"
                            multiple="multiple"
                            data-container="body" 
                            data-live-search="true"
                            data-abs-locale-currently-selected='${CurrentlySelected}'
                            data-abs-locale-empty-title='${EmptyTitle}'
                            data-abs-locale-error-text='${ErrorText}'
                            data-abs-locale-search-placeholder='${SearchPlaceholder}'
                            data-abs-locale-status-initialized='${StatusInitialized}'
                            data-abs-locale-search-no-results='${SearchNoResults}'
                            data-abs-locale-status-searching='${StatusSearching}'
                            data-abs-param-type-source="#eventType"
                            data-abs-ajax-url="/api/participants/options">
                            <fmt:message key="PleaseChoose" var="PleaseChoose"/>
                            <spf:options items="${Model.participants}" itemValue="UUID"/>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="Participants"/></span>
                    </div>
                    <div class="select-toggle-SingleRoundRobin select-toggle-GroupKnockout select-toggle-GroupTwoRounds select-toggle-Knockout select-toggle-PullRoundRobin">
                        <%-- Zahlungsmethoden --%>
                        <div class="relative">
                            <spf:select path="paymentMethods" class="select-multiple form-control" data-style="form-center-element" data-container="body">
                                <c:forEach var="PaymentMethod" items="${PaymentMethods}">
                                    <fmt:message key="${PaymentMethod}" var="Label"/>
                                    <spf:option value="${PaymentMethod}" label="${Label}"/>
                                </c:forEach>
                            </spf:select>
                            <span class="explanation-select"><fmt:message key="PaymentMethods"/></span>
                        </div>

                        <%-- Price --%>
                        <div class="relative"> 
                            <spf:input path="price" type="text" class="form-control form-center-element" placeholder="20.00" data-valid-chars="[0-9\.]"/>
                            <span class="explanation"><fmt:message key="PricePerBooking"/></span>
                        </div>
                        
                        <div class="relative"> 
                            <spf:input path="confirmationMailRemark" type="text" class="form-control form-center-element" placeholder=""/>
                            <span class="explanation"><fmt:message key="ConfirmationMailRemark"/></span>
                        </div>
                    </div>
                    <spf:input type="hidden" path="currency" value="EUR"/>

                    <div class="relative">
                        <fmt:message key="LocationDesc" var="LocationDesc"/>
                        <spf:input path="location" type="text" class="form-control form-center-element" placeholder="${LocationDesc}"/>
                        <div class="explanation"><fmt:message key="Location"/></div>
                    </div>

                    <div class="relative">
                        <spf:input path="numberOfSets" type="number" class="form-control form-center-element" min="1" max="5" />
                        <div class="explanation-select"><fmt:message key="MaxNumberOfSets"/></div>
                    </div>

                    <div class="relative">
                        <spf:input path="numberOfGamesPerSet" type="number" class="form-control form-center-element" min="1" max="99"/>
                        <div class="explanation-select"><fmt:message key="MaxNumberOfGamesPerSet"/></div>
                    </div>

                    <div class="relative">
                        <spf:input path="numberOfGamesInFinalSet" type="number" class="form-control form-bottom-element" min="1" max="99"/>
                        <div class="explanation-select"><fmt:message key="MaxNumberOfGamesInFinalSet"/></div>
                    </div>


                    <div class="unit">
                        <spf:checkbox path="active" id="active"/><label for="active"><fmt:message key="Active"/>&nbsp;(<fmt:message key="PubliclyAvailable"/>)</label>
                    </div>
                    <div class="unit">
                        <spf:checkbox path="allowSignup" id="allowSignup"/><label for="allowSignup"><fmt:message key="AllowEventSignup"/></label>
                    </div>
                    <div class="unit">
                        <spf:checkbox path="showParticipants" id="showParticipants"/><label for="showParticipants"><fmt:message key="ShowParticipants"/></label>
                    </div>
                    <div class="unit">
                        <spf:checkbox path="showGames" id="showGames"/><label for="showGames"><fmt:message key="ShowGames"/></label>
                    </div>
                    <div class="unit">
                        <spf:checkbox path="showScores" id="showScores"/><label for="showScores"><fmt:message key="ShowScores"/></label>
                    </div>
                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                    <c:if test="${not empty Model.id and (Model.eventType eq 'Knockout')}">
                        <a class="btn btn-primary btn-block unit" href="${contextPath}/admin/events/edit/${Model.id}/draws"><fmt:message key="ToDraws"/></a>
                    </c:if>
                    <c:if test="${not empty Model.id and (Model.eventType eq 'GroupKnockout')}">
                        <a class="btn btn-primary btn-block unit" href="${contextPath}/admin/events/edit/${Model.id}/groupdraws"><fmt:message key="ToGroupDraws"/></a>
                        <a class="btn btn-primary btn-block unit" href="${contextPath}/admin/events/edit/${Model.id}/groupschedule"><fmt:message key="ToGameSchedule"/></a>
                    </c:if>
                    <c:if test="${not empty Model.id and (Model.eventType eq 'GroupTwoRounds')}">
                        <a class="btn btn-primary btn-block unit" href="${contextPath}/admin/events/edit/${Model.id}/groupdraws"><fmt:message key="ToGroupDraws"/> <fmt:message key="Round"/> 1</a>
                        <a class="btn btn-primary btn-block unit" href="${contextPath}/admin/events/edit/${Model.id}/groupdraws/round/1"><fmt:message key="ToGroupDraws"/> <fmt:message key="Round"/> 2</a>
                    </c:if>
                    <c:if test="${not empty Model.id and (Model.eventType eq 'PullRoundRobin' or Model.eventType eq 'SingleRoundRobin')}">
                        <a class="btn btn-primary btn-block unit" href="${contextPath}/admin/events/edit/${Model.id}/gameschedule"><fmt:message key="ToGameSchedule"/></a>
                    </c:if>
                </spf:form>
            </div>
        </div>    
    </div>
</div>

<%-- include summernote in body when requested via ajax, otherwise after footer (where jquery is added) --%>
<c:if test="${not empty header['x-requested-with']}">
    <jsp:include page="/jsp/admin/include/text-editor.jsp"/>
</c:if>
<jsp:include page="/jsp/include/footer.jsp"/>
<c:if test="${empty header['x-requested-with']}">
    <jsp:include page="/jsp/admin/include/text-editor.jsp"/>
</c:if>
