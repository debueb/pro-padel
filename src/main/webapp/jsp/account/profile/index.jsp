<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>


        <spf:form modelAttribute="Model" enctype="multipart/form-data">
            <spf:input path="id" type="hidden"/>
            <div class="alert alert-danger"><spf:errors path="*"/></div>
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h4><fmt:message key="Profile"/></h4>
                </div>
                <div class="panel-body">
                    <jsp:include page="/jsp/include/player-input.jsp"/>
                </div>
            </div>
            <div class="panel panel-info unit">
                <div class="panel-heading">
                    <h4><fmt:message key="ProfilePicture"/></h4>
                </div>
                <div class="panel-body">
                    <div class="col-xs-12 text-center">
                        <figure class="picture text-center">
                            <c:set var="Player" value="${Model}" scope="request"/>
                            <jsp:include page="/jsp/players/include/profile-image.jsp"/>
                            <div class="unit picture-subtext"><fmt:message key="ClickImageToChange"/></div>
                        </figure>
                        <spf:input type="file" capture="camera" accept="image/*" path="profileImageMultipartFile" class="picture-input hidden"/>
                    </div>
                    <div class="clearfix"></div>
                </div>
            </div>
<!--            <div class="panel panel-info unit">
                <div class="panel-heading">
                    <h4><fmt:message key="MySkillLevel"/></h4>
                </div>
                <div class="panel-body">
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
            </div>-->
            <div class="panel panel-info unit">
                <div class="panel-heading">
                    <h4><fmt:message key="NotificationSettings"/></h4>
                </div>
                <div class="panel-body">
                    <spf:checkbox path="enableMatchNotifications" id="active"/><label for="active"><fmt:message key="NotifyMeAboutNewMatchOffers"/></label>

                    <div class="relative">
                        <fmt:message key="SkillLevel" var="SkillLevelMsg"/>
                        <spf:select path="notificationSkillLevels" class="select-multiple show-tick form-control" title="${SkillLevelMsg}" multiple="true" data-container="body">
                            <c:forEach var="SkillLevel" items="${SkillLevels}">
                                <c:set var="selected" value="${fn:contains(Model.notificationSkillLevels, SkillLevel) ? 'selected': 'false'}"/>
                                <spf:option value="${SkillLevel}"><fmt:message key="${SkillLevel}"/></spf:option>
                            </c:forEach>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="SkillLevel"/></span>
                    </div>
                </div>
            </div>
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h4><fmt:message key="AvailableTimes"/></h4>
                </div>
                <div class="panel-body">
                    <spf:input type="hidden" path="daySchedules"/>
                    <div id="day-schedule"></div>
                    
                    <div class="stretch col-leyenda-header unit"><fmt:message key="Leyenda"/></div>
                    <div class="container-flex stretch">
                        <span class="col-flex col-leyenda schedule-unavailable"><fmt:message key="IAmNotAvailable"/></span>
                        <span class="col-flex col-leyenda schedule-available"><fmt:message key="IAmAvailable"/></span>
                    </div>
                    
                    <script>
                        var initDayScheduler = function () {
                            if ($("#day-schedule").length > 0) {
                                $("#day-schedule").dayScheduleSelector({
                                    stringDays: ['Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So'],
                                    days: [0, 1, 2, 3, 4, 5, 6],
                                    interval: 30,
                                    startTime: '08:00',
                                    endTime: '23:30'
                                });
                                $("#day-schedule").on('selected.artsy.dayScheduleSelector', function (e) {
                                    var selectedSlots = $(e.target).data('artsy.dayScheduleSelector').serialize();
                                    $('input[name="daySchedules"]').val(JSON.stringify(selectedSlots));
                                });
                                var schedule = JSON.parse($('input[name="daySchedules"]').val()) || [];
                                console.log(schedule);
                                $("#day-schedule").data('artsy.dayScheduleSelector').deserialize(schedule);
                            }
                        };

                        $(document).ready(initDayScheduler);
                        $(window).on('statechangecomplete', initDayScheduler);
                    </script>
                </div>
            </div>


            <button class="btn btn-primary btn-block unit" type="submit"><fmt:message key="Save"/></button>
        </spf:form>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
