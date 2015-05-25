<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Profile"/></h4>
            </div>
            <div class="panel-body">
                <spf:form class="form-signin" role="form" modelAttribute="Model" enctype="multipart/form-data">
                    <div class="alert alert-danger"><spf:errors path="*"/></div>

                    <spf:input path="id" type="hidden"/>

                    <div class="accordion" id="account-profile">
                        <div><fmt:message key="General"/></div>
                        <div>
                            <jsp:include page="/jsp/include/player-input.jsp"/>
                        </div>
                        <div><fmt:message key="ProfilePicture"/></div>
                        <div>
                            <div class="col-xs-12 text-center">
                                <figure id="profile-picture">
                                    <c:set var="Player" value="${Model}" scope="request"/>
                                    <jsp:include page="/jsp/players/include/profile-image.jsp"/>
                                    <div id="profile-picture-subtext" class="unit"><fmt:message key="ClickImageToUploadProfilePicture"/></div>
                                </figure>
                                <spf:input type="file" capture="camera" accept="image/*" id="profile-picture-input" path="profileImageMultipartFile" class="hidden"/>
                            </div>
                            <div class="clearfix"></div>
                        </div>
                        <div><fmt:message key="NotificationSettings"/></div>
                        <div>
                            <spf:checkbox path="enableMatchNotifications" id="active"/><label for="active"><fmt:message key="NotifyMeAboutNewMatchOffers"/></label>

                            <span class="relative block">
                                <fmt:message key="SkillLevel" var="SkillLevelMsg"/>
                                <spf:select path="notificationSkillLevels" class="select-multiple show-tick form-control" title="${SkillLevelMsg}" multiple="true">
                                    <c:forEach var="SkillLevel" items="${SkillLevels}">
                                        <c:set var="selected" value="${fn:contains(Model.notificationSkillLevels, SkillLevel) ? 'selected': 'false'}"/>
                                        <spf:option value="${SkillLevel}"><fmt:message key="${SkillLevel}"/></spf:option>
                                    </c:forEach>
                                </spf:select>
                                <span class="explanation-select"><fmt:message key="SkillLevel"/></span>
                            </span>
                        </div>
                        <div><fmt:message key="MySkillLevel"/></div>
                        <div class="row form-group product-chooser">
                            <c:forEach var="SkillLevel" items="${SkillLevels}">
                                <div class="col-xs-12 col-sm-6 col-md-4">
                                    <div class="product-chooser-item ${Model.skillLevel == SkillLevel ? 'selected' : ''}">
                                        <div class="title"><fmt:message key="${SkillLevel}"/></div>
                                        <span class="description"><fmt:message key="${SkillLevel}-desc"/></span>
                                        <spf:radiobutton checked="${Model.skillLevel == SkillLevel ? 'checked' : ''}" path="skillLevel" value="${SkillLevel}"/>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <button class="btn btn-primary btn-block unit" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
