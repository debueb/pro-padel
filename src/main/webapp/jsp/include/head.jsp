<%@include file="/jsp/include/include.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <%--<c:if test="${empty header['x-requested-with']}">--%>
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
        <meta name="description" content="">
        <meta name="author" content="anon">

        <meta name="mobile-web-app-capable" content="yes"> <%-- Chrome M31 and up --%>
        <meta name="apple-mobile-web-app-capable" content="yes" /> <%-- Mobile Safari --%>
        <meta name="format-detection" content="telephone=no">
        
        <meta name="google-site-verification" content="zV4l09W2NsrN2uP1MCLQNG5mR8FPsmIPocAQiYGLaHs" />

        <%-- https://mathiasbynens.be/notes/touch-icons --%>
        <%-- we only use one touch-icon as it will be downscaled if necessary and the touch-icon has the same ratio on all devices --%>
        <link rel="apple-touch-icon" href="/images/apple-touch-icon.png">
        <link rel="icon" sizes="192x192" href="/images/touch-icon-192x192.png">

        <%-- iPhone 3 and 4 Non-Retina --%>
        <link rel="apple-touch-startup-image" media="(device-width: 320px)" href="/images/apple-touch-startup-image-320x460.png">
        <%-- iPhone 4 Retina --%>
        <link rel="apple-touch-startup-image" media="(device-width: 320px) and (-webkit-device-pixel-ratio: 2)" href="/images/apple-touch-startup-image-640x920.png">
        <%-- iPhone 5 Retina --%>
        <link rel="apple-touch-startup-image" media="(device-width: 320px) and (device-height: 568px) and (-webkit-device-pixel-ratio: 2)" href="/images/apple-touch-startup-image-640x1096.png">
        <%-- iPhone 6 --%>
        <link rel="apple-touch-startup-image" media="(device-width: 375px) and (device-height: 667px) and (orientation: portrait) and (-webkit-device-pixel-ratio: 2)" href="/images/apple-touch-startup-image-750x1296.png">
        <%-- iPhone 6+ Portrait --%>
        <link rel="apple-touch-startup-image" media="(device-width: 414px) and (device-height: 736px) and (orientation: portrait) and (-webkit-device-pixel-ratio: 3)" href="/images/apple-touch-startup-image-1242x2148.png">
        <%-- iPad Non-Retina Portrait --%>
        <link rel="apple-touch-startup-image" media="(device-width: 768px) and (orientation: portrait)" href="/images/apple-touch-startup-image-768x1004.png">
        <%-- iPad Non-Retina Landscape --%>
        <link rel="apple-touch-startup-image" media="(device-width: 768px) and (orientation: landscape)" href="/images/apple-touch-startup-image-1024x748.png">
        <%-- iPad Retina Portrait --%>
        <link rel="apple-touch-startup-image" media="(device-width: 1536px) and (orientation: portrait) and (-webkit-device-pixel-ratio: 2)" href="/images/apple-touch-startup-image-1536x2008.png">
        <%-- iPad Retina Landscape --%>
        <link rel="apple-touch-startup-image" media="(device-width: 1536px)  and (orientation: landscape) and (-webkit-device-pixel-ratio: 2)" href="/images/apple-touch-startup-image-2048x1496.png">

        <link rel="icon" href="/favicon.ico">

        <title>${sessionScope.customer}</title>

        <c:set var="compressCSS" value="${compress.css}"/>
        <c:choose>
            <c:when test="${compressCSS or not empty param.debug}">
                <link rel="stylesheet" href="/css/${sessionScope.customer}/all.min.stylesheet">
            </c:when>
            <c:otherwise>
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/10_bootstrap.css.stylesheet">
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/20_bootstrap-select.css.stylesheet">
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/30_font-awesome.min.css.stylesheet">
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/40_jquery-ui.min.css.stylesheet">
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/50_slick.css.stylesheet">
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/90_project.css.stylesheet">
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/96_tennisball.css.stylesheet">
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/99_addtohomescreen.css.stylesheet">
            </c:otherwise>
        </c:choose>
                
        <c:set var="compressJS" value="${compress.js}"/>
        <c:choose>
            <c:when test="${compressJS or not empty param.debug}">
                <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
                <script>window.jQuery || document.write('<script src="/js/noconcat/10_jquery-1.11.1.min.js">\x3C/script>');</script>
                <script src="/js/all.min.js"></script>
                <script src="/js/noconcat/31_datepicker-de.js"></script>
            </c:when>
            <c:otherwise>
                <%-- we use jquery 1.x instead of 2.x to support older Android browsers. Specifically, jQuery.on() does not work on Android 2.1 --%>
                <script src="/js/noconcat/10_jquery-1.11.1.min.js"></script>
                <script src="/js/20_bootstrap.min.js"></script>
                <script src="/js/25_bootstrap-select.js"></script>
                <script src="/js/30_jquery-ui.min.js"></script>
                <script src="/js/noconcat/31_datepicker-de.js"></script>
                <script src="/js/50_slick.min.js"></script>
                <script src="/js/80_jquery.livequery.min.js"></script>
                <script src="/js/90_project.js"></script>
                <script src="/js/91_jquery.history.min.js"></script>
                <script src="/js/92_ajaxify.js"></script>
                <script src="/js/95_ga.js"></script>
                <script src="/js/98_ie10-viewport-bug-workaround.js"></script>
            </c:otherwise>
        </c:choose>
    </head>
    <%--</c:if>--%>
    <body ontouchstart="">
        <div class="background">
            <div class="wrapper">
                <c:if test="${empty param.embed}">
                    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
                        <div class="navbar-header">
                            <c:choose>
                                <c:when test="${pageContext.request.requestURI == '/jsp/index.jsp'}">
                                    <a class="navbar-brand ajaxify" href="/"><img src="images/logo.png" width="63" height="40"/>${pageContext.request.serverName}</a>
                                    </c:when>
                                    <c:otherwise>
                                    <a class="navbar-icon back-icon btn-back"><span class="fa fa-lg fa-arrow-circle-left"></span></a><a class="navbar-icon home-icon ajaxify" href="/"><span class="fa fa-lg fa-home"></span></a>
                                    </c:otherwise>
                                </c:choose>
                            <a class="navbar-icon navbar-toggle">
                                <div class="menu-line menu-line-1"></div>
                                <div class="menu-line menu-line-2"></div>
                                <div class="menu-line menu-line-3"></div>

                                <!--<span class="fa fa-lg fa-bars"></span>-->
                            </a>        
                        </div>
                        <div class="collapse navbar-collapse">
                            <jsp:include page="/jsp/include/navigation.jsp"/>
                        </div>
                    </div>
                </c:if>
                <jsp:include page="/jsp/include/spinner.jsp"/>
                <div id="shadow"><progress></progress></div>
                <div id="site-menu">
                    <jsp:include page="/jsp/include/navigation.jsp"/>
                </div>
                <div id="site-shadow"></div>
                <div id="site-canvas">
                    <div class="container-fluid" id="content">
