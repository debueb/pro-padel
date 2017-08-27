<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        
        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li class="active"><fmt:message key="Reports"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Reports"/></h4>
            </div>
            <div class="panel-body">
                <div class="list-group">
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/reports/allocations"/>
                        <jsp:param name="key" value="Allocations"/>
                        <jsp:param name="icon" value="cubes"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/reports/players"/>
                        <jsp:param name="key" value="TopPlayers"/>
                        <jsp:param name="icon" value="user"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/reports/times"/>
                        <jsp:param name="key" value="TopTimes"/>
                        <jsp:param name="icon" value="calendar"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/reports/paymentmethods"/>
                        <jsp:param name="key" value="TopPaymentMethod"/>
                        <jsp:param name="icon" value="credit-card"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/reports/utilization"/>
                        <jsp:param name="key" value="Utilization"/>
                        <jsp:param name="icon" value="line-chart"/>
                    </jsp:include>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
