<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header">
            <h1><fmt:message key="MatchOffer"/></h1>
        </div>
        
        <jsp:include page="/jsp/matchmaker/offers/include/offer-details.jsp"/>

        <c:set var="url" value="/matchmaker/offers/offer/${Model.id}"/>
        <c:choose>
            <c:when test="${empty sessionScope.accessLevel}">
                <a class="btn btn-primary btn-block" href="/login?redirect=${url}"><fmt:message key="LoginToParticipate"/></a>
                <a class="btn btn-primary btn-block" href="/register?redirect=${url}><fmt:message key="RegisterToParticipate"/></a>
            </c:when>
            <c:when test="${Model.owner == sessionScope.user}">
                <a class="btn btn-primary btn-block" href="/matchmaker/offers/edit/${Model.id}"><fmt:message key="EditMatchOffer"/></a>
            </c:when>
            <c:when test="${fn:contains(Model.players, sessionScope.user)}">
                <input type="checkbox" name="cancel-participation" id="cancel-participation"/>
                <label class="checkbox" for="cancel-participation"><small><fmt:message key="MatchMakerCancel"/></small></label>
        
                <button class="btn btn-primary btn-block"><fmt:message key="IWantToCancelParticipation"/></button>
            </c:when>
            <c:otherwise>
                <input type="checkbox" name="accept-participation" id="accept-participation"/>
                <label class="checkbox" for="accept-participation"><small><fmt:message key="MatchMakerAccept"><fmt:param value="${CancellationPolicyDeadline}"/></fmt:message></small></label>
        
                <button class="btn btn-primary btn-block"><fmt:message key="IWantToParticipate"/></button>
            </c:otherwise>
        </c:choose>
        <a class="btn btn-primary btn-block" href="/matchmaker"><fmt:message key="OtherMatchOffers"/></a>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
