<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<div class="panel panel-info">
    <div class="panel-heading">
        <h4>${Player}</h4>
    </div>
    <div class="panel-body">
        <div class="text-center">
            <jsp:include page="/WEB-INF/jsp/players/include/profile-image.jsp"/>
        </div>
        <c:choose>
            <c:when test="${empty sessionScope.user}">
                <div class="alert alert-info unit-2">
                    <fmt:message key="LogInToContact"><fmt:param value="${Player}"/></fmt:message>
                </div>
                <c:url value="/players/player/${Player.UUID}" var="playerURL"/>
                <a class="btn btn-primary btn-block unit-2" href="/login?redirect=${playerURL}"><fmt:message key="Login"/></a>
                <a class="btn btn-primary btn-block" href="/login/register?redirect=${playerURL}"><fmt:message key="Register"/></a>
            </c:when>
            <c:when test="${not sessionScope.user.verified and not AccountVerificationLinkSent}">
                <div class="alert alert-info unit-2">
                    <fmt:message key="VerifyAccountToContact"><fmt:param value="${Player}"/></fmt:message>
                </div>
                <form class="unit-2" method="POST" action="/players/player/${Player.UUID}">
                    <button type="submit" class="btn btn-block btn-primary"><fmt:message key="ResendVerificationEmail"/></button>
                </form>
            </c:when>
            <c:when test="${not sessionScope.user.verified and AccountVerificationLinkSent}">
                <div class="alert alert-info unit-2">
                    <fmt:message key="AccountVerificationLinkSent"/>
                </div>
            </c:when>
            <c:otherwise>
                <div class="list-group unit-2">
                    <a href="/ranking/${Player.UUID}/history" class="list-group-item">
                        <div class="list-item-text"><fmt:message key="Ranking"/></div>
                        <div class="list-group-item-icon"><c:choose>
                            <c:when test="${not empty RankingValue}">
                                ${RankingValue}
                            </c:when>
                            <c:otherwise>
                                <div class="fa fa-trophy"></div>
                            </c:otherwise>
                        </c:choose>
                        </div>
                    </a>
                    <a href="/players/player/${Player.UUID}/games" class="list-group-item" data-anchor="#games">
                        <div class="list-item-text"><fmt:message key="GamesWith"><fmt:param>${Player}</fmt:param></fmt:message></div>
                        <div class="list-group-item-icon"><div class="fa fa-soccer-ball-o"></div></div>
                    </a>
                    <a href="/players/player/${Player.UUID}/teams" class="list-group-item" data-anchor="#teams">
                        <div class="list-item-text"><fmt:message key="TeamsWith"><fmt:param>${Player}</fmt:param></fmt:message></div>
                        <div class="list-group-item-icon"><div class="fa fa-group"></div></div>
                    </a>
                    <a class="list-group-item private-data" data-fake="${Player.obfuscatedPhone}" data-prefix="tel:">
                        <div class="list-item-text"><fmt:message key="Call"/></div>
                        <div class="list-group-item-icon"><div class="fa fa-phone"></div></div>
                    </a>
                    <a class="list-group-item private-data" data-fake="${Player.obfuscatedPhone}" data-prefix="sms:">
                        <div class="list-item-text"><fmt:message key="SendSMS"/></div>
                        <div class="list-group-item-icon"><div class="fa fa-envelope"></div></div>
                    </a>
                    <a class="list-group-item private-data" data-fake="${Player.obfuscatedEmail}" data-prefix="mailto:">
                        <div class="list-item-text"><fmt:message key="SendMail"/></div>
                        <div class="list-group-item-icon"><div class="fa fa-at"></div></div>
                    </a>
                    <a href="/players/player/${Player.UUID}/vcard.vcf" class="list-group-item no-ajaxify" download>
                        <div class="list-item-text"><fmt:message key="AddToContacts"/></div>
                        <div class="list-group-item-icon"><div class="fa fa-phone-square"></div></div>
                    </a>  
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>