<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>
        <jsp:include page="/jsp/events/include/info.jsp"/>
        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${title}</h4>
                <c:if test="${not empty subtitle}">
                    <h4>${subtitle}</h4>
                </c:if>
            </div>
            <div class="panel-body" style="padding: 0;">
                <c:set var="redirectUrl" value="games/event/${Model.id}" scope="request"/>
                <jsp:include page="/jsp/events/include/matchtable.jsp"/>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
