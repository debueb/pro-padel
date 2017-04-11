<%@include file="/jsp/include/include.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <%--<c:if test="${empty header['x-requested-with']}">--%>
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
        
        <meta name="mobile-web-app-capable" content="yes"> <%-- Chrome M31 and up --%>
        <meta name="apple-mobile-web-app-capable" content="yes" /> <%-- Mobile Safari --%>
        <meta name="format-detection" content="telephone=no">
        
        <meta name="google-site-verification" content="zV4l09W2NsrN2uP1MCLQNG5mR8FPsmIPocAQiYGLaHs" />
        
        <%-- also used in footer --%>
        <c:set var="indexOfUnderscore" value='${fn:indexOf(sessionScope.sessionLocale, "_")}'/>
        <c:set var="sessionLang" value="${indexOfUnderscore > 0 ? fn:substring(sessionScope.sessionLocale, 0, indexOfUnderscore) : sessionScope.sessionLocale}" scope="request"/>
        
        <c:if test="${pageContext.request.requestURI == '/jsp/index.jsp'}">
            <c:forEach items="${sessionScope.customer.supportedLanguages}" var="lang">
                <c:if test="${lang ne sessionLang}">
                    <c:set var="subdomain" value="${sessionScope.customer.defaultLanguage eq lang ? '' : lang}"/>
                    <c:set var="r" value="${pageContext.request}"/>
                    <link rel="alternate" href="${r.scheme}://${subdomain}${empty subdomain ? '' : '.'}${sessionScope.customer.domainName}${r.serverPort == '8080' ? ':8080' : ''}${r.contextPath}/home" hreflang="${lang}"/>
                </c:if>
            </c:forEach>
        </c:if>
        
        <c:choose>
            <c:when test="${not empty PageTitle}">
                <title>${PageTitle}</title>
            </c:when>
            <c:when test="${empty Module}">
                <title>${sessionScope.customer}</title>
            </c:when>
            <c:otherwise>
                <title>${Module.seoTitle}</title>
                <c:if test="${not empty Module.shortDescription}">
                    <meta name="description" content="${Module.shortDescription}">
                </c:if>
            </c:otherwise>
        </c:choose>
                    
        <meta name="robots" content="${Module.seoRobots}">

        <%-- https://mathiasbynens.be/notes/touch-icons --%>
        <%-- we only use one touch-icon as it will be downscaled if necessary and the touch-icon has the same ratio on all devices --%>
        <link rel="apple-touch-icon" href="${sessionScope.customer.touchIconPath}">
        <link rel="icon" sizes="192x192" href="${sessionScope.customer.touchIconPath}">
        
        <link rel="manifest" href="/manifest/manifest.json">
        
        <link rel="stylesheet" href="/css/${sessionScope.customer}/all.min.stylesheet" title="project_css">

        <script src="/js/noconcat/bugsnag.js" data-apikey="c3a3e8e773db9d46f2d51d905ead0e83"></script>
        <script src="/app/dist/bundle.js" 
                data-release-stage="${profile.id}" 
                data-user-name="${user.firstName} ${user.lastName}" 
                data-user-email="${user.email}"
                data-google-tagmanager-id="${sessionScope.customer.googleTagManagerId}"
                data-google-analytics-id="${sessionScope.customer.googleAnalyticsTrackingId}"
                >
        </script>
    </head>
    <%--</c:if>--%>
    <body ontouchstart="">
        <c:if test="${not empty sessionScope.customer and not empty sessionScope.customer.googleTagManagerId}">
            <noscript><iframe src="https://www.googletagmanager.com/ns.html?id=${sessionScope.customer.googleTagManagerId}" height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
        </c:if>
        <div class="background">
            <div class="wrapper">
                <c:if test="${empty skipNavbar}">
                    <c:set var="isHomepage" value="${pageContext.request.requestURI == '/jsp/index.jsp'}"/>
                    <div class="navbar navbar-default navbar-fixed-top">
                        <c:choose>
                            <c:when test="${empty sessionScope.customer.companyLogo}">
                                <c:if test="${not isHomepage}">
                                    <a class="navbar-icon back-icon btn-back"><span class="fa fa-lg fa-arrow-circle-left"></span></a>
                                </c:if>
                                <a class="navbar-icon navbar-hover home-icon" href="/home"><span class="fa fa-lg fa-home"></span></a>
                            </c:when>
                            <c:otherwise>
                                <span class="navbar-logo">
                                    <c:if test="${not isHomepage}">
                                        <a class="navbar-icon back-icon btn-back"><span class="fa fa-lg fa-arrow-circle-left"></span></a>
                                    </c:if>
                                    <c:set var="logo" value="${sessionScope.customer.companyLogo}"/>
                                    <a class="customerLogo" href="/home"
                                       style="width: ${logo.width/2}px; background-size: ${logo.width/2}px ${logo.height/2}px; background-image: url('${sessionScope.customer.companyLogoPath}')">
                                    </a>
                                </span>
                            </c:otherwise>
                        </c:choose>
                        <a class="navbar-icon navbar-toggle">
                            <div class="menu-line menu-line-1"></div>
                            <div class="menu-line menu-line-2"></div>
                            <div class="menu-line menu-line-3"></div>
                        </a> 
                        <div class="collapse navbar-collapse">
                            <jsp:include page="/jsp/include/navigation.jsp"/>
                        </div>
                    </div>
                </c:if>
                <jsp:include page="/jsp/include/spinner.jsp"/>
                <div id="shadow"></div>
                <div id="site-menu">
                    <jsp:include page="/jsp/include/navigation.jsp"/>
                </div>
                <div id="site-shadow"></div>
                <div id="site-canvas">
                    <div class="container-fluid" id="content">
