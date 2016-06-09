<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/events/include/info.jsp"/>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="ParticipateChoosePartner"/></h4>
            </div>
            <div class="panel-body">
                <spf:form class="form-signin" method="POST" modelAttribute="Player">
                <div class="alert alert-info"><fmt:message key="ParticipateChoosePartnerDesc"/></div>
                <div class="alert alert-danger unit"><spf:errors path="*"/></div>
                
                <div class="relative unit">
                        <select class="form-control select-simple" name="paymentMethod">
                    <c:forEach var="PaymentMethod" items="${Model.calendarConfig.paymentMethods}">
                        <option value="${PaymentMethod}"><fmt:message key="${PaymentMethod}"/></option>
                    </c:forEach>
                </select>
                    <span class="explanation-select"><fmt:message key="PaymentMethod"/></span>
                </div>
                
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
                                    <fmt:message key="ChoosePartner" var="ChoosePartner"/>
                                </spf:select>
                                <span class="explanation-select"><fmt:message key="Partner"/></span>
                            </div>
                        </div>

                        <div><fmt:message key="CreateNewPlayer"/></div>
                        <div>
                            <jsp:include page="/jsp/include/player-input.jsp"/>
                        </div>
                    </div>
                    <div>
                        <button class="btn btn-primary btn-block unit" type="submit"><fmt:message key="Save"/></button>
                    </div>
                </spf:form>         
            </div>
        </div>
    </div>
</div>



<jsp:include page="/jsp/include/footer.jsp"/>
