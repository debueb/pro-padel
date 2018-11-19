<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>

        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/events"><fmt:message key="Events"/></a></li>
            <li><a href="/admin/events/edit/${Event.id}">${Event.name}</a></li>
            <li class="active"><fmt:message key="AddPlayerWithGuests"/></li>
        </ol>
        
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${Event}: <fmt:message key="AddPlayerWithGuests"/></h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" role="form" modelAttribute="Player">
                    <div class="alert alert-danger unit-2"><spf:errors path="*"/></div>
                    <div class="accordion unit">
                        <div><fmt:message key="ChooseExistingPlayer"/></div>
                        <div>
                            <fmt:message key="CurrentlySelected" var="CurrentlySelected"/>
                            <fmt:message key="PleaseChoose" var="EmptyTitle"/>
                            <fmt:message key="ErrorText" var="ErrorText"/>
                            <fmt:message key="Search" var="SearchPlaceholder"/>
                            <fmt:message key="StatusInitialized" var="StatusInitialized"/>
                            <fmt:message key="SearchNoResults" var="SearchNoResults"/>
                            <fmt:message key="StatusSearching" var="StatusSearching"/>
                            <div class="relative">
                                <spf:select
                                    path="UUID"
                                    class="form-control select-ajax-search"
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
                                </spf:select>
                                <span class="explanation-select"><fmt:message key="Player"/></span>
                            </div>
                        </div>

                        <div><fmt:message key="CreateNewPlayer"/></div>
                        <div>
                            <jsp:include page="player-input.jsp"/>
                        </div>
                    </div>

                    <div class="unit-2 relative">
                        <input id="NumberOfGuests" name="NumberOfGuests" type="number" class="form-control" min="1" value="${NumberOfGuests}"/>
                        <div class="explanation-select"><fmt:message key="NumberOfGuests"/></div>
                    </div>

                    <button class="btn btn-primary btn-block btn-form-submit unit-2" type="submit"><fmt:message key="Save"/></button>

                </spf:form>
            </div>
        </div>    
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
