<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/pro/include/head.jsp"/>
<div class="homepage-hero-module">
    <div class="video-container">
        <video autoplay loop class="fillWidth">
            <source src="/pro/videos/bg.mp4" type="video/mp4" />Your browser does not support the video tag. I suggest you upgrade your browser.
            <source src="/pro/videos/bg.webm" type="video/webm" />Your browser does not support the video tag. I suggest you upgrade your browser.
        </video>
    </div>
</div>
<div class="title-container">
    <div class="container">
        <div class="row">
            <div class="col-xs-12 col-md-10 col-md-offset-1 col-lg-8 col-lg-offset-2 col-xl-6 col-xl-offset-3">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3>Willkommen bei pro-padel.de</h3>
                    </div>
                    <div class="panel-body">
                        Hier findest Du alle Infos rund um Padel Turniere und ein Ranking, dass sich automatisch aktualisiert. Wenn Du ein Turnier veranstaltest, dann trage es einfach ein!
                    </div>
                </div>
            </div>
        </div>

        <div class="link-container flex-container">

            <div class="panel flex-item panel-default">
                <div class="panel-heading">
                    <a href="/pro/tournaments"><h3 class="panel-title"><fmt:message key="Tournaments"/></h3></a>
                </div>
                <div class="panel-body">
                    <a href="/pro/tournaments">Wann findet das nächste Turnier statt? Wer ist in der Gruppephase raus geflogen? Wie ist das Finale ausgegangen? Hier findest Du alle Infos zu gelisteten Turnieren</a>
                </div>
            </div>

            <div class="panel flex-item panel-default">
                <div class="panel-heading">
                    <a href="/pro/ranking"><h3 class="panel-title"><fmt:message key="Ranking"/></h3></a>
                </div>
                <div class="panel-body">
                    <a href="/pro/ranking">Schaue Dir das tagesaktuelle Ranking an.</a>
                </div>
            </div>

            <div class="panel flex-item panel-default">
                <div class="panel-heading">
                    <a href="/pro/operators"><h3 class="panel-title"><fmt:message key="ForOperators"/></h3></a>
                </div>
                <div class="panel-body">
                    <a href="/pro/operators">Veranstalte deinen eigenen Turniere. Oder verwalte deine eigene Liga. Oder biete deinen Spielern eine Spielbörse an.</a>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/pro/include/footer.jsp"/>