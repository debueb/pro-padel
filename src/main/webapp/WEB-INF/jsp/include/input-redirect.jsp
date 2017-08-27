<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<c:if test="${not empty param.redirectUrl}">
    <input type="hidden" name="redirectUrl" value="${param.redirectUrl}"> 
</c:if>