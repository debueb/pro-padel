<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li class="active"><fmt:message key="Communities"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Communities"/></h4>
            </div>
            <div class="panel-body">

                <jsp:include page="/WEB-INF/jsp/admin/include/search.jsp"/>

                <table class="table table-striped table-bordered">
                    <thead>
                        <th><fmt:message key="CommunityName"/></th>
                        <th><fmt:message key="Players"/></th>
                        <th class="text-center"><fmt:message key="SendMail"/></th>
                    </thead>
                    <tbody>
                        <c:forEach var="Community" items="${Page.content}">
                            <tr>
                                <td><a href="/${moduleName}/edit/${Community.id}">${Community.name}</a></td>
                                <td>
                                    <c:forEach var="Player" items="${Community.players}" varStatus="status">
                                        <a href="/${moduleName}/edit/${Community.id}">${Player}</a>${status.last ? "" : ", "}
                                    </c:forEach>
                                </td>
                                <td><a class="block text-center" href="/admin/mail/community/${Community.id}"><i class="fa fa-envelope"></i></a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <jsp:include page="/WEB-INF/jsp/admin/include/pagination.jsp"/>
            </div>
        </div>

    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
