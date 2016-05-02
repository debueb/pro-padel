<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/events/include/info.jsp"/>
        
        <div class="list-group">
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/events/${Model.id}/groupgames"/>
                <jsp:param name="key" value="GroupGames"/>
                <jsp:param name="icon" value="group"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/events/${Model.id}/knockoutgames"/>
                <jsp:param name="key" value="KnockoutGames"/>
                <jsp:param name="icon" value="list-ol"/>
            </jsp:include>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
