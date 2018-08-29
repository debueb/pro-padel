<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        <fmt:message key="${empty Model.id ? 'AddBalanceEntry' : 'EditBalanceEntry'}" var="Title"/>

        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/players"><fmt:message key="Players"/></a></li>
            <li><a href="/admin/players/edit/${Player.id}">${Player}</a></li>
            <li><a href="/admin/players/${Player.UUID}/transactions"><fmt:message key="Balance" /></a></li>
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
                    <div class="relative">
                        <spf:input path="comment" type="text" class="form-control form-top-element"/>
                        <div class="explanation"><fmt:message key="Comment"/></div>
                    </div>
                    <div class="datepicker-container">
                        <div class="datepicker-text-container">
                            <div class="datepicker-label"><fmt:message key="Date"/></div>
                            <span class="fa fa-calendar datepicker-icon"></span>
                            <div class="datepicker-text"></div>
                        </div>
                        <spf:input type="hidden" path="date" class="datepicker-input form-control" value="${Model.date}"/>
                        <div class="datepicker" data-show-on-init="false" data-allow-past="true"></div>
                    </div>
                    <div class="relative">
                        <spf:input path="amount" type="number" class="form-control form-bottom-element"/>
                        <div class="explanation"><fmt:message key="Amount" /></div>
                    </div>
                    <button class="btn btn-primary btn-block btn-form-submit unit-2" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
