<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/pro/include/head-simple.jsp"/>

<div id="overlay">
    <video class="visible-desktop" id="hero-vid" poster="/pro/images/bg.png" autoplay loop muted>
        <source type="video/webm" src="/pro/videos/bg.webm"></source>
        <source type="video/mp4" src="/pro/videos/bg.mp4"></source>
    </video>
    <img id="hero-pic" class="hidden-desktop" src="/pro/images/bg.png" alt="">
</div>
<div class="content" style="background-color: #263248">
    <p>
    <h3 class="text-center"><fmt:message key="WelcomeTo"><fmt:param value="${pageContext.request.serverName}"/></fmt:message></h3>
        <br><br>
        <h4 class="text-center"><fmt:message key="ProPadelDesc"/></h4>
</p>
</div>
<div class="parallax-window" data-parallax="scroll" data-image-src="/pro/images/3.jpg"></div>
<div class="content" style="background-color: #FF9800">
    <p>
        <a href="/pro/tournaments" class="white"><h3 class="text-center"><fmt:message key="Tournaments"/></h3></a>
        <br><br>
        <a href="/pro/tournaments" class="white"><h4 class="text-center"><fmt:message key="TournamentsUserDesc"/></h4></a>
    </p>
    <p>
        <br><br>
        <a href="/pro/ranking" class="white"><h3 class="text-center"><fmt:message key="Ranking"/></h3></a>
        <br><br>
        <a href="/pro/ranking" class="white"><h4 class="text-center"><fmt:message key="RankingUserDesc"/></h4></a>
    </p>
</div>
<div class="parallax-window" data-parallax="scroll" data-image-src="/pro/images/4.jpg"></div>
<div class="content" style="background-color: #7E8AA2">
    <p>
        <a href="/pro/operators" class="white"><h3 class="text-center"><fmt:message key="ForOperators"/></h3></a>
        <br><br>
        <a href="/pro/operators" class="white"><h4 class="text-center"><fmt:message key="ForOperatorsDesc"/></h4></a>
    </p>
</div>
<jsp:include page="/jsp/pro/include/footer-simple.jsp"/>