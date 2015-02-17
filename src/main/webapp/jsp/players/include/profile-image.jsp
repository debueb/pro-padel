<%@include file="/jsp/include/include.jsp"%>
<span class="polaroid">
    <c:choose>
        <c:when test="${empty Player.profileImage}">
            <span class="fa-stack fa-5x">
                <i class="fa fa-circle fa-stack-2x fa-inverse"></i>
                <i class="fa fa-user fa-stack-1x"></i>
            </span>
            <span class="block"><a href="/players/player/${Player.id}">${Player}</a></span>
        </c:when>
        <c:otherwise>
            <a href="/players/player/${Player.id}">
                <img src="/images/image/${Player.profileImage.sha256}"/>
                <span class="block">${Player}</span>
            </a>
        </c:otherwise>
    </c:choose>
</span>
    

