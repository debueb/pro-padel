<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        <fmt:message key="${empty Model.id ? 'NewPlayer' : 'EditPlayer'}" var="Title"/>

        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/players"><fmt:message key="Players"/></a></li>
            <li class="active">${Title}</li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${Title}</h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <div class="alert alert-danger"><spf:errors path="*" cssClass="error"/></div>
                    <spf:input path="id" type="hidden"/>
                    <jsp:include page="/WEB-INF/jsp/include/player-input.jsp"/>
                    <c:if test="${Model.deleted}">
                        <div class="relative">
                            <spf:checkbox path="deleted" readonly="readonly"/><label for="deleted"><fmt:message key="DeactivatedAccount"/></label>
                        </div>
                    </c:if>
                    <button class="btn btn-primary btn-block btn-form-submit unit-2" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
