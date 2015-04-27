<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <h1><fmt:message key="General"/></h1>
        </div>

        <div class="list-group">
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/general/offers"/>
                <jsp:param name="key" value="Offers"/>
                <jsp:param name="icon" value="dribbble"/>
            </jsp:include>
            <%--
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/general/facilities"/>
                <jsp:param name="key" value="Facilities"/>
                <jsp:param name="icon" value="building"/>
            </jsp:include>
            --%>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/general/admingroups"/>
                <jsp:param name="key" value="AdminGroups"/>
                <jsp:param name="icon" value="fire"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/general/design"/>
                <jsp:param name="key" value="Design"/>
                <jsp:param name="icon" value="sun-o"/>
            </jsp:include>
            <jsp:include page="/jsp/include/list-group-item.jsp">
                <jsp:param name="href" value="/admin/general/modules"/>
                <jsp:param name="key" value="Modules"/>
                <jsp:param name="icon" value="cubes"/>
            </jsp:include>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
