<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Modules"/></h4>
            </div>
            <div class="panel-body">
                <div class="dd">
                    <ol class="dd-list">
                        <c:forEach var="Model" items="${Models}">
                            <li class="dd-item" data-id="${Model.id}">
                                <div class="dd-handle dd3-handle"></div>
                                <a class="ajaxify" href="/admin/general/modules/edit/${Model.id}">
                                    <div class="dd3-content">${Model.title}<span class="float-right"><fmt:message key="${Model.moduleType}"/></span></div>
                                </a>
                                <c:if test="${not empty Model.subModules}">
                                    <ol class="dd-list">
                                        <c:forEach var="SubModule" items="${Model.subModules}">
                                            <li class="dd-item" data-id="${SubModule.id}">
                                                <div class="dd-handle dd3-handle"></div>
                                                <a class="ajaxify" href="/admin/general/modules/edit/${SubModule.id}">
                                                    <div class="dd3-content">${SubModule.title}<span class="float-right"><fmt:message key="${SubModule.moduleType}"/></span></div>
                                                </a>
                                            </li>
                                        </c:forEach>
                                    </ol>
                                </c:if>
                            </li>
                        </c:forEach>
                    </ol>
                </div>
                <div class="clearfix"></div>
                
                <a href="/admin/general/modules/add" class="btn btn-primary btn-block unit-2 ajaxify"><fmt:message key="Add"/></a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/admin/include/nestable.jsp"/>
<jsp:include page="/jsp/include/footer.jsp"/>
