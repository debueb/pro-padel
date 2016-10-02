<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/pro/include/head-simple.jsp"/>
<div class="wrapper">
    <div class="content" style="background-color: #263248">
        <p>
        <h3 class="text-center"><fmt:message key="ForOperators"/></h3>
        <br><br>
        <h4 class="text-center"><fmt:message key="ForOperatorsInfo"/></h4>
        </p>
    </div>
    <div class="parallax-window" data-parallax="scroll" data-image-src="/pro/images/8.jpg"></div>
    <div class="content content-teaser" style="background-color: #FF9800">
        <p>
        <h3 class="text-center"><fmt:message key="Features"/></h3>
        <br><br>
        <div class="flex-container">
            <div class="panel flex-item panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title text-center"><fmt:message key="TournamentManagement"/></h3>
                </div>
                <div class="panel-body">
                    <fmt:message key="TournamentManagementDesc"/>
                </div>
            </div>
            <div class="panel flex-item panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title text-center"><fmt:message key="LeagueManagement"/></h3>
                </div>
                <div class="panel-body">
                    <fmt:message key="LeagueManagementDesc"/>
                </div>
            </div>
            <div class="panel flex-item panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title text-center"><fmt:message key="Ranking"/></h3>
                </div>
                <div class="panel-body">
                    <fmt:message key="RankingInfo"/>
                </div>
            </div>
            <div class="panel flex-item panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title text-center"><fmt:message key="PlayersAndTeams"/></h3>
                </div>
                <div class="panel-body">
                    <fmt:message key="PlayersAndTeamsDesc"/>
                </div>
            </div>
            <div class="panel flex-item panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title text-center"><fmt:message key="ContentManagementSystem"/></h3>
                </div>
                <div class="panel-body">
                    <fmt:message key="ContentManagementSystemDesc"/>
                </div>
            </div>
            <div class="panel flex-item panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title text-center"><fmt:message key="AdaptiveDesign"/></h3>
                </div>
                <div class="panel-body">
                    <fmt:message key="AdaptiveDesignDesc"/>
                </div>
            </div>
            <div class="panel flex-item panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title text-center"><fmt:message key="MatchOffers"/></h3>
                </div>
                <div class="panel-body">
                    <fmt:message key="MatchOffersDesc"/>
                </div>
            </div>
            <div class="panel flex-item panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title text-center"><fmt:message key="BookingSystem"/></h3>
                </div>
                <div class="panel-body">
                    <fmt:message key="BookingSystemDesc"/>
                </div>
            </div>
        </div>
        </p>
    </div>
    <div class="parallax-window" data-parallax="scroll" data-image-src="/pro/images/4.jpg"></div>
    <div class="content content-teaser" style="background-color: #7E8AA2">
        <p>
        <h3 class="text-center"><fmt:message key="References"/></h3>
        <br><br>
        <div class="flex-container">
            <c:set var="req" value="${pageContext.request}" />
            <c:forEach var="Customer" items="${Customers}">
                <div class="panel flex-item panel-default" style="height: 100%; border-bottom-left-radius: 0; border-bottom-right-radius:0;">
                    <div class="panel-heading">
                        <c:forEach var="domainName" items="${Customer.domainNames}" end="0">
                            <h3 class="panel-title text-center"><a href="https://${domainName}" target="blank">${Customer}</a></h3>
                            </c:forEach>
                    </div>
                    <div class="panel-body no-padding" style="height: 100%;">
                        <c:forEach var="domainName" items="${Customer.domainNames}" end="0">
                            <iframe src="https://${domainName}" width="100%" height="568" style="height: 568px; max-height: 568px; display: block;" frameborder="0"></iframe>
                            </c:forEach>
                    </div>
                </div>
            </c:forEach>
        </div>
        </p>
    </div>
    <div class="parallax-window" data-parallax="scroll" data-image-src="/pro/images/7.jpg"></div>
    <div class="content" style="background-color: #FF9800">
        <p>
        <h3 class="text-center"><fmt:message key="Pricing"/></h3>
        <br><br>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title text-center"><fmt:message key="PricingTable"/></h3>
            </div>
            <div class="panel-body table-responsive" style="padding: 0">
                <table class="table table-striped table-condensed">
                    <thead>
                    <th></th>
                    <th class="text-center">Basic</th>
                    <th class="text-center">Premium</th>
                    <th class="text-center">Pro</th>
                    </thead>
                    <tbody>
                        <tr>
                            <td><fmt:message key="TournamentManagement"/></td>
                            <td class="text-center check"></td>
                            <td class="text-center check"></td>
                            <td class="text-center check"></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="LeagueManagement"/></td>
                            <td class="text-center check"></td>
                            <td class="text-center check"></td>
                            <td class="text-center check"></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="Ranking"/></td>
                            <td class="text-center check"></td>
                            <td class="text-center check"></td>
                            <td class="text-center check"></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="PlayersAndTeams"/></td>
                            <td class="text-center check"></td>
                            <td class="text-center check"></td>
                            <td class="text-center check"></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="ContentManagementSystem"/></td>
                            <td class="text-center check"></td>
                            <td class="text-center check"></td>
                            <td class="text-center check"></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="MatchOffers"/></td>
                            <td class="text-center cross"></td>
                            <td class="text-center check"></td>
                            <td class="text-center check"></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="BookingSystem"/></td>
                            <td class="text-center cross"></td>
                            <td class="text-center check"></td>
                            <td class="text-center check"></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="WebApp"/></td>
                            <td class="text-center check"></td>
                            <td class="text-center check"></td>
                            <td class="text-center check"></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="iOSApp"/></td>
                            <td class="text-center cross"></td>
                            <td class="text-center cross"></td>
                            <td class="text-center check"></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="AndroidApp"/></td>
                            <td class="text-center cross"></td>
                            <td class="text-center cross"></td>
                            <td class="text-center check"></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td class="text-center"><fmt:message key="EURmtl"><fmt:param value="0"/></fmt:message></td>
                            <td class="text-center"><fmt:message key="EURmtl"><fmt:param value="40"/></fmt:message></td>
                            <td class="text-center"><fmt:message key="EURmtl"><fmt:param value="70"/></fmt:message></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            </p>
        </div>

        <div class="parallax-window" data-parallax="scroll" data-image-src="/pro/images/5.jpg"></div>
        <div class="content" style="background-color: #7E8AA2">
            <p>
                <a href="/pro/operators/newaccount" class="white"><h3 class="text-center"><fmt:message key="Register"/></h3></a>
            <br><br>
            <a href="/pro/operators/newaccount" class="white"><h4 class="text-center"><fmt:message key="RegisterAsOperatorDesc"/></h4></a>
        </p>
    </div>


    <jsp:include page="/jsp/pro/include/footer-simple.jsp"/>