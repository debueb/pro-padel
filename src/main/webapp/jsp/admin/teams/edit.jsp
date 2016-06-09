<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        <div class="panel panel-info">
            <div class="panel-heading">
                <fmt:message key="NewTeam" var="NewTeam"/>
                <fmt:message key="EditTeam" var="EditTeam"/>
                <h4>${empty Model.name ? NewTeam : EditTeam}</h4>
            </div>
            <div class="panel-body">

                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <spf:input type="hidden" path="id"/>
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>
                    <div class="alert alert-info" role="alert"><fmt:message key="LeaveTeamNameEmptyForGeneratedTeamName"/></div>
                    
                    <div class="unit">
                        <fmt:message key="TeamName" var="TeamName"/>
                        <spf:input path="name" type="text" class="form-control form-top-element" placeholder="${TeamName}" />

                        <fmt:message key="Players" var="Players"/>
                        <spf:select path="players" class="select-multiple show-tick form-control" data-style="form-bottom-element" title="${Players}" multiple="true" data-live-search="true" data-container="body">
                            <spf:options items="${TeamPlayers}" itemValue="id"/>
                            <spf:options items="${AllPlayers}" itemValue="id"/>
                        </spf:select>
                    </div>

                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>
    </div></div>

<jsp:include page="/jsp/include/footer.jsp"/>
