<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        <div class="panel panel-info">
            <div class="panel-heading">
                <fmt:message key="NewTeam" var="NewTeam"/>
                <fmt:message key="EditTeam" var="EditTeam"/>
                <h4>${empty Model.name ? NewTeam : EditTeam}</h4>
            </div>
            <div class="panel-body">

                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <spf:input type="hidden" path="id"/>
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>
                    <div class="alert alert-info" role="alert"><fmt:message key="LeaveTeamNameEmptyForGeneratedTeamName"/></div>
                    
                    <div class="unit">
                        <div class="relative">
                            <spf:input path="name" type="text" class="form-control form-top-element"/>
                            <div class="explanation"><fmt:message key="TeamName"/></div>
                        </div>

                        <c:if test="${not empty Communities}">
                            <div class="relative">
                                <spf:select 
                                    path="community" 
                                    class="form-control select-simple form-center-element"
                                    data-container="body" 
                                    >
                                    <fmt:message key="None" var="None"/>
                                    <spf:option value="" label="${None}"/>
                                    <spf:options items="${Communities}" itemValue="id"/>
                                </spf:select>
                                <span class="explanation-select"><fmt:message key="Community"/></span>
                            </div>
                        </c:if>
                        
                        <fmt:message key="Players" var="Players"/>
                        <div class="relative">
                            <fmt:message key="CurrentlySelected" var="CurrentlySelected"/>
                            <fmt:message key="PleaseChoose" var="EmptyTitle"/>
                            <fmt:message key="ErrorText" var="ErrorText"/>
                            <fmt:message key="Search" var="SearchPlaceholder"/>
                            <fmt:message key="StatusInitialized" var="StatusInitialized"/>
                            <fmt:message key="SearchNoResults" var="SearchNoResults"/>
                            <fmt:message key="StatusSearching" var="StatusSearching"/>
                            <spf:select 
                                path="players" 
                                class="form-control form-bottom-element select-ajax-search"
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
                                data-abs-ajax-url="/api/players/options">
                                <fmt:message key="PleaseChoose" var="PleaseChoose"/>
                                <spf:options items="${Model.players}" itemValue="UUID"/>
                            </spf:select>
                            <span class="explanation-select"><fmt:message key="Players"/></span>
                        </div>
                    </div>

                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>
    </div></div>

<jsp:include page="/jsp/include/footer.jsp"/>
