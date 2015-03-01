<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header">
            <h1><fmt:message key="MatchOffer"/></h1>
        </div>
        
        <div class="alert alert-danger">${error}</div>
        <div class="alert alert-success">${msg}</div>
        
        <jsp:include page="/jsp/matchoffers/include/offer-details.jsp"/>

        <c:set var="url" value="/matchoffers/${Model.id}"/>
        <c:choose>
            <c:when test="${Model.owner == sessionScope.user}">
                <a class="btn btn-primary btn-block" href="/matchoffers/${Model.id}/edit"><fmt:message key="EditMatchOffer"/></a>
            </c:when>
            <c:when test="${empty sessionScope.user and empty sessionScope.accessLevel}">
                <a class="btn btn-primary btn-block" href="/login?redirect=${url}"><fmt:message key="LoginToParticipate"/></a>
                <a class="btn btn-primary btn-block" href="/login/register?redirect=${url}"><fmt:message key="RegisterToParticipate"/></a>
            </c:when>
            <c:otherwise>
                <form method="POST" style="margin-bottom: 5px;">
                <c:choose>
                    <c:when test="${fn:contains(Model.players, sessionScope.user)}">
                        <input type="checkbox" name="cancel-participation" id="cancel-participation"/>
                        <label class="checkbox" for="cancel-participation"><small><fmt:message key="MatchOffersCancel"/></small></label>

                        <button class="btn btn-primary btn-block"><fmt:message key="IWantToCancelParticipation"/></button>
                    </c:when>
                    <c:otherwise>
                        <input type="checkbox" name="accept-participation" id="accept-participation"/>
                        <label class="checkbox" for="accept-participation"><small><fmt:message key="MatchOffersAccept"/></small></label>

                        <button class="btn btn-primary btn-block"><fmt:message key="IWantToParticipate"/></button>
                    </c:otherwise>
                </c:choose>
                </form>
            </c:otherwise>
        </c:choose>
        <c:url var="fullUrl" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}${url}"/>
        <a class="btn btn-primary btn-block" href="whatsapp://send?text=${fullUrl}"><fmt:message key="ShareOfferViaWhatsApp"/></a>
        <a class="btn btn-primary btn-block" href="mailto:?subject=${NewMatchOfferMailSubject}&body=${NewMatchOfferMailBody}"><fmt:message key="ShareOfferViaMail"/></a>
        <a class="btn btn-primary btn-block" href="/matchoffers"><fmt:message key="OtherMatchOffers"/></a>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>