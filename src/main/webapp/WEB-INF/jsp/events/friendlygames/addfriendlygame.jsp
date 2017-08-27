<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${Event}: <fmt:message key="AddGame"/></h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" modelAttribute="Model">
                    <div class="alert alert-danger"><spf:errors path="*"/></div>
                    <table class="table-full-width table-add-pull-game">
                        <thead>
                            <th style="padding-left: 10px;"><fmt:message key="Team"/></th>
                            <c:forEach begin="1" end="${Event.numberOfSets}" var="setNumber" varStatus="status">
                                <th class="text-center">${setNumber}.</th>
                            </c:forEach>
                        </thead>
                        <tbody>
                            <fmt:message key="CurrentlySelected" var="CurrentlySelected"/>
                            <fmt:message key="PleaseChoose" var="EmptyTitle"/>
                            <fmt:message key="ErrorText" var="ErrorText"/>
                            <fmt:message key="Search" var="SearchPlaceholder"/>
                            <fmt:message key="StatusInitialized" var="StatusInitialized"/>
                            <fmt:message key="SearchNoResults" var="SearchNoResults"/>
                            <fmt:message key="StatusSearching" var="StatusSearching"/>
                            <c:forEach begin="1" end="2" var="teamNumber" varStatus="teamStatus">
                                <tr>
                                    <td>
                                        <div class="relative">
                                        <spf:select 
                                            path="team${teamNumber}"
                                            class="form-control form-left-element ${teamStatus.first ? 'form-top-element' : 'form-bottom-element'} select-ajax-search" 
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
                                    <c:forEach begin="1" end="${Event.numberOfSets}" var="setNumber" varStatus="status">
                                        <td>
                                            <c:set var="paramName" value="set-${setNumber}-team-${teamNumber}"/>
                                            <select name="${paramName}" class="select-simple form-control ${status.last ? 'form-right-element' : 'form-center-element'} ${teamStatus.last ? 'form-bottom-element' : 'form-top-element'}" data-container="body" data-live-search="false">
                                                <option value="-1">-</option>
                                                <c:choose>
                                                    <c:when test="${Event.numberOfSets > 1 && status.last}">
                                                        <c:set var="end" value="${Event.numberOfGamesInFinalSet}"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:set var="end" value="${Event.numberOfGamesPerSet}"/>
                                                    </c:otherwise>
                                                </c:choose>
                                                <c:forEach begin="0" end="${end}" step="1" var="current">
                                                    <option value="${current}" ${param[paramName] == current ? 'selected' : ''}>${current}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </c:forEach>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <button class="btn btn-primary btn-block btn-form-submit unit-2" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>    
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
