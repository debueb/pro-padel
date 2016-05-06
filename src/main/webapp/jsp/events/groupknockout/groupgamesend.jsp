<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <div class="panel panel-info">
            
            <div class="panel-heading">
                <h4><fmt:message key="EndGroupGames"/></h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" modelAttribute="Model">
                    <spf:input type="hidden" path="id"/>
                    <spf:input type="hidden" path="name"/>
                    <div class="alert alert-danger"><spf:errors path="*"/></div>
                    <div class="alert alert-warning unit">
                        <fmt:message key="ConfirmGroupPhaseEnd"><fmt:param value="${Model.name}"/></fmt:message>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block"><fmt:message key="Confirm"/></button>
                    <a class="btn btn-primary btn-block" href="${contextPath}/events/event/${Model.id}"><fmt:message key="Cancel"/></a>
                </spf:form>
            </div>
        </div>
        
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
