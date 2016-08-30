<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <fmt:message var="AddModule" key="AddModule"/>
        <fmt:message var="EditModule" key="EditModule"/>
                
        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/general"><fmt:message key="General"/></a></li>
            <li><a href="/admin/general/modules"><fmt:message key="Modules"/></a></li>
            <li class="active">${empty Model.id ? AddModule : EditModule}</li>
        </ol>
        
        <div class="page-header"></div>
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${empty Model.id ? AddModule : EditModule}</h4>
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
                        <option value=""><fmt:message key="NoIcon"/></option>
                        <c:forEach var="IconName" items="${FontAwesomeIconNames}">
                            <spf:option data-icon="fa-${IconName}" value="${IconName}">${IconName}</spf:option>
                        </c:forEach>
                    </spf:select>
                    
                    <div>
                        <spf:checkbox path="showOnHomepage" />
                        <label class="checkbox" for="showOnHomepage1"><fmt:message key="ShowOnHomepage"/></label>
                    </div>
                    <div>
                        <spf:checkbox path="showInMenu" />
                        <label class="checkbox" for="showInMenu1"><fmt:message key="ShowInMenu"/></label>
                    </div>
                    <div>
                        <spf:checkbox path="showInFooter" />
                        <label class="checkbox" for="showInFooter1"><fmt:message key="ShowInFooter"/></label>
                    </div>
                    <button class="btn btn-primary btn-block btn-form-submit unit-2" type="submit"><fmt:message key="Save"/></button>
                    <c:if test="${Model.moduleType == 'Page'}">
                        <a class="btn btn-primary btn-block ajaxify" href="/admin/general/modules/page/${Model.id}"><fmt:message key="ManageEntries"/></a>
                    </c:if>
                    <c:if test="${not empty Model.id}">
                        <a href="/admin/general/modules/edit/${Model.id}/submodules" class="btn btn-primary btn-block unit-2 ajaxify"><fmt:message key="ManageSubmodules"/></a>
                    </c:if>
                    <a class="btn btn-primary btn-block btn-back unit-2 ajaxify"><fmt:message key="Cancel"/></a>

                </spf:form>
            </div>
        </div>
    </div></div>

<jsp:include page="/jsp/admin/include/summernote.jsp"/>
<jsp:include page="/jsp/include/footer.jsp"/>
