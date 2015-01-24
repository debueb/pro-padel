<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <div class="page-header">
            <h1><fmt:message key="Contact"/></h1>
        </div>
        
        <p><fmt:message key="CourtLocationDescription"/></p>
        <p><fmt:message key="CourtLocationAddress"/></p>
        
        <a target="blank" href="https://www.google.de/maps/place/Universit%C3%A4t+zu+K%C3%B6ln+Universit%C3%A4tssport/@50.92847,6.93349,17z/data=!3m1!4b1!4m2!3m1!1s0x4171868a4cb3f3cd:0xbcc787e23ab4bd28">
            <img src="/images/gmaps.png" style="width: 100%; height: auto;"/>
        </a>
        
        <hr/>

        <p><fmt:message key="ContactDescription"/></p>
        
        <spf:form method="POST" class="form-signin" modelAttribute="Model">
            <spf:errors path="*" cssClass="error"/>
            <fmt:message key="EmailAddress" var="EmailAddress"/>
            <fmt:message key="Subject" var="Subject"/>
            <fmt:message key="Message" var="Message"/>
            <spf:input type="email" path="from" class="form-control form-top-element" placeholder="${EmailAddress}"/>
            <spf:input type="text" path="subject" class="form-control form-center-element" placeholder="${Subject}"/>
            <spf:textarea path="body" class="form-control form-bottom-element" placeholder="${Message}"  style="height: 200px;"/>
            <button class="btn btn-primary btn-block unit" type="submit"><fmt:message key="Send"/></button>
        </spf:form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
