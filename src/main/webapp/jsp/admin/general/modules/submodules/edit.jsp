<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        <div class="panel panel-info">
            <div class="panel-heading">
                <fmt:message var="AddSubModule" key="AddSubModule"/>
                <fmt:message var="EditSubModule" key="EditSubModule"/>
                <h4>${Parent.title} ${empty Model.id ? AddSubModule : EditSubModule}</h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin summernote-form" role="form" modelAttribute="Model" id="news-form">
                    <spf:input type="hidden" path="id"/>
                    <spf:input type="hidden" path="position"/>
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>

                    <div class="relative">
                        <c:if test="${not empty Model.id}">
                            <spf:input type="hidden" path="moduleType"/>
                        </c:if>
                        <spf:select disabled="${not empty Model.id}" path="moduleType" type="text" class="form-control form-top-element select-simple select-toggle" data-container="body">
                            <c:forEach var="ModuleType" items="${ModuleTypes}">
                                <spf:option value="${ModuleType}"><fmt:message key="${ModuleType}"/></spf:option>
                            </c:forEach>
                        </spf:select>
                        <div class="explanation-select"><fmt:message key="Type"/></div>
                    </div>
                    
                    <div class="relative">
                        <fmt:message var="Title" key="Title"/>
                    <spf:input path="title" type="text" class="form-control form-center-element" placeholder="${Title}"/>
                        <div class="explanation"><fmt:message key="Title"/></div>
                    </div>
                    
                    <div class="select-toggle-Events select-toggle-Bookings select-toggle-Ranking select-toggle-MatchOffers select-toggle-Staff relative">
                        <spf:input path="description" type="hidden" id="summernote-input"/>
                        <div class="form-cotrol form-center-element">
                            <div id="summernote">${Model.description}</div>
                        </div>
                        <div class="explanation"><fmt:message key="Description"/></div>
                    </div>
                    
                    <div class="select-toggle-Events relative">
                        <spf:select path="eventTypes" type="text" class="form-control form-center-element select-multiple" multiple="multiple" data-container="body">
                            <c:forEach var="EventType" items="${EventTypes}">
                                <spf:option value="${EventType}"><fmt:message key="${EventType}"/></spf:option>
                            </c:forEach>
                        </spf:select>
                        <div class="explanation-select"><fmt:message key="EventType"/></div>
                    </div>
                    
                    <spf:select path="iconName" type="text" class="form-control form-bottom-element select-simple" data-live-search="true" data-container="body">
                        <c:forEach var="IconName" items="${FontAwesomeIconNames}">
                            <spf:option data-icon="fa-${IconName}" value="${IconName}">${IconName}</spf:option>
                        </c:forEach>
                    </spf:select>
                    
                    <button class="btn btn-primary btn-block btn-form-submit unit-2" type="submit"><fmt:message key="Save"/></button>
                    <c:if test="${Model.moduleType == 'Page'}">
                        <a class="btn btn-primary btn-block ajaxify" href="/admin/general/modules/page/${Model.id}"><fmt:message key="ManageEntries"/></a>
                    </c:if>
                    <a class="btn btn-primary btn-block btn-back unit-2 ajaxify"><fmt:message key="Cancel"/></a>

                </spf:form>
            </div>
        </div>
    </div></div>

<jsp:include page="/jsp/admin/include/summernote.jsp"/>
<jsp:include page="/jsp/include/footer.jsp"/>
