<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <fmt:message key="NewTeam" var="NewTeam"/>
            <fmt:message key="EditTeam" var="EditTeam"/>
            <h1>${empty Model.name ? NewTeam : EditTeam}</h1>
        </div>
        
        
        <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
            <spf:input type="hidden" path="id"/>
            <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>
            
            <fmt:message key="TeamName" var="TeamName"/>
            <spf:input path="name" type="text" class="form-control form-top-element" placeholder="${TeamName}" />
            
            <fmt:message key="Players" var="Players"/>
            <spf:select path="players" class="select-multiple show-tick form-control" data-style="form-bottom-element" title="${Players}" multiple="true">
                <spf:options items="${TeamPlayers}" itemLabel="displayName" itemValue="id"/>
                <spf:options items="${AllPlayers}" itemLabel="displayName" itemValue="id"/>
            </spf:select>
            
            <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
      </spf:form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
