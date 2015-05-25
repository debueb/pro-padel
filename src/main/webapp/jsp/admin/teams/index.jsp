<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="AllTeams"/></h4>
            </div>
            <div class="panel-body">



                <table class="table table-striped table-bordered datatable">
                    <thead>
                    <th><fmt:message key="TeamName"/></th>
                    <th><fmt:message key="Players"/></th>
                    <th class="delete"><fmt:message key="Delete"/></th>
                    </thead>
                    <tbody>
                        <c:forEach var="Team" items="${Models}">
                            <tr>
                                <td><a class="ajaxify" href="/admin/teams/edit/${Team.id}">${Team.name}</a></td>
                                <td>
                                    <c:forEach var="Player" items="${Team.players}" varStatus="status">
                                        <a class="ajaxify" href="/admin/players/edit/${Player.id}">${Player}</a>${status.last ? "" : ", "}
                                    </c:forEach>
                                </td>
                                <td class="delete"><a href="/admin/teams/${Team.id}/delete" type="btn btn-primary" class="fa fa-minus-circle ajaxify"></a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <a href="/admin/teams/add" class="btn btn-primary unit ajaxify"><fmt:message key="NewTeam"/></a>
            </div>
        </div>

    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
