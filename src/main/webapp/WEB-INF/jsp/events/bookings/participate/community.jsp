<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        <jsp:include page="/WEB-INF/jsp/events/include/info.jsp"/>
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="ParticipateCommunity"/></h4>
            </div>
            <div class="panel-body">
                <spf:form class="form-signin" method="POST" modelAttribute="EventBookingRequest">
                    <div class="alert alert-info"><fmt:message key="ParticipateCommunityDesc"/></div>
                    <div class="alert alert-danger unit"><spf:errors path="*"/></div>

                    <div class="relative unit-4">
                        <spf:select class="form-control select-simple form-top-element" path="paymentMethod" data-container="body">
                            <c:forEach var="PaymentMethod" items="${Model.paymentMethods}">
                                <spf:option value="${PaymentMethod}"><fmt:message key="${PaymentMethod}"/></spf:option>
                            </c:forEach>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="PaymentMethod"/></span>
                    </div>

                    <div class="relative">
                        <spf:input path="community.name" type="text" class="form-control form-bottom-element"/>
                        <div class="explanation"><fmt:message key="CommunityName"/></div>
                    </div>
                    <div class="unit-4">
                        <fmt:message key="CurrentlySelected" var="CurrentlySelected"/>
                        <fmt:message key="Search" var="SearchPlaceholder"/>
                        <fmt:message key="ChooseExistingPlayers" var="ChooseExistingPlayers"/>
                        <fmt:message key="StatusInitialized" var="StatusInitialized"/>
                        <fmt:message key="SearchNoResults" var="SearchNoResults"/>
                        <fmt:message key="StatusSearching" var="StatusSearching"/>
                        <div>${ChooseExistingPlayers}</div>
                        <div class="relative">
                            <spf:select
                                path="players"
                                multiple="multiple"
                                class="form-control select-ajax-search"
                                data-container="body"
                                data-live-search="true"
                                data-abs-locale-currently-selected='${CurrentlySelected}'
                                data-abs-locale-empty-title='${ChooseExistingPlayers}'
                                data-abs-locale-error-text='${ErrorText}'
                                data-abs-locale-search-placeholder='${SearchPlaceholder}'
                                data-abs-locale-status-initialized='${StatusInitialized}'
                                data-abs-locale-search-no-results='${SearchNoResults}'
                                data-abs-locale-status-searching='${StatusSearching}'
                                data-abs-ajax-url="/api/players/options">
                                <spf:options items="${EventBookingRequest.players}" itemValue="UUID"/>
                            </spf:select>
                            <span class="explanation-select"><fmt:message key="Players"/></span>
                        </div>
                    </div>
                    <!-- NEW PLAYERS -->
                    <c:set var="visibleIndex" value="0"/>
                    <c:forEach var="player" items="${EventBookingRequest.newPlayers}" varStatus="status">
                        <c:if test="${not empty player.firstName}">
                            <c:set var="visibleIndex" value="${status.index+1}"/>
                        </c:if>
                        <div class="unit-4" id="newPlayer-${status.index}" style="display: ${empty player.firstName ? 'none' : 'block'};">
                            <div><fmt:message key="AddNewPlayerNumber"><fmt:param value="${status.index+1}"/></fmt:message></div>
                            <fmt:message key="FirstName" var="FirstName"/>
                            <fmt:message key="LastName" var="LastName"/>
                            <fmt:message key="EmailAddress" var="EmailAddress"/>
                            <fmt:message key="PhoneNumber" var="PhoneNumber"/>
                            <fmt:message key="Password" var="Password"/>
                            <div class="relative">
                                <spf:input path="newPlayers[${status.index}].firstName" type="text" class="form-control form-top-element"/>
                                <div class="explanation">${FirstName}</div>
                            </div>
                            <div class="relative">
                                <spf:input path="newPlayers[${status.index}].lastName" type="text" class="form-control form-center-element"/>
                                <div class="explanation">${LastName}</div>
                            </div>
                            <div class="relative">
                                <spf:input path="newPlayers[${status.index}].email" type="email"  class="form-control form-center-element"/>
                                <div class="explanation">${EmailAddress}</div>
                            </div>
                            <div class="relative">
                                <spf:input path="newPlayers[${status.index}].phone" type="tel"  class="form-control form-center-element"/>
                                <div class="explanation">${PhoneNumber}</div>
                            </div>
                            <div class="relative">
                                <spf:select path="newPlayers[${status.index}].gender" class="select-simple form-control ${not empty param.showPassword ? 'form-center-element' : 'form-bottom-element'}" data-container="body">
                                    <spf:option value="male"><fmt:message key="male"/></spf:option>
                                    <spf:option value="female"><fmt:message key="female"/></spf:option>
                                </spf:select>
                                <span class="explanation-select"><fmt:message key="Gender"/></span>
                            </div>
                        </div>
                    </c:forEach>
                    <button class="btn btn-default btn-block unit-4 enableInputGroup" data-target="#newPlayer-${visibleIndex}"><fmt:message key="AddNewPlayer"/></button>
                    <div>
                        <button class="btn btn-primary btn-block unit-4" type="submit"><fmt:message key="Participate"/></button>
                    </div>
                </spf:form>         
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>