<%@include file="/jsp/include/include.jsp"%>
            </div><!-- wrapper -->
        </div>
    </div>
        <div class="footer">
            <c:if test="${not empty sessionScope.customer.footerPrefix}">
                ${sessionScope.customer.footerPrefix}&nbsp;
            </c:if>
            <c:set var="first" value="true"/>
            <c:forEach var="CustomerModule" items="${customerModules[sessionScope.customer.name]}">
                <c:if test="${CustomerModule.showInFooter}">
                    <c:if test="${not first}"> | </c:if><a href="${CustomerModule.url}" ${CustomerModule.moduleType == 'Link' ? 'target="blank"' : ''}>${CustomerModule.title}</a>
                    <c:set var="first" value="false"/>
                </c:if>
            </c:forEach>
            <c:if test="${not empty sessionScope.customer.footerSuffix}">
                ${sessionScope.customer.footerSuffix}&nbsp;
            </c:if>
            <div>
                <c:forEach items="${sessionScope.customer.supportedLanguages}" var="lang">
                    <c:if test="${lang ne sessionLang}">
                        <c:set var="subdomain" value="${sessionScope.customer.defaultLanguage eq lang ? '' : lang}"/>
                        <c:set var="r" value="${pageContext.request}"/>
                        <a href="${r.scheme}://${subdomain}${empty subdomain ? '' : '.'}${sessionScope.customer.domainName}${r.serverPort == '8080' ? ':8080' : ''}${r.contextPath}/home"><span class="flag-icon flag-icon-${lang}"></span></a>
                    </c:if>
                </c:forEach>
                powered by <a href="https://pro-padel.de">pro-padel.de</a>
            </div>
        </div>
    </div><!-- background -->
    <div id="offline-msg">
        <fmt:message key="Offline"/>
        <div>
            <a id="offline-msg-btn" class="btn btn-default unit" href="#">OK</i></a>
        </div>
    </div>
    </body>
</html>