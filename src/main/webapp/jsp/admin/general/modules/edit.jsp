<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        <div class="panel panel-info">
            <div class="panel-heading">
                <fmt:message var="AddModule" key="AddModule"/>
                <fmt:message var="EditModule" key="EditModule"/>
                <h4>${empty Model.id ? AddModule : EditModule}</h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model" id="news-form">
                    <spf:input type="hidden" path="id"/>
                    <spf:input type="hidden" path="position"/>
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>

                    <spf:select path="moduleType" type="text" class="form-control form-top-element select-simple">
                        <spf:options items="${ModuleTypes}" />
                    </spf:select>
                    <spf:select path="iconName" type="text" class="form-control form-center-element select-simple" data-live-search="true">
                        <c:forEach var="IconName" items="${FontAwesomeIconNames}">
                            <spf:option data-icon="fa-${IconName}" value="${IconName}">${IconName}</spf:option>
                        </c:forEach>
                    </spf:select>
                    <fmt:message var="Title" key="Title"/>
                    <spf:input path="title" type="text" class="form-control form-bottom-element" placeholder="${Title}"/>
                    <div>
                        <spf:checkbox path="showInMenu" />
                        <label class="checkbox" for="showInMenu1"><fmt:message key="ShowInMenu"/></label>
                    </div>
                    <div>
                        <spf:checkbox path="showInFooter" />
                        <label class="checkbox" for="showInFooter1"><fmt:message key="ShowInFooter"/></label>
                    </div>
                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                    <a class="btn btn-primary btn-block btn-back unit ajaxify"><fmt:message key="Cancel"/></a>

                    <c:if test="${Model.moduleType == 'Page'}">
                        <a class="btn btn-primary btn-block ajaxify" href="/admin/general/modules/page/${Model.id}"><fmt:message key="ManageEntries"/></a>
                    </c:if>
                </spf:form>
            </div>
        </div>
    </div></div>

<jsp:include page="/jsp/include/footer.jsp"/>
