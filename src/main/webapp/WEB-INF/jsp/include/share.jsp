<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<span class="a2a_kit a2a_kit_size_32 a2a_default_style">
<c:set var="baseUrl" value="${pageContext.request.scheme}://${pageContext.request.serverName}${pageContext.request.contextPath}"/>
<c:set var="shareUrl" value="${shareUrl == null ? baseUrl : shareUrl}"/>
<a href="https://www.addtoany.com/share?linkurl=${shareUrl}&amp;linkname="></a>
<a class="a2a_button_whatsapp"></a>
<a class="a2a_button_telegram"></a>
<a class="a2a_button_facebook"></a>
<a class="a2a_button_email"></a>
<a class="a2a_button_google_gmail"></a>
</span>
<script>
var a2a_config = a2a_config || {};
a2a_config.linkurl = "${shareUrl}";
a2a_config.locale = "de";
</script>
<script async src="https://static.addtoany.com/menu/page.js"></script>