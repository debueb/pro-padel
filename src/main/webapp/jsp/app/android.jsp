<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="AndroidApp"/></h4></div>
            <div class="panel-body">
                <fmt:message key="AndroidAppMsg"/>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
<script src="/js/noconcat/99_addtohomescreen.min.js"></script>
<script type="text/javascript">
    addToHomescreen.removeSession();
    addToHomescreen({
        autostart: true,
        startDelay: 5
    });
</script>
