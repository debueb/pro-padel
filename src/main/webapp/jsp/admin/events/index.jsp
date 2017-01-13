<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

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
                    <jsp:include page="/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/events/active"/>
                        <jsp:param name="key" value="activeEvents"/>
                        <jsp:param name="icon" value="eye"/>
                    </jsp:include>
                    <jsp:include page="/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/events/inactive"/>
                        <jsp:param name="key" value="inactiveEvents"/>
                        <jsp:param name="icon" value="eye-slash"/>
                    </jsp:include>
                </div>
                <a href="${contextPath}/admin/events/add" class="btn btn-primary btn-block ajaxify unit-2"><fmt:message key="NewEvent"/></a>
            </div>
        </div>  
    </div>
</div>


<jsp:include page="/jsp/include/footer.jsp"/>
