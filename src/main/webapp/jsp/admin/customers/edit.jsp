<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Customer"/></h4>
            </div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <spf:input type="hidden" path="id"/>
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>
                    
                    <div class="relative">
                        <spf:input path="name" type="text" class="form-control form-top-element"/>
                        <div class="explanation">
                            <fmt:message key="Name"/>
                        </div>
                    </div>
                    <div class="relative">
                        <spf:input path="domainNames" type="text" class="form-control form-center-element"/>
                        <div class="explanation">
                            <fmt:message key="DomainNames"/>
                        </div>
                    </div>
                    <div class="relative">
                        <spf:input path="defaultEmail" type="text" class="form-control form-center-element"/>
                        <div class="explanation">
                            <fmt:message key="DefaultEmail"/>
                        </div>
                    </div>
                    <div class="relative">
                        <spf:input path="googleAnalyticsTrackingId" type="text" class="form-control form-center-element" placeholder="GA-H8J7HH9"/>
                        <div class="explanation">
                            <fmt:message key="GoogleAnalyticsTrackingId"/>
                        </div>
                    </div>
                    <div class="relative">
                        <spf:input path="googleTagManagerId" type="text" class="form-control form-center-element" placeholder="GTM-M3Q4N2"/>
                        <div class="explanation">
                            <fmt:message key="GoogleTagManagerId"/>
                        </div>
                    </div>
                    <div class="relative">
                        <spf:input path="footerPrefix" type="text" class="form-control form-center-element"/>
                        <div class="explanation">
                            <fmt:message key="FooterPrefix"/>
                        </div>
                    </div>
                    <div class="relative">
                        <spf:input path="footerSuffix" type="text" class="form-control form-bottom-element"/>
                        <div class="explanation">
                            <fmt:message key="FooterSuffix"/>
                        </div>
                    </div>

                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
