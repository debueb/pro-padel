<%@include file="/jsp/include/include.jsp"%>
<div class="panel panel-info relative">
    <div class="panel-heading">
        <h4>${PageEntry.title}</h4>
        <c:if test="${not empty privileges and fn:contains(privileges, 'ManageGeneral')}">
            <a href="/admin/general/modules/page/${PageEntry.module.id}/edit/${PageEntry.id}" target="blank" class="edit"><i class="fa fa-edit"></i></a>
        </c:if>
    </div>
    <div class="panel-body">${PageEntry.message}
        <c:if test="${PageEntry.showContactForm}">
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
        </c:if>
    </div>
</div>