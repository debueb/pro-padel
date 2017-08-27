<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li class="active"><fmt:message key="Events"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Events"/></h4>
            </div>
            <div class="panel-body">
                <div class="list-group">
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/events/current"/>
                        <jsp:param name="key" value="currentEvents"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/events/past"/>
                        <jsp:param name="key" value="pastEvents"/>
                    </jsp:include>
                </div>
                <a href="${contextPath}/admin/events/add" class="btn btn-primary btn-block unit-2"><fmt:message key="NewEvent"/></a>
            </div>
        </div>  
    </div>
</div>


<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
