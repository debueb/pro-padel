<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading"><h4><fmt:message key="Events"/></h4></div>
            <div class="panel-body"><fmt:message key="EventsDesc"/></div>
        </div>

        <div class="list-group">
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
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
