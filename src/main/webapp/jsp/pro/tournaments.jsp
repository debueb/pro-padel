<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/pro/include/head.jsp"/>
<div class="container-fluid">
    <div class="row">
        <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-md-8 col-md-offset-2">
            <div class="panel panel-default unit-2">
                <div class="panel-heading">
                    <h4 class="text-center"><fmt:message key="UpcomingTournaments"/></h4>
                </div>
                <div class="panel-body">
                    <div class="table-responsive">
                        <c:set var="Models" value="${UpcomingEvents}"/>
                        <jsp:include page="/jsp/pro/include/event-list.jsp"/>
                    </div>
                </div>
            </div>
            <div class="panel panel-default unit-2">
                <div class="panel-heading">
                    <h4 class="text-center"><fmt:message key="PastTournaments"/></h4>
                </div>
                <div class="panel-body">
                    <div class="table-responsive">
                        <c:set var="Models" value="${PastEvents}" scope="request"/>
                        <jsp:include page="/jsp/pro/include/event-list.jsp"/>
                    </div>
                </div>
            </div>
        </div>
    </div>    
</div>
<jsp:include page="/jsp/pro/include/footer.jsp"/>