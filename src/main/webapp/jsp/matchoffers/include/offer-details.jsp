<%@include file="/jsp/include/include.jsp"%>
<div class="row">
    <div class="col-xs-3 booking-cell"><fmt:message key="OfferBy"/>:</div>
    <div class="col-xs-9 booking-cell">${Model.owner}</div>
</div>  

<div class="row">
    <div class="col-xs-3 booking-cell"><fmt:message key="Participants"/>:</div>
    <div class="col-xs-9 booking-cell">
        <c:forEach var="Player" items="${Model.players}" varStatus="status">
            <c:set var="Player" value="${Player}" scope="request"/>
            <div class="${status.first ? '' : 'unit'}">
                <jsp:include page="/jsp/players/include/profile-image.jsp"><jsp:param name="includeName" value="true"/></jsp:include>
                </div>
        </c:forEach>
    </div>
</div>

<div class="row">
    <div class="col-xs-3 booking-cell"><fmt:message key="SkillLevel"/>:</div>
    <div class="col-xs-9 booking-cell">
        <c:forEach var="SkillLevel" items="${Model.skillLevels}" varStatus="status">
            <fmt:message key="${SkillLevel}"/>
            ${not status.last ? ', ' : ''}
        </c:forEach>
    </div>
</div>

<div class="row">
    <div class="col-xs-3 booking-cell"><fmt:message key="Date"/>:</div>
    <div class="col-xs-9 booking-cell"><joda:format value="${Model.startDate}" pattern="EEEE, dd. MMMM yyyy"/></div>
</div>                   
<div class="row">
    <div class="col-xs-3 booking-cell"><fmt:message key="Time"/>:</div>
    <div class="col-xs-9 booking-cell"><joda:format value="${Model.startTime}" pattern="HH:mm"/> - <joda:format value="${Model.endTime}" pattern="HH:mm"/></div>
</div>