<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header">
            <h1><fmt:message key="Profile"/></h1>
        </div>

        <spf:form class="form-signin" role="form" modelAttribute="Model" enctype="multipart/form-data">
            <div class="alert alert-danger"><spf:errors path="*"/></div>

            <spf:input path="id" type="hidden"/>

            <jsp:include page="/jsp/include/player-input.jsp"/>

            <hr/>
            
            <!-- picture -->
            <div class="col-xs-12 text-center">
                <figure id="profile-picture">
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
                    <div id="profile-picture-subtext"><fmt:message key="ClickImageToUploadProfilePicture"/></div>
                </figure>
                <spf:input type="file" capture="camera" accept="image/*" id="profile-picture-input" path="profileImageMultipartFile" class="hidden"/>
            </div>
            <div class="clearfix"></div>
            <hr/>
          
            <!-- level -->
            <div class="row form-group product-chooser unit">
                <c:forEach var="level" begin="1" end="5">
                    <div class="col-xs-12 col-sm-6 col-md-4">
                        <div class="product-chooser-item ${Model.skillLevel == level ? 'selected' : ''}">
                            <div class="title"><fmt:message key="Level-${level}"/></div>
                            <span class="description"><fmt:message key="Level-${level}-desc"/></span>
                            <spf:radiobutton checked="${Model.skillLevel == level ? 'checked' : ''}" path="skillLevel" value="${level}"/>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <button class="btn btn-primary btn-block unit" type="submit"><fmt:message key="Save"/></button>
        </spf:form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
