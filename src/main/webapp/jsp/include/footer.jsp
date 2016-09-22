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
        <c:if test="${empty skipFooter}">
            <div class="footer">
                <c:if test="${not empty sessionScope.customer.footerPrefix}">
                    ${sessionScope.customer.footerPrefix}&nbsp;
                </c:if>
                <c:set var="first" value="true"/>
                <c:forEach var="CustomerModule" items="${customerModules[sessionScope.customer.name]}">
                    <c:if test="${CustomerModule.showInFooter}">
                        <c:if test="${not first}"> | </c:if><a href="${CustomerModule.url}" ${CustomerModule.moduleType == 'Link' ? 'target="blank"' : 'class="ajaxify"'}>${CustomerModule.title}</a>
                        <c:set var="first" value="false"/>
                    </c:if>
                </c:forEach>
                <div>
                    <a href="?lang=en" class="ajaxify"><span class="flag-icon flag-icon-gb"></span></a>
                    <a href="?lang=de" class="ajaxify"><span class="flag-icon flag-icon-de"></span></a>
                    powered by <a href="http://pro-padel.de">pro-padel.de</a>
                </div>
            </div>
        </c:if>
    </div><!-- background -->
    <a id="dummy-link" class="ajaxify" href="#"></a>
    <div id="offline-msg">
        <fmt:message key="Offline"/>
        <div>
            <a id="offline-msg-btn" class="btn btn-default unit" href="#">OK</i></a>
        </div>
    </div>
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