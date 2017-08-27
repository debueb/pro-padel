<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<a href="${OfferURL}" class="list-group-item">
    <div class="list-item-text">
        <joda:format value="${Model.startDate}" pattern="EEEE, dd. MMMM yyyy"/> <joda:format value="${Model.startTime}" pattern="HH:mm"/> - <joda:format value="${Model.endTime}" pattern="HH:mm"/> 
        <br/>
        <fmt:message key="SkillLevel"/>:
        <c:forEach var="SkillLevel" items="${Model.skillLevels}" varStatus="status">
            <fmt:message key="${SkillLevel}"/>${status.last ? '' : ' - '}
        </c:forEach>
    </div>
</a>