<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <fmt:message key="${empty Model.id ? 'NewStaffMember' : 'EditStaffMember'}" var="Title"/>
        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/staff"><fmt:message key="StaffMembers"/></a></li>
            <li class="active">${Title}</li>
        </ol>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${Title}</h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" modelAttribute="Model" enctype="multipart/form-data">
                    <spf:input type="hidden" path="id"/>
                    <spf:input type="hidden" path="profileImage.id"/>
                    <spf:input type="hidden" path="position"/>
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>

                    <div class="relative">
                        <spf:input path="name" type="text" class="form-control form-top-element"/>
                        <div class="explanation"><fmt:message key="Name"/></div>
                    </div>
                    <div class="relative">
                        <spf:input path="teaser" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="Teaser"/></div>
                    </div>
                    <div class="relative">
                        <spf:input path="description" type="text" class="form-control form-bottom-element"/>
                        <div class="explanation"><fmt:message key="DescriptionMax255"/></div>
                    </div>

                    <figure class="picture text-center unit-2">
                        <span class="polaroid">
                            <c:choose>
                                <c:when test="${empty Model.profileImage}">
                                    <span class="fa-stack fa-5x">
                                        <i class="fa fa-circle fa-stack-2x fa-inverse"></i>
                                        <i class="fa fa-user fa-stack-1x"></i>
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <img src="/images/image/${Model.profileImage.sha256}"/>
                                </c:otherwise>
                            </c:choose>
                        </span>
                        <div class="unit picture-subtext"><fmt:message key="ClickImageToChange"/></div>
                    </figure>
                    <spf:input type="file" capture="camera" accept="image/*" path="profileImageFile" class="picture-input hidden"/>

                    <button class="btn btn-primary btn-block btn-form-submit unit-2" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/admin/include/colorpicker.jsp"/>