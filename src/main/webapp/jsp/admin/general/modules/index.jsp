<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>
<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/general"><fmt:message key="General"/></a></li>
            <li class="active"><fmt:message key="Modules"/></li>
        </ol>
        
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
                                <div class="dd3-content dd-nodrag">
                                    <a class="no-ajaxify block" href="/admin/general/modules/edit/${Model.id}">
                                        ${Model.title}<span class="float-right"><fmt:message key="${Model.moduleType}"/></span>
                                    </a>
                                </div>
                                <c:if test="${not empty Model.subModules}">
                                    <ol class="dd-list">
                                        <c:forEach var="SubModule" items="${Model.subModules}">
                                            <li class="dd-item" data-id="${SubModule.id}">
                                                <div class="dd-handle dd3-handle"></div>
                                                <div class="dd3-content dd-nodrag">
                                                    <a class="no-ajaxify block" href="/admin/general/modules/edit/${SubModule.id}">
                                                        ${SubModule.title}<span class="float-right"><fmt:message key="${SubModule.moduleType}"/></span>
                                                    </a>
                                                </div>
                                            </li>
                                        </c:forEach>
                                    </ol>
                                </c:if>
                            </li>
                        </c:forEach>
                    </ol>
                </div>
                <div class="clearfix"></div>
                
                <a href="/admin/general/modules/add" class="btn btn-primary btn-block unit-2"><fmt:message key="Add"/></a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/admin/include/nestable.jsp"/>
<jsp:include page="/jsp/include/footer.jsp"/>
