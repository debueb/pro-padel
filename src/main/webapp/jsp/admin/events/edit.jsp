<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        <div class="panel panel-info">
            <div class="panel-heading">
                <fmt:message key="NewEvent" var="NewEvent"/>
                <fmt:message key="EditEvent" var="EditEvent"/>
                <h4>${empty Model.id ? NewEvent : EditEvent}</h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin summernote-form unit" modelAttribute="Model">
                    <spf:input type="hidden" path="id"/>
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>
                    
                    <div class="relative">
                        <fmt:message key="EventName" var="EventName"/>
                        <spf:input path="name" type="text" class="form-control form-top-element" placeholder="${EventName}"/>
                        <div class="explanation">${EventName}</div>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="description" type="hidden" id="summernote-input"/>
                        <div class="form-center-element">
                            <div id="summernote">${Model.description}</div>
                        </div>
                        <div class="explanation"><fmt:message key="Description"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:select path="gender" class="select-simple form-control" data-style="form-center-element" data-container="body">
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
                            <spf:input type="hidden" path="eventType"/>
                        </c:otherwise>
                    </c:choose>
                    
                    <div class="select-toggle-GroupKnockout relative">
                        <spf:input path="numberOfGroups" type="number" class="form-control form-center-element" min="1"/>
                        <div class="explanation-select"><fmt:message key="NumberOfGroups"/></div>
                    </div>
                    
                    <div class="select-toggle-GroupKnockout relative">
                        <spf:input path="numberOfWinnersPerGroup" type="number" class="form-control form-center-element"/>
                        <div class="explanation-select"><fmt:message key="NumberOfWinnersPerGroups"/></div>
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

                    <div class="datepicker-container">
                        <div class="datepicker-text-container form-center-element">
                            <div class="datepicker-label"><fmt:message key="End"/></div>
                            <span class="fa fa-calendar datepicker-icon"></span>
                            <div class="datepicker-text"></div>
                        </div>
                        <spf:input type="hidden" path="endDate" class="datepicker-input form-control" value="${Model.endDate}"/>
                        <div class="datepicker" data-show-on-init="false" data-allow-past="true"></div>
                    </div>
                        
                    <div class="relative">
                        <fmt:message key="LocationDesc" var="LocationDesc"/>
                        <spf:input path="location" type="text" class="form-control form-center-element" placeholder="${LocationDesc}"/>
                        <div class="explanation"><fmt:message key="Location"/></div>
                    </div>
                    
                    <div class="select-toggle-GroupKnockout relative">
                        <spf:input path="numberOfSets" type="number" class="form-control form-center-element"/>
                        <div class="explanation-select"><fmt:message key="MaxNumberOfSets"/></div>
                    </div>
                    
                    <div class="select-toggle-GroupKnockout relative">
                        
                        <spf:input path="numberOfGamesPerSet" type="number" class="form-control form-center-element"/>
                        <div class="explanation-select"><fmt:message key="MaxNumberOfGamesPerSet"/></div>
                    </div>
                    
                    <div class="select-toggle-GroupKnockout relative">
                        <spf:input path="numberOfGamesInFinalSet" type="number" class="form-control form-center-element"/>
                        <div class="explanation-select"><fmt:message key="MaxNumberOfGamesInFinalSet"/></div>
                    </div>

                    <div class="relative">
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
                            data-abs-ajax-url="/api/teams/options">
                            <fmt:message key="PleaseChoose" var="PleaseChoose"/>
                            <spf:options items="${Model.participants}" itemValue="UUID"/>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="Participants"/></span>
                    </div>
                    
                    <div class="relative">
                        <spf:select path="calendarConfig" class="select-simple form-control" data-style="form-bottom-element" data-container="body">
                            <option value=""><fmt:message key="PleaseChoose"/></option>
                            <spf:options itemValue="id" items="${CalendarConfigs}"/>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="CalendarConfigOffer"/></span>
                    </div>
                    
                    <div class="alert alert-info unit"><fmt:message key="NewEventDesc"/></div>
                    
                    <div class="unit">
                        <spf:checkbox path="active" id="active"/><label for="active"><fmt:message key="Active"/>&nbsp;(<fmt:message key="PubliclyAvailable"/>)</label>
                    </div>
                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                    <c:if test="${not empty Model.id and (Model.eventType eq 'Knockout')}">
                        <a class="btn btn-primary btn-block unit" href="${contextPath}/admin/events/edit/${Model.id}/draws"><fmt:message key="ToDraws"/></a>
                    </c:if>
                    <c:if test="${not empty Model.id and (Model.eventType eq 'GroupKnockout')}">
                        <a class="btn btn-primary btn-block unit" href="${contextPath}/admin/events/edit/${Model.id}/groupdraws"><fmt:message key="ToGroupDraws"/></a>
                        <a class="btn btn-primary btn-block unit" href="${contextPath}/admin/events/edit/${Model.id}/gameschedule"><fmt:message key="ToGameSchedule"/></a>
                    </c:if>
                </spf:form>
            </div>
        </div>    
    </div>
</div>

<%-- include summernote in body when requested via ajax, otherwise after footer (where jquery is added) --%>
<c:if test="${not empty header['x-requested-with']}">
    <jsp:include page="/jsp/admin/include/summernote.jsp"/>
</c:if>
<jsp:include page="/jsp/include/footer.jsp"/>
<c:if test="${empty header['x-requested-with']}">
    <jsp:include page="/jsp/admin/include/summernote.jsp"/>
</c:if>
