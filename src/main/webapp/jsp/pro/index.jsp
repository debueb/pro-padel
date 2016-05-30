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
            <div class="headline">
                <h1>Willkommen bei pro-padel.de</h1>
            </div>
            <div class="description">
                <div class="inner"><h4>Hier findest Du alle Infos rund um Padel Turniere und ein Ranking, dass sich automatisch aktualisiert. Wenn Du ein Turnier veranstaltest, dann trage es einfach ein!</h4></div>
            </div>

            <div class="container link-container">
                <!-- Example row of columns -->
                <div class="row">
                    <div class="col-md-4">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title"><fmt:message key="Tournaments"/></h3>
                            </div>
                            <div class="panel-body">
                                Wann findet das nächste Turnier statt? Wer ist in der Gruppephase raus geflogen? Wie ist das Finale ausgegangen? Hier findest Du alle Infos zu gelisteten Turnieren
                            </div>
                            <div class="panel-footer">
                                <a class="btn btn-default" href="#" role="button"><fmt:message key="AllTournaments"/></a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title"><fmt:message key="Ranking"/></h3>
                            </div>
                            <div class="panel-body">
                                Schaue Dir das tagesaktuelle Ranking an.
                            </div>
                            <div class="panel-footer"><a class="btn btn-default" href="#" role="button"><fmt:message key="CurrentRanking"/></a></div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title"><fmt:message key="ForOperators"/></h3>
                            </div>
                            <div class="panel-body">
                                Veranstalte deinen eigenen Turniere. Oder verwalte deine eigene Liga. Oder biete deinen Spielern eine Spielbörse an.
                            </div>
                            <div class="panel-footer">
                                <a class="btn btn-default" href="/pro/operators" role="button"><fmt:message key="OperatorInfos"/></a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
<jsp:include page="/jsp/pro/include/footer.jsp"/>