<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info unit">
            <div class="panel-heading">
                <fmt:message var="NewAdminGroup" key="NewAdminGroup"/>
                <fmt:message var="EditAdminGroup" key="EditAdminGroup"/>
                <h4>${empty Model.name ? NewAdminGroup : EditAdminGroup}</h4></div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <spf:input type="hidden" path="id"/>
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>
                    <fmt:message var="GroupName" key="GroupName"/>
                    <spf:input path="name" type="text" class="form-control form-top-element" placeholder="${GroupName}"/>

                    <fmt:message var="Members" key="Members"/>
                    <spf:select path="players" class="select-multiple show-tick form-control" data-style="form-center-element" title="${Members}" multiple="true" data-live-search="true" data-container="body">
                        <spf:options items="${AdminPlayers}" itemValue="id"/>
                        <spf:options items="${AllPlayers}" itemValue="id"/>
                    </spf:select>

                    <fmt:message var="PrivilegesLabel" key="PrivilegesLabel"/>
                    <spf:select path="privileges" class="select-multiple show-tick form-control" data-style="form-bottom-element" title="${PrivilegesLabel}" multiple="true" data-container="body">
                        <spf:options items="${Privileges}"/>
                        <spf:options items="${AllPrivileges}"/>
                    </spf:select>

                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>


        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
