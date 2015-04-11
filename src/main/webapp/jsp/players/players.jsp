<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <h1>${title}</h1>
        </div>

        <div class="list-group">
            <c:forEach var="Player" items="${Players}">
                <a href="/players/player/${Player.id}" class="list-group-item ajaxify">
                    <div class="list-item-text">${Player}
                        <div class="list-group-item-icon">
                            <c:choose>
                                <c:when test="${empty Player.profileImage}">
                                    <i class="fa fa-user"></i>
                                </c:when>
                                <c:otherwise>
                                    <img src="/images/image/${Player.profileImage.sha256}"/>
                                </c:otherwise>
                        </c:choose>
                        </div>
                    </div>
                </a>
            </c:forEach>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
