<%@include file="/jsp/include/include.jsp"%>
<form method="GET" class="search ajaxify">
    <input type="text" name="search" class="form-control" value="${param.search}" placeholder="<fmt:message key="Search"/>" ${not empty param.search ? 'autofocus onfocus="this.value = this.value;"' : ''}/>
</form>