<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${Model.name}</h4>
            </div>
        </div>


        <div class="list-group">
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/players/event/${Model.id}"/>
                <jsp:param name="key" value="Players"/>
                <jsp:param name="icon" value="user"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/teams/event/${Model.id}"/>
                <jsp:param name="key" value="Teams"/>
                <jsp:param name="icon" value="group"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/games/event/${Model.id}"/>
                <jsp:param name="key" value="Games"/>
                <jsp:param name="icon" value="dot-circle-o"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/scores/event/${Model.id}"/>
                <jsp:param name="key" value="Score"/>
                <jsp:param name="icon" value="list-ol"/>
            </jsp:include>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
