<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4>${Player}</h4>
            </div>
            <div class="panel-body">


                <div class="text-center">
                    <jsp:include page="/jsp/players/include/profile-image.jsp"/>
                </div>

                <div class="list-group unit">
                    <a href="/teams/player/${Player.UUID}" class="list-group-item ajaxify">
                        <div class="list-item-text"><fmt:message key="TeamsWith"><fmt:param>${Player}</fmt:param></fmt:message>
                                    <div class="list-group-item-icon"><div class="fa fa-lg fa-group"></div></div>
                                </div>
                            </a>
                    <c:if test="${sessionScope.accessLevel == 'loggedInAndParticipant'}">
                        <a class="list-group-item private-data" data-fake="${Player.obfuscatedPhone}" data-prefix="tel:">
                            <div class="list-item-text"><fmt:message key="Call"/>
                                <div class="list-group-item-icon"><div class="fa fa-lg fa-phone"></div></div>
                            </div>
                        </a>
                        <a class="list-group-item private-data" data-fake="${Player.obfuscatedPhone}" data-prefix="sms:">
                            <div class="list-item-text"><fmt:message key="SendSMS"/>
                                <div class="list-group-item-icon"><div class="fa fa-lg fa-envelope"></div></div>
                            </div>
                        </a>
                        <a class="list-group-item private-data" data-fake="${Player.obfuscatedEmail}" data-prefix="mailto:">
                            <div class="list-item-text"><fmt:message key="SendMail"/>
                                <div class="list-group-item-icon"><div class="fa fa-lg fa-at"></div></div>
                            </div>
                        </a>
                        <a href="/players/player/${Player.UUID}/vcard.vcf" class="list-group-item" download>
                            <div class="list-item-text"><fmt:message key="AddToContacts"/>
                                <div class="list-group-item-icon"><div class="fa fa-lg fa-phone-square"></div></div>
                            </div>
                        </a>     
                    </c:if>
                </div>

                <c:if test="${empty sessionScope.accessLevel}">
                    <div class="alert alert-info unit">
                        <fmt:message key="LogInToContact"><fmt:param value="${Player}"/></fmt:message>
                        </div>
                    <c:url value="/players/player/${Player.UUID}" var="playerURL"/>
                    <a class="btn btn-primary btn-block unit ajaxify" href="/login?redirect=${playerURL}"><fmt:message key="Login"/></a>
                    <a class="btn btn-primary btn-block ajaxify" href="/login/register?redirect=${playerURL}"><fmt:message key="Register"/></a>
                </c:if>

                <c:if test="${sessionScope.accessLevel == 'loggedIn'}">
                    <div class="alert alert-info unit">
                        <fmt:message key="NeedToParticipateToContact"><fmt:param value="${Player}"/></fmt:message>
                        </div>
                </c:if>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
