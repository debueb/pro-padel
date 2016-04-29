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
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <spf:input type="hidden" path="id"/>
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>
                    
                    <div class="relative">
                        <fmt:message key="EventName" var="EventName"/>
                        <spf:input path="name" type="text" class="form-control form-top-element" placeholder="${EventName}"/>
                        <div class="explanation">${EventName}</div>
                    </div>
                    
                    
                    <spf:select path="eventType" class="select-simple form-control" data-style="form-center-element" data-container="body">
                        <spf:options items="${EventTypes}"/>
                    </spf:select>
                    
                    <spf:select path="gender" class="select-simple form-control" data-style="form-center-element" data-container="body">
                        <spf:options items="${Genders}"/>
                    </spf:select>
                    
                    <div class="relative">
                        <fmt:message key="NumberOfGroups" var="NumberOfGroups"/>
                        <spf:input path="numberOfGroups" type="number" class="form-control form-center-element" placeholder="${NumberOfGroups}"/>
                        <div class="explanation">${NumberOfGroups}</div>
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

                    <fmt:message key="Participants" var="Participants"/>
                    <spf:select path="participants" class="select-multiple show-tick form-control" data-style="form-bottom-element" title="${Participants}" multiple="true" data-live-search="true" data-container="body">
                        <fmt:message key="Participants" var="Label"/>
                        <optgroup label="${Label}">
                            <spf:options items="${EventParticipants}" itemValue="id"/>
                        </optgroup>
                        <fmt:message key="Teams" var="Label"/>
                        <optgroup label="${Label}">
                            <spf:options items="${AllTeams}" itemValue="id"/>
                        </optgroup>
                        <fmt:message key="Players" var="Label"/>
                        <optgroup label="${Label}" disabled>
                            <spf:options items="${AllPlayers}" itemValue="id"/>
                        </optgroup>
                    </spf:select>

                    <div class="unit">
                        <spf:checkbox path="active" id="active"/><label for="active"><fmt:message key="Active"/>&nbsp;(<fmt:message key="PubliclyAvailable"/>)</label>
                    </div>
                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                    <c:if test="${not empty Model.id and Model.eventType eq 'Knockout'}">
                        <a class="btn btn-primary btn-block unit" href="${contextPath}/admin/events/edit/${Model.id}/draws"><fmt:message key="ToDraws"/></a>
                    </c:if>
                </spf:form>
            </div>
        </div>    
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
