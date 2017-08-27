<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <fmt:message var="Title" key="${empty Model.name ? 'NewSubscription' : 'EditSubscription'}"/>

        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/${moduleName}"><fmt:message key="Subscriptions"/></a></li>
            <li class="active">${Title}</li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${Title}</h4>
            </div>
            <div class="panel-body">

                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <spf:input type="hidden" path="id"/>
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>
                    
                    <%-- Name --%>
                    <div class="unit relative">
                        <spf:input path="name" type="text"  class="form-control form-top-element"/>
                        <div class="explanation"><fmt:message key="Name"/></div>
                    </div>
                    
                    <%-- Price --%>
                    <div class="relative"> 
                        <spf:input path="price" type="text" class="form-control form-center-element" placeholder="49.00" data-valid-chars="[0-9\.]"/>
                        <span class="explanation"><fmt:message key="Price"/></span>
                    </div>

                    <%-- Max Minutes Per Day --%>
                    <div class="relative"> 
                        <spf:input path="maxMinutesPerDay" type="text" class="form-control form-center-element" placeholder="90" data-valid-chars="[0-9]"/>
                        <span class="explanation"><fmt:message key="MaxMinutesPerDay"/></span>
                    </div>
                    
                    <%-- Max Minutes Per Week --%>
                    <div class="relative"> 
                        <spf:input path="maxMinutesPerWeek" type="text" class="form-control form-center-element" placeholder="630" data-valid-chars="[0-9]"/>
                        <span class="explanation"><fmt:message key="MaxMinutesPerWeek"/></span>
                    </div>

                    
                    <%-- Players --%>
                    <div>
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

                    <button class="btn btn-primary btn-block btn-form-submit unit-2" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>
    </div></div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
