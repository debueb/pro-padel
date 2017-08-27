<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>
        <div class="page-header"></div>

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
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model" id="news-form">
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
                        <spf:input path="title" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="Title"/></div>
                    </div>
                    
                    <div class="select-toggle-Page select-toggle-Blog select-toggle-Events relative">
                        <spf:input path="urlTitle" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="URLTitle"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="seoTitle" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="SEOTitle"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:input type="text" path="shortDescription" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="ShortDescription"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:select path="seoRobots" type="text" class="form-control form-center-element select-simple" data-container="body">
                            <spf:option value="index, follow">index, follow</spf:option>
                            <spf:option value="index, nofollow">index, nofollow</spf:option>
                            <spf:option value="noindex, follow">noindex, follow</spf:option>
                            <spf:option value="noindex, nofollow">noindex, nofollow</spf:option>
                        </spf:select>
                        <div class="explanation-select"><fmt:message key="SEORobots"/></div>
                    </div>
                        
                    <div class="select-toggle-Events select-toggle-Bookings select-toggle-Ranking select-toggle-MatchOffers select-toggle-Staff relative">
                        <div class="relative">
                            <spf:textarea path="description" class="form-control form-center-element text-editor"/>
                            <div class="explanation"><fmt:message key="Description"/></div>
                        </div>
                    </div>
                    
                    <div class="select-toggle-Link relative">
                        <spf:input path="url" type="text" class="form-control form-center-element" placeholder="http://www.google.de"/>
                        <div class="explanation"><fmt:message key="URL"/></div>
                    </div>
                    
                    <div class="select-toggle-Events relative">
                        <spf:select path="eventGroups" type="text" class="form-control form-center-element select-multiple" multiple="multiple" data-container="body">
                            <spf:options items="${EventGroups}" itemLabel="name" itemValue="id"/>
                        </spf:select>
                        <div class="explanation-select"><fmt:message key="EventGroups"/></div>
                    </div>
                    
                    <spf:select path="iconName" type="text" class="form-control form-bottom-element select-simple" data-live-search="true" data-container="body">
                        <option value=""><fmt:message key="NoIcon"/></option>
                        <c:forEach var="IconName" items="${FontAwesomeIconNames}">
                            <spf:option data-icon="fa-${IconName}" value="${IconName}">${IconName}</spf:option>
                        </c:forEach>
                    </spf:select>
                    
                    <div class="select-toggle-Blog select-toggle-Bookings select-toggle-Events select-toggle-Link select-toggle-MatchOffers select-toggle-Page select-toggle-Ranking select-toggle-Staff">
                        <div>
                            <spf:checkbox path="showInMenu" />
                            <label class="checkbox" for="showInMenu1"><fmt:message key="ShowInMenu"/></label>
                        </div>
                        <c:if test="${isRootModule}">
                            <div>
                                <spf:checkbox path="showOnHomepage" />
                                <label class="checkbox" for="showOnHomepage1"><fmt:message key="ShowOnHomepage"/></label>
                            </div>

                            <div>
                                <spf:checkbox path="showInFooter" />
                                <label class="checkbox" for="showInFooter1"><fmt:message key="ShowInFooter"/></label>
                            </div>
                        </c:if>
                    </div>
                    <button class="btn btn-primary btn-block btn-form-submit unit-2" type="submit"><fmt:message key="Save"/></button>
                    <c:if test="${Model.moduleType == 'Page' || Model.moduleType == 'HomePage' || Model.moduleType == 'LandingPage'}">
                        <a class="btn btn-primary btn-block" href="/admin/general/modules/page/${Model.id}"><fmt:message key="ManageEntries"/></a>
                    </c:if>
                    <c:if test="${Model.moduleType == 'Blog'}">
                        <a class="btn btn-primary btn-block" href="/admin/general/modules/blog/${Model.id}"><fmt:message key="ManageBlogEntries"/></a>
                    </c:if>
                    <c:if test="${not empty Model.id}">
                        <a href="/admin/general/modules/${Model.id}/delete" class="btn btn-danger btn-block unit-2"><fmt:message key="Delete"/></a>
                    </c:if>
                    <a class="btn btn-primary btn-block btn-back unit-2"><fmt:message key="Cancel"/></a>

                </spf:form>
            </div>
        </div>
    </div></div>

<jsp:include page="/WEB-INF/jsp/admin/include/text-editor.jsp"/>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
