<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li class="active"><fmt:message key="Subscriptions"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Subscriptions"/></h4>
            </div>
            <div class="panel-body">

                <table class="table table-striped table-bordered">
                    <thead>
                    <th><fmt:message key="Subscription"/></th>
                    <th><fmt:message key="Players"/></th>
                    <th class="delete"><fmt:message key="Delete"/></th>
                    </thead>
                    <tbody>
                        <c:forEach var="Model" items="${Page.content}">
                            <tr>
                                <td><a href="/${moduleName}/edit/${Model.id}">${Model.name}</a></td>
                                <td>
                                    <c:forEach var="Player" items="${Model.players}" varStatus="status">
                                        <a href="/admin/players/edit/${Player.id}">${Player}</a>${status.last ? "" : ", "}
                                    </c:forEach>
                                </td>
                                <td class="delete"><a href="/${moduleName}/${Model.id}/delete" type="btn btn-primary" class="fa fa-minus-circle"></a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <jsp:include page="/WEB-INF/jsp/admin/include/pagination.jsp"/>

                <a href="/${moduleName}/add" class="btn btn-primary btn-block unit"><fmt:message key="NewSubscription"/></a>
            </div>
        </div>

    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
