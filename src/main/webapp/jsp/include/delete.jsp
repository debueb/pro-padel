<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="DeleteWarning"/></h4>
            </div>
            <div class="panel-body">
                <div class="alert alert-danger">${error}</div>
                <c:choose>
                    <c:when test="${empty Model}">
                        <div class="alert alert-danger">
                            <fmt:message key="ObjectDoesNotExist"/>
                        </div>
                        <p><a class="btn btn-primary btn-lg" href="/"><fmt:message key="Home"/></a>&nbsp;<a class="btn btn-primary btn-lg" href="/contact"><fmt:message key="Contact"/></a></p>
                    </c:when>
                    <c:otherwise>
                        <h4><fmt:message key="AreYouSureYouWantToDelete"><fmt:param value="${Model}"/></fmt:message></h4>

                        <form method="POST">
                            <a class="btn btn-primary btn-back unit"><fmt:message key="Cancel"/></a>
                            <jsp:include page="/jsp/include/input-redirect.jsp"/>
                            <button class="btn btn-danger unit" style="margin-left: 10px;"><fmt:message key="Delete"/></button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
