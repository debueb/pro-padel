<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <ol class="unit-2 breadcrumb">
            <li class="active"><fmt:message key="Administration"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Administration"/></h4>
            </div>
            <div class="panel-body">
                <div class="list-group">
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/general"/>
                        <jsp:param name="key" value="General"/>
                        <jsp:param name="icon" value="gears"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/bookings"/>
                        <jsp:param name="key" value="Bookings"/>
                        <jsp:param name="icon" value="calendar"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/reports"/>
                        <jsp:param name="key" value="Reports"/>
                        <jsp:param name="icon" value="pie-chart"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/players"/>
                        <jsp:param name="key" value="Players"/>
                        <jsp:param name="icon" value="user"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/teams"/>
                        <jsp:param name="key" value="Teams"/>
                        <jsp:param name="icon" value="users"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/communities"/>
                        <jsp:param name="key" value="Communities"/>
                        <jsp:param name="icon" value="signing"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/subscriptions"/>
                        <jsp:param name="key" value="Subscriptions"/>
                        <jsp:param name="icon" value="money"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/eventgroups"/>
                        <jsp:param name="key" value="EventGroups"/>
                        <jsp:param name="icon" value="sitemap"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/events"/>
                        <jsp:param name="key" value="Events"/>
                        <jsp:param name="icon" value="trophy"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/contact"/>
                        <jsp:param name="key" value="ContactSettings"/>
                        <jsp:param name="icon" value="at"/>
                    </jsp:include>
                    <jsp:include page="/WEB-INF/jsp/include/list-group-item.jsp">
                        <jsp:param name="href" value="/admin/staff"/>
                        <jsp:param name="key" value="Staff"/>
                        <jsp:param name="icon" value="male"/>
                    </jsp:include>
                </div>
            </div>
        </div>  
    </div>
</div>


<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
