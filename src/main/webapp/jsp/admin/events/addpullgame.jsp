<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/events"><fmt:message key="Events"/></a></li>
            <li><a href="/admin/events/edit/${Event.id}">${Event.name}</a></li>
            <li class="active"><fmt:message key="AddGame"/></li>
        </ol>

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
                            <c:forEach begin="1" end="2" var="teamNumber" varStatus="teamStatus">
                                <tr>
                                    <td>
                                        <spf:select path="team${teamNumber}" class="select-multiple form-control ${teamStatus.last ? 'form-bottom-element' : 'form-top-element'} form-left-element" data-container="body" data-max-options="2">
                                            <spf:options items="${Event.participants}" itemValue="UUID"/>
                                        </spf:select>
                                    </td>
                                    <c:forEach begin="1" end="${Event.numberOfSets}" var="setNumber" varStatus="status">
                                        <td>
                                            <c:set var="paramName" value="set-${setNumber}-team-${teamNumber}"/>
                                            <select name="${paramName}" class="select-simple form-control ${teamStatus.last ? 'form-bottom-element' : 'form-top-element'} form-right-element" data-container="body" data-live-search="false">
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
                    <a class="btn btn-primary btn-block btn-back unit-2"><fmt:message key="Cancel"/></a>
                </spf:form>
            </div>
        </div>    
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
