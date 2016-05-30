<%@include file="/jsp/include/include.jsp"%>

<fmt:message key="CustomerRegistrationSuccessful"/>
<c:choose>
    <c:when test="${dnsRecordExists}">
        <a href="http://${domainName}">${domainName}</a>
    </c:when>
    <c:otherwise>
        <fmt:message key="DnsRecordNotYetAvailable"/>
        <script type="text/javascript">
            setTimeout(function(){
                window.location.reload(1);
            }, 30000);
        </script>
    </c:otherwise>
</c:choose>