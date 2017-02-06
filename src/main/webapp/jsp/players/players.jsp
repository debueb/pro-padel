<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        <jsp:include page="/jsp/events/include/info.jsp"/>
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${title}</h4>
            </div>
            <div class="panel-body">
                <div class="list-group">
                    <c:forEach var="RankingMapEntry" items="${RankingMap}">
                        <c:set var="Player" value="${RankingMapEntry.key}"/>
                        <c:set var="Ranking" value="${RankingMapEntry.value}"/>
                        <a href="/players/player/${Player.UUID}" class="list-group-item ajaxify">
                            <div class="list-item-text">${Player}</div>
                            <div class="list-group-item-icon">
                                <c:choose>
                                    <c:when test="${empty Player.profileImage}">
                                        <i class="fa fa-user"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="/images/image/${Player.profileImage.sha256}"/>
                                    </c:otherwise>
                                </c:choose>
                                <span class="player-ranking">${Ranking}</span>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
