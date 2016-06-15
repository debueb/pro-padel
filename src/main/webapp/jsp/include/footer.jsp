<%@include file="/jsp/include/include.jsp"%>
<%-- only admin users should need to download admin functionality
     we check the URL and wether the request was done via AJAX
     if so, we add the <script> dependencies before the closing
     wrapper div, as only <script> tags within this container
     are evaluated by the ajaxify script
--%>
<c:if test="${fn:contains(pageContext.request.requestURI, '/admin/')}">
    <jsp:include page="/jsp/include/datatables.jsp"/>
</c:if>
</div><!-- wrapper -->
</div>
</div>
<c:if test="${empty param.embed}">
    <div class="footer small container-fluid">
        <div class="row-fluid">
            <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
                <div class="footer-container">
                <c:forEach var="Module" items="${footerLinks[sessionScope.customer.name]}" varStatus="status">
                    <a href="${Module.url}" class="ajaxify">${Module.title}</a> |  
                </c:forEach>
                    powered by <a href="http://pro-padel.de">pro-padel.de</a>
                </div>
            </div>
        </div>
    </div>
</div><!-- background -->
</c:if>
<a id="dummy-link" class="ajaxify" href="#"></a>
<div id="offline-msg">
    <fmt:message key="Offline"/>
    <div>
        <a id="offline-msg-btn" class="btn btn-default unit" href="#">OK</i></a>
    </div>
</div>
</body>
</html>