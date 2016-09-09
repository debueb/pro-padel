<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/${path}include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="Contact"/></h4></div>
            <div class="panel-body"><fmt:message key="ContactDescription"/>

                <spf:form method="POST" class="form-signin unit" modelAttribute="Model">
                    <div class="alert alert-danger"><spf:errors path="*" cssClass="error"/></div>
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
    </div>
</div>

<jsp:include page="/jsp/${path}include/footer.jsp"/>
