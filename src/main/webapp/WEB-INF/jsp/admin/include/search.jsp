<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<form method="GET" class="search relative">
    <input type="text" name="search" class="form-control" value="${param.search}" placeholder="<fmt:message key="Search"/>"/>
    <span class="fa-stack clear-search">
        <i class="fa fa-close fa-stack-1x"></i>
    </span>
</form>