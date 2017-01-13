<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>
        
        <ol class="unit-2 breadcrumb">
            <li><a class="ajaxify" href="/admin"><fmt:message key="Administration"/></a></li>
            <li class="active"><fmt:message key="Teams"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="AllTeams"/></h4>
            </div>
            <div class="panel-body">

                <jsp:include page="/jsp/admin/include/search.jsp"/>

                <table class="table table-striped table-bordered">
                    <thead>
                    <th><fmt:message key="TeamName"/></th>
                    <th><fmt:message key="Players"/></th>
                    <th><fmt:message key="Community"/></th>
                    <th class="text-center"><fmt:message key="SendMail"/></th>
                    <th class="delete"><fmt:message key="Delete"/></th>
                    </thead>
                    <tbody>
                        <c:forEach var="Team" items="${Page.content}">
                            <tr>
                                <td><a class="ajaxify" href="/admin/teams/edit/${Team.id}">${Team.name}</a></td>
                                <td>
                                    <c:forEach var="Player" items="${Team.players}" varStatus="status">
                                        <a class="ajaxify" href="/admin/players/edit/${Player.id}">${Player}</a>${status.last ? "" : ", "}
                                    </c:forEach>
                                </td>
                                <td>${Team.community}</td>
                                <td><a class="block text-center" href="mailto:${Team.mailTo}"><i class="fa fa-envelope"></i></a></td>
                                <td class="delete"><a href="/admin/teams/${Team.id}/delete" type="btn btn-primary" class="fa fa-minus-circle ajaxify"></a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                    
                <jsp:include page="/jsp/admin/include/pagination.jsp"/>
                
                <a href="/admin/teams/add" class="btn btn-primary btn-block unit ajaxify"><fmt:message key="NewTeam"/></a>
            </div>
        </div>

    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
