<%@include file="/jsp/include/include.jsp"%>

<div class="page-header"></div>

<div class="panel panel-info">
    <div class="panel-heading">
        <h4> ${Model.name}</h4>
    </div>
    <div class="panel-body">
        <div class="container-fluid">
            <div class="col-xs-4 text-right">
                <fmt:message key="Date"/>:
            </div>
            <div class="col-xs-8">
                <joda:format value="${Model.startDate}" pattern="EEEE, dd. MMMM yyyy"/> - <joda:format value="${Model.endDate}" pattern="EEEE, dd. MMMM yyyy"/>
            </div>
            <div class="col-xs-4 text-right">
                <fmt:message key="Category"/>:
            </div>
            <div class="col-xs-8">
                <fmt:message key="${Model.gender}"/>
            </div>
            <div class="col-xs-4 text-right">
                <fmt:message key="Mode"/>:
            </div>
            <div class="col-xs-8">
                <fmt:message key="${Model.eventType}"/>
            </div>
            <c:if test="${not empty Model.location}">
                <div class="col-xs-4 text-right">
                    <fmt:message key="Location"/>:
                </div>
                <div class="col-xs-8">
                    <a href="https://www.google.com/maps/search/${Model.location}" target="blank">${Model.location}</a>
                </div>
            </c:if>
        </div>
    </div>
</div>
