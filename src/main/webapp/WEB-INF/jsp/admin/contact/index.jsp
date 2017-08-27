<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/WEB-INF/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li class="active"><fmt:message key="ContactSettings"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="ContactSettings"/></h4>
            </div>
            <div class="panel-body">
                <div class="table-responsive unit-2">
                    <table class="table table-striped table-bordered">
                        <thead>
                        <th><fmt:message key="Name"/></th>
                        <th><fmt:message key="EmailAddress"/></th>
                        <th class="text-center"><fmt:message key="NotifyOnContactForm"/></th>
                        <th class="text-center"><fmt:message key="NotifyOnBooking"/></th>
                        <th class="text-center"><fmt:message key="NotifyOnBookingCancellation"/></th>
                        <th class="delete"><fmt:message key="Delete"/></th>
                        </thead>
                        <tbody>
                            <c:forEach var="Contact" items="${Models}">
                                <c:set var="url" value="/admin/contact/edit/${Contact.id}"/>
                                <tr>
                                    <td><a href="${url}">${Contact.emailDisplayName}</a></td>
                                    <td><a href="${url}">${Contact.emailAddress}</a></td>
                                    <td class="text-center"><a href="${url}"><i class="fa fa-${Contact.notifyOnContactForm ? 'check' : 'remove'}"/></a></td>
                                    <td class="text-center"><a href="${url}"><i class="fa fa-${Contact.notifyOnBooking ? 'check' : 'remove'}"/></a></td>
                                    <td class="text-center"><a href="${url}"><i class="fa fa-${Contact.notifyOnBookingCancellation ? 'check' : 'remove'}"/></a></td>
                                    <td class="delete"><a href="/admin/contact/${Contact.id}/delete" type="btn btn-primary" class="fa fa-minus-circle"></a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <a href="/admin/contact/add" class="btn btn-primary btn-block unit"><fmt:message key="NewContact"/></a>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
