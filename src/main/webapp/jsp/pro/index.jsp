<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/pro/include/head-simple.jsp"/>

<div id="overlay">
    <video class="visible-desktop" id="hero-vid" poster="/pro/images/bg.png" autoplay loop muted>
        <source type="video/webm" src="/pro/videos/bg.webm"></source>
        <source type="video/mp4" src="/pro/videos/bg.mp4"></source>
    </video>
    <img id="hero-pic" class="hidden-desktop" src="/pro/images/bg.png" alt="">
</div>
<div class="content" style="background-color: #263248;">
    <div class="content-text">
        <h3 class="text-center"><fmt:message key="WelcomeTo"><fmt:param value="${pageContext.request.serverName}"/></fmt:message></h3>
        <br><br>
        <h4 class="text-center"><fmt:message key="ProPadelDesc"/></h4>
    </div>
</div>
<div class="parallax-window" data-parallax="scroll" data-image-src="/pro/images/3.jpg"></div>
<div class="content" style="background-color: #FF9800">
    <div class="content-text">
        <a href="/pro/tournaments" class="white"><h3 class="text-center"><fmt:message key="Tournaments"/></h3></a>
        <br><br>
        <a href="/pro/tournaments" class="white"><h4 class="text-center"><fmt:message key="TournamentsUserDesc"/></h4></a>
    
        <br><br><br><br>
        <a href="/pro/ranking" class="white"><h3 class="text-center"><fmt:message key="Ranking"/></h3></a>
        <br><br>
        <a href="/pro/ranking" class="white"><h4 class="text-center"><fmt:message key="RankingUserDesc"/></h4></a>
    </div>
</div>
<div class="parallax-window" data-parallax="scroll" data-image-src="/pro/images/4.jpg"></div>
<div class="content content-teaser" style="background-color: #7E8AA2">
    <div  class="content-text" style="padding-bottom: 0px;">
        <a href="/pro/operators" class="white"><h3 class="text-center"><fmt:message key="ForOperators"/></h3></a>
        <br><br>

        <a href="/pro/operators">
            <div style="text-align:center;">
                <section class="teaser">
                    <div class="mobile">
                        <div class="top" style="z-index: 8;">
                            <iframe id="mobile" src="https://walls.de/home">
                            </iframe>
                        </div>
                    </div>

                    <div class="tablet">
                        <div class="top" style="z-index: 6;">
                            <iframe id="tablet" src="https://walls.de/home">
                            </iframe>
                        </div>
                    </div>
                    <div class="laptop">
                        <div class="top" style="z-index: 10;">
                            <iframe id="laptop" src="https://walls.de/home">
                            </iframe>
                        </div>
                    </div>
                    <div class="desktop">
                        <div class="top">
                            <iframe id="desktop" src="https://walls.de/home">
                            </iframe>
                        </div>
                    </div>
                </section>
            </div>
        </a>
    </div>
</div>
<div class="content" style="background-color: #7E8AA2; padding-top: 10px;">
    <a href="/pro/operators" class="white"><h4 class="text-center"><fmt:message key="ForOperatorsDesc"/></h4></a>
</div>
<jsp:include page="/jsp/pro/include/footer-simple.jsp"/>