<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Games"/></h4>
            </div>
        </div>


        <c:choose>
            <c:when test="${empty Events}">
                <fmt:message key="NoActiveEvents"/>
            </c:when>
            <c:otherwise>
                <div class="list-group">
                    <c:forEach var="Event" items="${Events}">
                        <fmt:message key="GamesIn" var="msg"><fmt:param>${Event.name}</fmt:param></fmt:message>
                        <jsp:include page="/jsp/include/list-badge-item.jsp">
                            <jsp:param name="msg" value="${msg}"/>
                            <jsp:param name="url" value="/games/event/${Event.id}"/>
                        </jsp:include>
                    </c:forEach>
                </div>    
            </c:otherwise>
        </c:choose>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
