<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<jsp:include page="/WEB-INF/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <div class="page-header"></div>

        <jsp:include page="/WEB-INF/jsp/events/include/info.jsp"/>
        
        <div class="panel panel-info">
            
            <div class="panel-heading">
                <h4><fmt:message key="EndGroupGames"/></h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" modelAttribute="Model">
                    <spf:input type="hidden" path="id"/>
                    <spf:input type="hidden" path="name"/>
                    <spf:input type="hidden" path="startDate"/>
                    <spf:input type="hidden" path="endDate"/>
                    <spf:input type="hidden" path="eventType"/>
                    <spf:input type="hidden" path="gender"/>
                    <spf:input type="hidden" path="description"/>
                    <div class="alert alert-danger"><spf:errors path="*"/></div>
                    <div class="alert alert-warning unit">
                        <fmt:message key="ConfirmGroupPhaseEnd"><fmt:param value="${Model.name}"/></fmt:message>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block unit-2"><fmt:message key="Confirm"/></button>
                    <a class="btn btn-primary btn-block" href="${contextPath}/events/event/${Model.id}"><fmt:message key="Cancel"/></a>
                </spf:form>
            </div>
        </div>
        
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
