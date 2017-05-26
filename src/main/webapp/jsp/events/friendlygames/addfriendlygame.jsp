<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${Event}: <fmt:message key="AddGame"/></h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" modelAttribute="Model">
                    <div class="alert alert-danger"><spf:errors path="*"/></div>
                    <table class="table-full-width table-fixed">
                        <thead>
                        <th class="text-center"><fmt:message key="Team1"/></th>
                        <th class="text-center"><fmt:message key="Team2"/></th>
                        </thead>
                        <tbody>
                            <tr>
                                <fmt:message key="CurrentlySelected" var="CurrentlySelected"/>
                                <fmt:message key="PleaseChoose" var="EmptyTitle"/>
                                <fmt:message key="ErrorText" var="ErrorText"/>
                                <fmt:message key="Search" var="SearchPlaceholder"/>
                                <fmt:message key="StatusInitialized" var="StatusInitialized"/>
                                <fmt:message key="SearchNoResults" var="SearchNoResults"/>
                                <fmt:message key="StatusSearching" var="StatusSearching"/>

                                <td>
                                    <div class="relative">
                                        <spf:select 
                                            path="team1"
                                            class="form-control select-ajax-search" 
                                            data-container="body" 
                                            data-live-search="true"
                                            data-max-options="2"
                                            data-abs-locale-currently-selected='${CurrentlySelected}'
                                            data-abs-locale-empty-title='${EmptyTitle}'
                                            data-abs-locale-error-text='${ErrorText}'
                                            data-abs-locale-search-placeholder='${SearchPlaceholder}'
                                            data-abs-locale-status-initialized='${StatusInitialized}'
                                            data-abs-locale-search-no-results='${SearchNoResults}'
                                            data-abs-locale-status-searching='${StatusSearching}'
                                            data-abs-ajax-url="/api/players/options">
                                            <spf:options items="${Model.team1}" itemValue="UUID"/>
                                        </spf:select>
                                    </div>
                                </td>
                                <td>
                                    <div class="relative">
                                        <spf:select 
                                            path="team2" 
                                            class="form-control select-ajax-search" 
                                            data-container="body" 
                                            data-live-search="true"
                                            data-max-options="2"
                                            data-abs-locale-currently-selected='${CurrentlySelected}'
                                            data-abs-locale-empty-title='${EmptyTitle}'
                                            data-abs-locale-error-text='${ErrorText}'
                                            data-abs-locale-search-placeholder='${SearchPlaceholder}'
                                            data-abs-locale-status-initialized='${StatusInitialized}'
                                            data-abs-locale-search-no-results='${SearchNoResults}'
                                            data-abs-locale-status-searching='${StatusSearching}'
                                            data-abs-ajax-url="/api/players/options">
                                            <fmt:message key="ChoosePartner" var="ChoosePartner"/>
                                            <spf:options items="${Model.team2}" itemValue="UUID"/>
                                        </spf:select>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <button class="btn btn-primary btn-block btn-form-submit unit-2" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>    
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
