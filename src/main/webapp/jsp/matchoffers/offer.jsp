<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="MatchOffer"/></h4></div>
            <div class="panel-body">
                <div class="alert alert-danger">${error}</div>
                <div class="alert alert-success">${msg}</div>
                <c:set var="url" value="/matchoffers/${Model.id}"/>
                
                <jsp:include page="/jsp/matchoffers/include/offer-details.jsp"/>
                <div class="row">
                    <div class="col-xs-3" style="height: 32px; line-height: 32px;">
                        <fmt:message key="ShareVia"/>:
                    </div>
                    <div class="col-xs-9">
                        <c:set var="shareUrl" scope="request" value="${pageContext.request.scheme}://${pageContext.request.serverName}${pageContext.request.contextPath}${url}"/>
                        <jsp:include page="/jsp/include/share.jsp"/>
                    </div>
                </div>

                <div class="unit-2">
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
                </div>
                <a class="btn btn-primary btn-block ajaxify unit" href="/matchoffers"><fmt:message key="OtherMatchOffers"/></a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
