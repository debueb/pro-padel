<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <ol class="unit-2 breadcrumb">
            <li><a class="ajaxify" href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a class="ajaxify" href="/admin/communities"><fmt:message key="Communities"/></a></li>
            <li class="active"><fmt:message key="Community"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <fmt:message key="NewCommunity" var="NewCommunity"/>
                <fmt:message key="EditCommunity" var="EditCommunity"/>
                <h4>${empty Model.name ? NewCommunity : EditCommunity}</h4>
            </div>
            <div class="panel-body">

                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <spf:input type="hidden" path="id"/>
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>

                    <div class="unit">
                        <fmt:message key="Name" var="Name"/>
                        <spf:input path="name" type="text" class="form-control" placeholder="${Name}" />
                    </div>

                    <button class="btn btn-primary btn-block btn-form-submit unit-2" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>
    </div></div>

<jsp:include page="/jsp/include/footer.jsp"/>
