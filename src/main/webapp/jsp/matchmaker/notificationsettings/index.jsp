<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>
        
        <div class="page-header">
            <h1><fmt:message key="NotificationSettings"/></h1>
        </div>
        
        
        <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
            <spf:input type="hidden" path="id"/>
            <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>
            
            <div class="unit">
                <spf:checkbox path="enabled" id="active"/><label for="active"><fmt:message key="NotifyMeAboutNewMatchOffers"/></label>
            </div>
            
            <span class="relative block">
            <spf:select path="skillLevels" class="select-multiple show-tick form-control" data-style="form-bottom-element" title="${SkillLevel}" multiple="true">
                <c:forEach var="SkillLevel" items="${SkillLevels}">
                    <c:set var="selected" value="${fn:contains(Model.skillLevels, SkillLevel) ? 'selected': 'false'}"/>
                    <spf:option value="${SkillLevel}"><fmt:message key="${SkillLevel}"/></spf:option>
                </c:forEach>
            </spf:select>
                <span class="explanation-select"><fmt:message key="SkillLevel"/></span>
            </span>
                       
            
            <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
      </spf:form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
