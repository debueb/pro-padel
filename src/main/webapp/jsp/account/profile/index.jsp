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
                    <c:set var="Player" value="${Model}" scope="request"/>
                    <jsp:include page="/jsp/players/include/profile-image.jsp"/>
                    <div id="profile-picture-subtext" class="unit"><fmt:message key="ClickImageToUploadProfilePicture"/></div>
                </figure>
                <spf:input type="file" capture="camera" accept="image/*" id="profile-picture-input" path="profileImageMultipartFile" class="hidden"/>
            </div>
            <div class="clearfix"></div>
            <!--<hr/>-->
          
            <!-- level -->
<!--            <div class="row form-group product-chooser unit">
                <c:forEach var="SkillLevel" items="${SkillLevels}">
                    <div class="col-xs-12 col-sm-6 col-md-4">
                        <div class="product-chooser-item ${Model.skillLevel == SkillLevel ? 'selected' : ''}">
                            <div class="title"><fmt:message key="${SkillLevel}"/></div>
                            <span class="description"><fmt:message key="${SkillLevel}-desc"/></span>
                            <spf:radiobutton checked="${Model.skillLevel == SkillLevel ? 'checked' : ''}" path="skillLevel" value="${SkillLevel}"/>
                        </div>
                    </div>
                </c:forEach>
            </div>-->

            <button class="btn btn-primary btn-block unit" type="submit"><fmt:message key="Save"/></button>
        </spf:form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
