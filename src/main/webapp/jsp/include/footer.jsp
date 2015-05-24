<%@include file="/jsp/include/include.jsp"%>
<%-- only admin users should need to download admin functionality
     we check the URL and wether the request was done via AJAX
     if so, we add the <script> dependencies before the closing
     wrapper div, as only <script> tags within this container
     are evaluated by the ajaxify script
--%>
<c:if test="${fn:contains(pageContext.request.requestURI, '/admin/') and header['X-Requested-With'] == 'XMLHttpRequest'}">
    <jsp:include page="/jsp/include/datatables.jsp"/>
</c:if>
</div><!-- wrapper -->
</div>
<div class="push"></div>
</div>
<c:if test="${empty param.embed}">
<div class="footer small">
    <span class="footer-container">
        <c:forEach var="Module" items="${footerLinks}" varStatus="status">
            <a href="${Module.url}" class="ajaxify">${Module.title}</a> ${not status.last ? ' | ' : ''}
        </c:forEach>
    </span>
</div>
</c:if>
<a id="dummy-link" class="ajaxify" href="#"></a>
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
    <%-- in case the /admin page is not loaded via AJAX 
         (e.g. page is loaded from bookmark or page reload)
         we simply append the admin <script> tags
    --%>
    <c:if test="${fn:contains(pageContext.request.requestURI, '/admin/')}">
        <jsp:include page="/jsp/include/datatables.jsp"/>
    </c:if>
</body>
</html>