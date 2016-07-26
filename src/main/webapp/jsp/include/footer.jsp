<%@include file="/jsp/include/include.jsp"%>
<%-- only admin users should need to download admin functionality
     we check the URL and wether the request was done via AJAX
     if so, we add the <script> dependencies before the closing
     wrapper div, as only <script> tags within this container
     are evaluated by the ajaxify script
--%>
            </div><!-- wrapper -->
        </div>
    </div>
        <div class="footer">
            <c:forEach var="Module" items="${customerModules[sessionScope.customer.name]}" varStatus="status">
                <c:if test="${Module.showInFooter}">
                    <a href="${Module.url}" class="ajaxify">${Module.title}</a> | 
                </c:if>
            </c:forEach>
                powered by <a href="http://pro-padel.de">pro-padel.de</a> 
        </div>
    </div><!-- background -->
    <a id="dummy-link" class="ajaxify" href="#"></a>
    <div id="offline-msg">
        <fmt:message key="Offline"/>
        <div>
            <a id="offline-msg-btn" class="btn btn-default unit" href="#">OK</i></a>
        </div>
    </div>
    <c:if test="${fn:contains(pageContext.request.requestURI, '/admin/')}">
        <jsp:include page="/jsp/include/datatables.jsp"/>
    </c:if>
    <c:if test="${not empty sessionScope.customer and not empty sessionScope.customer.googleAnalyticsTrackingId}">
        <script type="text/javascript" data-id="ga">
            (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
            (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
            m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
            })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

            ga('create', '${sessionScope.customer.googleAnalyticsTrackingId}', 'auto');
            ga('require', 'displayfeatures');
            ga('send', 'pageview');
        </script>
    </c:if>
    </body>
</html>