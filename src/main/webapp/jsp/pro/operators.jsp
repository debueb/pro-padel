<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/pro/include/head.jsp"/>
<div class="container-fluid">
    <div class="row row-margin-bottom" style="background: #D9EDF7; padding-bottom: 10px;">
        <div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3 operator-info">
            <h2 class="text-center"><fmt:message key="ForOperators"/></h2>
            <div class="text-center"><fmt:message key="ForOperatorsInfo"/></div>
        </div>
    </div>
    <div class="row row-margin-bottom" style="background: #D9EDF7;">
        <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-10 col-lg-offset-1 col-xl-8 col-xl-offset-2 operator-info">
            <h2 class="text-center"><fmt:message key="Features"/></h2>
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
                <div class="panel flex-item panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title text-center"><fmt:message key="ContentManagementSystem"/></h3>
                    </div>
                    <div class="panel-body">
                        <fmt:message key="ContentManagementSystemDesc"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row row-margin-bottom" style="background: #D9EDF7;">
        <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-10 col-lg-offset-1 col-xl-8 col-xl-offset-2 operator-info">
            <div class="row">
                <h2 class="text-center"><fmt:message key="References"/></h2>
                <div class="flex-container">
                    <c:set var="req" value="${pageContext.request}" />
                    <c:forEach var="Customer" items="${Customers}">
                        <div class="panel flex-item panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title text-center">${Customer}</h3>
                            </div>
                            <div class="panel-body no-padding">
                                <c:forEach var="domainName" items="${Customer.domainNames}" end="0">
                                    <iframe src="https://${domainName}" width="100%" height="568px" frameborder="0"></iframe>
                                </c:forEach>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
    <div class="row row-margin-bottom" style="background: #D9EDF7;">
        <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-10 col-lg-offset-1 col-xl-8 col-xl-offset-2 operator-info">
            <h2 class="text-center"><fmt:message key="Pricing"/></h2>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title text-center"><fmt:message key="PricingTable"/></h3>
                </div>
                <div class="panel-body">
                    <table class="table table-striped">
                        <thead>
                        <th></th>
                        <th class="text-center">Basic</th>
                        <th class="text-center">Pro</th>
                        </thead>
                        <tbody>
                            <tr>
                                <td><fmt:message key="TournamentManagement"/></td>
                                <td class="text-center">&#10003;</td>
                                <td class="text-center">&#10003;</td>
                            </tr>
                            <tr>
                                <td><fmt:message key="LeagueManagement"/></td>
                                <td class="text-center">&#10003;</td>
                                <td class="text-center">&#10003;</td>
                            </tr>
                            <tr>
                                <td><fmt:message key="Ranking"/></td>
                                <td class="text-center">&#10003;</td>
                                <td class="text-center">&#10003;</td>
                            </tr>
                            <tr>
                                <td><fmt:message key="PlayersAndTeams"/></td>
                                <td class="text-center">&#10003;</td>
                                <td class="text-center">&#10003;</td>
                            </tr>
                            <tr>
                                <td><fmt:message key="MatchOffers"/></td>
                                <td class="text-center">&#x2717;</td>
                                <td class="text-center">&#10003;</td>
                            </tr>
                            <tr>
                                <td><fmt:message key="BookingSystem"/></td>
                                <td class="text-center">&#x2717;</td>
                                <td class="text-center">&#10003;</td>
                            </tr>
                            <tr>
                                <td><fmt:message key="ContentManagementSystem"/></td>
                                <td class="text-center">&#x2717;</td>
                                <td class="text-center">&#10003;</td>
                            </tr>
                            <tr>
                                <td></td>
                                <td class="text-center"><fmt:message key="EURmtl"><fmt:param value="0"/></fmt:message></td>
                                <td class="text-center"><fmt:message key="EURmtl"><fmt:param value="40"/></fmt:message></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <hr/>
        </div>
    </div>
    <div class="row" style="background: #D9EDF7;">
        <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-10 col-lg-offset-1 col-xl-8 col-xl-offset-2 operator-info">
            <h2 class="text-center"><fmt:message key="Register"/></h2>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title text-center"><fmt:message key="RegisterAsOperator"/></h3>
                </div>
                <div class="panel-body">
                    <fmt:message key="RegisterAsOperatorDesc"/>
                </div>
                <div class="panel-footer">
                    <a class="btn btn-default btn-block" href="/pro/operators/newaccount"><fmt:message key="Register"/></a>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/pro/include/footer.jsp"/>