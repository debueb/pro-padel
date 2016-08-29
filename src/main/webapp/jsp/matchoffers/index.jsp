<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/module-description.jsp"/>
        <div class="panel panel-info unit-2">
            <div class="panel-heading"><h4><fmt:message key="MatchOffers"/></h4></div>
            <div class="panel-body">
                <div class="list-group unit">
                    <jsp:include page="/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/matchoffers/profile"/>
                        <jsp:param name="key" value="MyProfile"/>
                        <jsp:param name="icon" value="wrench"/>
                    </jsp:include>
                    <c:if test="${not empty PersonalOffers}">
                        <jsp:include page="/jsp/include/list-group-item.jsp">
                            <jsp:param name="href" value="/matchoffers/myoffers"/>
                            <jsp:param name="key" value="MyOffers"/>
                            <jsp:param name="icon" value="list"/>
                        </jsp:include>
                    </c:if>
                    <jsp:include page="/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/matchoffers/add"/>
                        <jsp:param name="key" value="NewMatchOffer"/>
                        <jsp:param name="icon" value="plus"/>
                    </jsp:include>
                </div>
            </div>
        </div>
        <hr>
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="CurrentMatchOffers"/></h4>
            </div>
            <div class="panel-body">
                <c:choose>
                    <c:when test="${empty Models}">
                        <div class="alert alert-info"><fmt:message key="NoCurrentMatchOffers"/></div>
                    </c:when>
                    <c:otherwise>
                        <div class="list-group unit">
                            <c:forEach var="Model" items="${Models}">
                                <c:set var="Model" value="${Model}" scope="request"/>
                                <c:set var="OfferURL" value="/matchoffers/${Model.id}" scope="request"/>
                                <jsp:include page="/jsp/matchoffers/include/offer-list-item.jsp"/>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>