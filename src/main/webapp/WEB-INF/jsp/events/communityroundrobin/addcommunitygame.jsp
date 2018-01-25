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
                    <table class="table-full-width">
                        <thead>
                            <th style="padding-left: 10px; width: 20%"><fmt:message key="Community"/></th>
                            <th style="padding-left: 10px; width: 65%"><fmt:message key="Team"/></th>
                            <c:forEach begin="1" end="${Event.numberOfSets}" var="setNumber" varStatus="status">
                                <th class="text-center" style="5%">${setNumber}.</th>
                            </c:forEach>
                        </thead>
                        <tbody>
                            <c:forEach items="${Model.teams}" varStatus="teamStatus">
                                <tr>
                                    <td>
                                        <spf:select
                                            path="teams[${teamStatus.index}].community"
                                            class="select-toggle-restrict select-simple form-control ${teamStatus.last ? 'form-bottom-element' : 'form-top-element'} form-left-element"
                                            data-select-toggle-restrict-container=".team-${teamStatus.index}-select"
                                            data-container="body">
                                            <spf:options items="${Event.communities}" itemValue="id"/>
                                        </spf:select>
                                    </td>
                                    <td>
                                        <c:forEach var="Community" items="${Event.communities}">
                                            <span class="team-${teamStatus.index}-select">
                                                <spf:select
                                                    path="teams[${teamStatus.index}].players"
                                                    class="select-toggle-restrict-${Community.id} select-multiple form-control form-center-element ${teamStatus.last ? 'form-bottom-element' : 'form-top-element'}"
                                                    data-container="body"
                                                    data-max-options="2">
                                                    <spf:options items="${Community.players}" itemValue="UUID"/>
                                                </spf:select>
                                            </span>
                                        </c:forEach>
                                    </td>
                                    <c:forEach begin="1" end="${Event.numberOfSets}" var="setNumber" varStatus="status">
                                        <td>
                                            <c:set var="paramName" value="set-${setNumber}-team-${teamStatus.index}"/>
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
                    <a class="btn btn-primary btn-block unit-2" href="/events/event/${Event.id}/communitygames"><fmt:message key="Cancel"/></a>
                </spf:form>
            </div>
        </div>    
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
