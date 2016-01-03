<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="AllContacts"/></h4>
            </div>
        </div>


        <div class="table-responsive">
            <table class="table table-striped table-bordered">
                <thead>
                <th><fmt:message key="Name"/></th>
                <th><fmt:message key="EmailAddress"/></th>
                <th class="delete"><fmt:message key="Delete"/></th>
                </thead>
                <tbody>
                    <c:forEach var="Contact" items="${Models}">
                        <tr>
                            <td><a class="ajaxify" href="/admin/contact/edit/${Contact.id}">${Contact.emailDisplayName}</a></td>
                            <td><a class="ajaxify" href="/admin/contact/edit/${Contact.id}">${Contact.emailAddress}</a></td>
                            <td class="delete"><a href="/admin/contact/${Contact.id}/delete" type="btn btn-primary" class="fa fa-minus-circle ajaxify"></a></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <a href="/admin/contact/add" class="btn btn-primary unit ajaxify"><fmt:message key="NewContact"/></a>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
