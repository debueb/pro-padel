<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/pro/include/head.jsp"/>
<div class="homepage-hero-module">
    <div class="video-container">
        <video autoplay loop class="fillWidth">
            <source src="/pro/videos/bg.mp4" type="video/mp4" />
            <source src="/pro/videos/bg.webm" type="video/webm" />
        </video>
    </div>
</div>
<div class="title-container">
        <div class="row">
            <div class="col-xs-12 col-md-10 col-md-offset-1 col-lg-8 col-lg-offset-2 col-xl-6 col-xl-offset-3">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3><fmt:message key="WelcomeTo"><fmt:param value="${pageContext.request.serverName}"/></fmt:message></h3>
                    </div>
                    <div class="panel-body">
                    <fmt:message key="ProPadelDesc"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="link-container flex-container">

            
            
            
            <div class="panel flex-item panel-default">
                <div class="panel-heading">
                    <a href="/pro/tournaments"><h4><fmt:message key="Tournaments"/></h4></a>
                </div>
                <div class="panel-body">
                    <a href="/pro/tournaments"><fmt:message key="TournamentsUserDesc"/></a>
                </div>
            </div>

            <div class="panel flex-item panel-default">
                <div class="panel-heading">
                    <a href="/pro/ranking"><h4><fmt:message key="Ranking"/></h4></a>
                </div>
                <div class="panel-body">
                    <a href="/pro/ranking"><fmt:message key="RankingUserDesc"/></a>
                </div>
            </div>

            <div class="panel flex-item panel-default">
                <div class="panel-heading">
                    <a href="/pro/operators"><h4><fmt:message key="ForOperators"/></h4></a>
                </div>
                <div class="panel-body">
                    <a href="/pro/operators"><fmt:message key="ForOperatorsDesc"/></a>
                </div>
            </div>
        </div>
</div>
<jsp:include page="/jsp/pro/include/footer.jsp"/>