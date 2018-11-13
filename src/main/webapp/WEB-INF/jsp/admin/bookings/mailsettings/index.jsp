<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        
        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/bookings"><fmt:message key="Bookings"/></a></li>
            <li class="active"><fmt:message key="MailSettings"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="MailSettings"/></h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <spf:input type="hidden" path="id"/>
                    <div class="alert alert-danger"><spf:errors path="*"/></div>

                    <div class="alert alert-info">
                        <div><fmt:message key="BookingMailVariablesDesc"/></div>
                        <p>
                            <c:forEach var="BookingMailVariable" items="${BookingMailVariables}">
                                <div>${BookingMailVariable.key}</div>
                            </c:forEach>
                        </p>
                    </div>

                    <div class="relative">
                        <spf:textarea path="htmlBodyTemplate" type="text" class="form-control text-editor" rows="40"/>
                        <span class="explanation">Mail Body Template</span>
                    </div>

                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div></div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/admin/include/text-editor.jsp"/>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>