<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<script type="text/javascript">
    var PAYMILL_PUBLIC_KEY = '${PAYMILL_PUBLIC_KEY}';
</script>
<script type="text/javascript" src="https://bridge.paymill.com/"></script>
<form id="token-form" method="POST">
    <input id="token" name="token" type="hidden"/>
</form>