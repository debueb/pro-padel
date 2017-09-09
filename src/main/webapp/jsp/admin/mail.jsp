<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Email"/></h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" action="/admin/mail" modelAttribute="Model">
                    <div class="alert alert-info">
                        <fmt:message key="MailAllPlayersWarning"/>
                    </div>
                    <div class="alert alert-info unit-2">
                        <fmt:message key="EditEmailTemplates"/>
                    </div>
                    <div class="alert alert-danger"><spf:errors path="*"/></div>
                    <div class="unit-2">
                        <div class="relative">
                            <fmt:message key="CurrentlySelected" var="CurrentlySelected"/>
                            <fmt:message key="PleaseChoose" var="EmptyTitle"/>
                            <fmt:message key="ErrorText" var="ErrorText"/>
                            <fmt:message key="Search" var="SearchPlaceholder"/>
                            <fmt:message key="StatusInitialized" var="StatusInitialized"/>
                            <fmt:message key="SearchNoResults" var="SearchNoResults"/>
                            <fmt:message key="StatusSearching" var="StatusSearching"/>
                            <spf:select 
                                path="recipients" 
                                class="form-control form-top-element select-ajax-search"
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
                                <spf:options items="${Model.recipients}" itemValue="UUID"/>
                            </spf:select>
                            <span class="explanation-select"><fmt:message key="Players"/></span>
                        </div>
                        <div class="relative">
                            <spf:select 
                                path="templateId" 
                                class="form-control form-bottom-element select-simple select-toggle"
                                data-container="body">
                                <fmt:message key="TextEmail" var="TextEmail"/>
                                <spf:option value="TextEmail" label="${TextEmail}"/>
                                <c:forEach var="Template" items="${Templates}">
                                    <spf:option value="${Template.id}" label="${Template.name}${not Template.published ? ' - TEMPLATE IS A DRAFT. PUBLISH IT FIRST' : ''}" disabled="${not Template.published}"/>
                                </c:forEach>
                            </spf:select>
                            <span class="explanation-select"><fmt:message key="EmailTemplate"/></span>
                        </div>
                        <div class="select-toggle-TextEmail unit-2">
                            <div class="relative">
                                <fmt:message key="Subject" var="Subject"/>
                                <spf:input path="subject" type="text" class="form-control form-top-element" placeholder="${Subject}"/>
                                <div class="explanation">${Subject}</div>
                            </div>
                            <div class="relative">
                                <spf:textarea path="body" class="form-control form-bottom-element" rows="20"/>
                                <div class="explanation"><fmt:message key="Message"/></div>
                            </div>
                        </div>
                    </div>

                    <button class="btn btn-primary btn-block btn-form-submit unit-2" type="submit"><fmt:message key="Send"/></button>
                    <button class="btn btn-primary btn-block btn-form-submit unit-2 export-emails" type="submit"><fmt:message key="ExportEmailAddresses"/></button>
                </spf:form>
            </div>
        </div>
    </div></div>

<jsp:include page="/jsp/include/footer.jsp"/>
