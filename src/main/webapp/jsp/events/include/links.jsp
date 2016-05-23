<%@include file="/jsp/include/include.jsp"%>
<div class="list-group">
    <jsp:include page="/jsp/include/list-group-item.jsp">
        <jsp:param name="href" value="/events/event/${Model.id}/participants"/>
        <jsp:param name="key" value="Participants"/>
        <jsp:param name="icon" value="group"/>
    </jsp:include>
    <jsp:include page="/jsp/include/list-group-item.jsp">
        <jsp:param name="href" value="/events/event/${Model.id}/groupgames"/>
        <jsp:param name="key" value="GroupGames"/>
        <jsp:param name="icon" value="dot-circle-o"/>
    </jsp:include>
    <jsp:include page="/jsp/include/list-group-item.jsp">
        <jsp:param name="href" value="/events/event/${Model.id}/knockoutgames"/>
        <jsp:param name="key" value="KnockoutGames"/>
        <jsp:param name="icon" value="list-ol"/>
    </jsp:include>
</div>