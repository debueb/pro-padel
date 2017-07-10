<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/pro/include/head-simple.jsp"/>

<main class="section-wrapper">
    <section class="section">
        <div class="parallax">
            <video class="video" poster="/pro/images/bg.png" autoplay loop muted>
                <source type="video/webm" src="/pro/videos/bg.webm"></source>
                <source type="video/mp4" src="/pro/videos/bg.mp4"></source>
            </video>
        </div>
    </section>

    <section class="section content" style="background-color: #263248;">
        <div class="content-text">
            <h2 class="text-center"><fmt:message key="WelcomeTo"><fmt:param value="${pageContext.request.serverName}"/></fmt:message></h2>
                <br/><br/>
                <h3 class="text-center"><fmt:message key="ProPadelDesc"/></h3>
        </div>
    </section>

    <section class="section">
        <div class="parallax" style="background-image: url('/pro/images/3.jpg');" alt=""></div>
    </section>

    <section class="section content" style="background-color: #FF9800">
        <div class="content-text">
            <a href="/pro/tournaments" class="white"><h2 class="text-center"><fmt:message key="Tournaments"/></h2></a>
            <br><br>
            <a href="/pro/tournaments" class="white"><h3 class="text-center"><fmt:message key="TournamentsUserDesc"/></h3></a>

            <br><br><br><br>
            <a href="/pro/ranking" class="white"><h2 class="text-center"><fmt:message key="Ranking"/></h2></a>
            <br><br>
            <a href="/pro/ranking" class="white"><h3 class="text-center"><fmt:message key="RankingUserDesc"/></h3></a>
        </div>
    </section>

    <section class="section">
        <div class="parallax" style="background-image: url('/pro/images/4.jpg');" alt=""></div>
    </section>

    <section class="section" style="background-color: #7E8AA2">
        <div class="content-text" style="padding-top: 50px;">
            <a href="/pro/operators" class="white"><h2 class="text-center"><fmt:message key="ForOperators"/></h2></a>
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
            <div class="content" style="margin-bottom: 50px">
                <a href="/pro/operators" class="white"><h3 class="text-center"><fmt:message key="ForOperatorsDesc"/></h3></a>
            </div>
        </div>
    </section>
</main>
<jsp:include page="/jsp/pro/include/footer-simple.jsp"/>