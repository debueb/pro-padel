<%@include file="/jsp/include/include.jsp"%>
<div class="row pageentry">
    <c:if test="${not PageEntry.fullWidth}"><div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2"></c:if>
    ${PageEntry.message}
    <c:if test="${not PageEntry.fullWidth}"></div></c:if>
</div>
<c:if test="${PageEntry.showContactForm}">
    <div class="row pageentry">
        <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">

            <div class="panel panel-info relative">
                <div class="panel-heading">
                    <h4><fmt:message key="Contact"/></h4>
                </div>
                <div class="panel-body">
                    <spf:form method="POST" class="form-signin unit" modelAttribute="Mail">
                        <spf:errors path="*" cssClass="error"/>
                        <div class="relative">
                            <spf:input type="email" path="from" class="form-control form-top-element"/>
                            <div class="explanation">
                                <fmt:message key="EmailAddress"/>
                            </div>
                        </div>
                        <div class="relative">
                            <spf:input type="text" path="subject" class="form-control form-center-element"/>
                            <div class="explanation">
                                <fmt:message key="Subject"/>
                            </div>
                        </div>
                        <div class="relative">
                            <spf:textarea path="body" class="form-control form-bottom-element" style="height: 200px;"/>
                            <div class="explanation">
                                <fmt:message key="Message"/>
                            </div>
                        </div>
                        <button class="btn btn-primary btn-block unit" type="submit"><fmt:message key="Send"/></button>
                    </spf:form>
                </div>
            </div>
        </div>
    </div>
</c:if>