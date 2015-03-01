<%@include file="/jsp/include/include.jsp"%>
</div>
</div>
<div class="push"></div>
</div>
<c:if test="${empty param.embed}">
<div class="footer small">
    <a href="/imprint" class="ajaxify"><fmt:message key="Imprint"/></a> | <a href="/privacy" class="ajaxify"><fmt:message key="Privacy"/></a>
</div>
</c:if>
<a id="dummy-link" class="ajaxify" href="#"></a>
<%--<c:if test="${empty header['x-requested-with']}">--%>
    <c:set var="compressJS" value="${compress.js}"/>
    <c:choose>
        <c:when test="${(compressJS and empty param.debug) or param.debug == 'minify'}">
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
            <script src="/js/80_jquery.livequery.min.js"></script>
            <script src="/js/90_project.js"></script>
            <script src="/js/91_jquery.history.min.js"></script>
            <script src="/js/92_ajaxify.js"></script>
            <script src="/js/95_ga.js"></script>
            <script src="/js/98_ie10-viewport-bug-workaround.js"></script>

            <%-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries --%>
            <!--[if lt IE 9]>
              <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
              <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
            <![endif]-->

            <script>if (location.host.split(':')[0] === 'localhost')
                    document.write('<script src="http://' + (location.host || 'localhost').split(':')[0] + ':35729/livereload.js?snipver=1"></' + 'script>')</script>
        </c:otherwise>
    </c:choose>
<%--</c:if>--%>
</body>
</html>