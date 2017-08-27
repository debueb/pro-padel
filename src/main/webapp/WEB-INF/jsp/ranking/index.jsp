<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/${path}include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        
        <c:if test="${not empty description}">
            ${description}
        </c:if>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Ranking"/></h4>
            </div>
            <div class="panel-body">
                <div class="unit-2">
                    <jsp:include page="/WEB-INF/jsp/ranking/include/links.jsp"/>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/${path}include/footer.jsp"/>
