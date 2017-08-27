<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/pro/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="panel panel-default unit-2">
            <div class="panel-heading">
                <h4 class="text-center"><fmt:message key="Register"/></h4>
            </div>
            <div class="panel-body">
                <fmt:message key="CustomerRegistrationSuccessful"/>
                <br>
                <a href="http://${domainName}" target="blank">${domainName}</a>
                <br><br>
                <fmt:message key="CustomerRegistrationSuccessfulVideos"/>
                <br><br>
                <a href="https://www.youtube.com/playlist?list=PLvIfgGS2XDif7p24yWejpvR72jwo-fqnK" target="blank">Tutorials on Youtube</a>
            </div>
        </div>
    </div>
</div> 
<jsp:include page="/WEB-INF/jsp/pro/include/footer.jsp"/>
