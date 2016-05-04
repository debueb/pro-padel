<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        
        <c:if test="${empty Models}">
            <div class="alert alert-info">
                <fmt:message key="NoActiveEvents"/>
            </div>
        </c:if>
        
        <div class="list-group">
            <c:forEach var="Event" items="${Models}">
                <jsp:include page="/jsp/include/list-group-item.jsp">
                    <jsp:param name="href" value="/events/event/${Event.id}"/>
                    <jsp:param name="title" value="${Event.name}"/>
                </jsp:include>
            </c:forEach>
        </div>

<%--        <div class="list-group">
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/scores"/>
                <jsp:param name="key" value="Scores"/>
                <jsp:param name="icon" value="list-ol"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/games"/>
                <jsp:param name="key" value="Games"/>
                <jsp:param name="icon" value="dot-circle-o"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/players"/>
                <jsp:param name="key" value="Players"/>
                <jsp:param name="icon" value="user"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/teams"/>
                <jsp:param name="key" value="Teams"/>
                <jsp:param name="icon" value="users"/>
            </jsp:include>
        </div>--%>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
