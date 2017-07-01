<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/${moduleName}"><fmt:message key="Players"/></a></li>
            <li><a href="/${moduleName}/edit/${Model.id}">${Model}</a></li>
            <li class="active"><fmt:message key="Delete"/></li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="DeleteWarning"/></h4>
            </div>
            <div class="panel-body">
                <div class="alert alert-danger">${error}</div>

                <h4><fmt:message key="AreYouSureYouWantToDelete"><fmt:param value="${Model}"/></fmt:message></h4>

                <c:if test="${not empty Bookings or not empty Games or not empty Teams or not empty Events}">
                <h4><fmt:message key="TheFollowingObjectsWillBeDeletedAsWell"/></h4>
                
                    <c:if test="${not empty Bookings}">
                        <h4><fmt:message key="Bookings"/>:</h4>
                        <c:forEach var="Booking" items="${Bookings}">
                            <div>${Booking}</div>
                        </c:forEach>
                    </c:if>
                            
                    <c:if test="${not empty Games}">
                        <h4><fmt:message key="Games"/>:</h4>
                        <c:forEach var="Game" items="${Games}">
                            <div>${Game}</div>
                        </c:forEach>
                    </c:if>
                            
                    <c:if test="${not empty Events}">
                        <h4><fmt:message key="EventParticipations"/>:</h4>
                        <c:forEach var="Event" items="${Events}">
                            <div>${Event}</div>
                        </c:forEach>
                    </c:if>
                            
                    <c:if test="${not empty Teams}">
                        <h4><fmt:message key="Teams"/>:</h4>
                        <c:forEach var="Team" items="${Teams}">
                            <div>${Team}</div>
                        </c:forEach>
                    </c:if>
                </c:if>

                <form method="POST">
                    <a class="btn btn-primary btn-back unit"><fmt:message key="Cancel"/></a>
                    <button class="btn btn-danger unit" style="margin-left: 10px;"><fmt:message key="Delete"/></button>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
