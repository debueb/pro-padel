<%@include file="/jsp/include/include.jsp"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
        <meta name="description" content="">
        <meta name="author" content="anon">

        <meta name="mobile-web-app-capable" content="yes"> <%-- Chrome M31 and up --%>
        <meta name="apple-mobile-web-app-capable" content="yes" /> <%-- Mobile Safari --%>
        <meta name="format-detection" content="telephone=no">
        
        <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
        <script src="/pro/js/bootstrap.min.js"></script>
        <script src="/pro/js/pro.js"></script>
        <link rel="stylesheet" href="/pro/css/bootstrap.css">
        <link rel="stylesheet" href="/pro/css/pro.css">
    </head>
    <body>
        <div class="navbar navbar-default navbar-fixed-top" role="navigation">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="/pro">${pageContext.request.serverName}</a>
                </div>
                <div class="navbar-collapse collapse">
                    <ul class="nav navbar-nav pull-right">
                        <li class="active"><a href="/pro/tournaments"><fmt:message key="Tournaments"/></a>
                        </li>
                        <li class="active"><a href="/pro/ranking"><fmt:message key="Ranking"/></a>
                        </li>
                        <li class="active"><a href="/pro/operators"><fmt:message key="ForOperators"/></a>
                        </li>
                        <li class="active"><a href="/pro/contact"><fmt:message key="Contact"/></a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>