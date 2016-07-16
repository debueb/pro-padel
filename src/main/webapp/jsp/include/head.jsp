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
        <meta name="48b1e5959ec929f1a8b8366d745e337f2a62cb3e" content="6b492683afd937337dc2c12cbaf78ff023df19c6" /> <%-- tinfoilsecurity --%>
        <meta name="detectify-verification" content="a4f2af1bd6103ba6eed3eeae0a91ceca" /> <%-- detectify --%>
        
        <meta http-equiv="Content-Security-Policy" content="frame-ancestors unisport.koeln">

        <%-- https://mathiasbynens.be/notes/touch-icons --%>
        <%-- we only use one touch-icon as it will be downscaled if necessary and the touch-icon has the same ratio on all devices --%>
        <link rel="apple-touch-icon" href="${sessionScope.customer.touchIconPath}">
        <link rel="icon" sizes="192x192" href="${sessionScope.customer.touchIconPath}">

        <title>${sessionScope.customer}</title>

        <c:set var="compressCSS" value="${compress.css}"/>
        <c:choose>
            <c:when test="${compressCSS and empty param.debug}">
                <link rel="stylesheet" href="/css/${sessionScope.customer}/all.min.stylesheet">
            </c:when>
            <c:otherwise>
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/10_bootstrap.css.stylesheet">
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/20_bootstrap-select.css.stylesheet">
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/21_ajax-bootstrap-select.css.stylesheet">
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/30_font-awesome.min.css.stylesheet">
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/40_jquery-ui.min.css.stylesheet">
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/50_slick.css.stylesheet">
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/51_slick-theme.css.stylesheet">
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/52_slick_project.css.stylesheet">
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/90_project.css.stylesheet">
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/96_tennisball.css.stylesheet">
                <link rel="stylesheet" href="/css/dev/${sessionScope.customer}/99_addtohomescreen.css.stylesheet">
            </c:otherwise>
        </c:choose>
                
        <c:set var="compressJS" value="${compress.js}"/>
        <c:choose>
            <c:when test="${compressJS and empty param.debug}">
                <script src="//code.jquery.com/jquery-1.11.1.min.js" integrity="sha384-UM1JrZIpBwVf5jj9dTKVvGiiZPZTLVoq4sfdvIe9SBumsvCuv6AHDNtEiIb5h1kU" crossorigin="anonymous"></script>
                <script>window.jQuery || document.write('<script src="/js/noconcat/10_jquery-1.11.1.min.js">\x3C/script>');</script>
                <script src="/js/all.min.js"></script>
                <script src="/js/noconcat/31_datepicker-de.js"></script>
            </c:when>
            <c:otherwise>
                <%-- we use jquery 1.x instead of 2.x to support older Android browsers. Specifically, jQuery.on() does not work on Android 2.1 --%>
                <script src="/js/noconcat/10_jquery-1.11.1.min.js"></script>
                <script src="/js/20_bootstrap.min.js"></script>
                <script src="/js/25_bootstrap-select.js"></script>
                <script src="/js/26_ajax-bootstrap-select.js"></script>
                <script src="/js/30_jquery-ui.min.js"></script>
                <script src="/js/noconcat/31_datepicker-de.js"></script>
                <script src="/js/50_slick.min.js"></script>
                <script src="/js/80_jquery.livequery.min.js"></script>
                <script src="/js/90_project.js"></script>
                <script src="/js/91_jquery.history.min.js"></script>
                <script src="/js/92_ajaxify.js"></script>
                <script src="/js/98_ie10-viewport-bug-workaround.js"></script>
            </c:otherwise>
        </c:choose>
    </head>
    <%--</c:if>--%>
    <body ontouchstart="">
        <div class="background">
            <div class="wrapper">
                <div class="navbar navbar-default navbar-fixed-top">
                    <div class="navbar-header">
                        <c:choose>
                            <c:when test="${pageContext.request.requestURI == '/jsp/index.jsp'}">
                                <div class="navbar-brand"><img src="${sessionScope.customer.companyLogoPath}" class="customerLogo"/>${pageContext.request.serverName}</div>
                            </c:when>
                            <c:otherwise>
                                <a class="navbar-icon back-icon btn-back"><span class="fa fa-lg fa-arrow-circle-left"></span></a>
                                <a class="navbar-icon navbar-hover home-icon ajaxify" href="/"><span class="fa fa-lg fa-home"></span></a>
                            </c:otherwise>
                            </c:choose>
                        <a class="navbar-icon navbar-toggle">
                            <div class="menu-line menu-line-1"></div>
                            <div class="menu-line menu-line-2"></div>
                            <div class="menu-line menu-line-3"></div>
                        </a>        
                    </div>
                    <div class="collapse navbar-collapse">
                        <div class="nav navbar-nav stretch">
                            <jsp:include page="/jsp/include/navigation.jsp"/>
                        </div>
                    </div>
                </div>
                <jsp:include page="/jsp/include/spinner.jsp"/>
                <div id="shadow"></div>
                <div id="site-menu">
                    <jsp:include page="/jsp/include/navigation.jsp"/>
                </div>
                <div id="site-shadow"></div>
                <div id="site-canvas">
                    <div class="container-fluid" id="content">
