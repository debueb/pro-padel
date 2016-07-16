<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>
        <div class="page-header"></div>

        <div class="panel panel-info unit">
            <div class="panel-heading">
                <h4><fmt:message key="MasterData"/></h4></div>
            <div class="panel-body">
                <spf:form method="POST" modelAttribute="Model" enctype="multipart/form-data">
                    <spf:input type="hidden" path="id"/>
                    <div class="alert alert-danger" role="alert"><spf:errors path="*"/></div>
                    
                    <div class="relative">
                        <spf:input path="companyName" type="text" class="form-control form-top-element"/>
                        <div class="explanation"><fmt:message key="CompanyName"/></div>
                    </div>

                    <div class="relative">
                        <spf:input path="street" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="Street"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="zip" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="Zip"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="city" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="City"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="iban" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="IBAN"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="bic" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="BIC"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="institute" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="Institute"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="companyCity" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="CompanyCity"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="hrb" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="HRB"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="taxNumber" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="TaxNumber"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="tradeId" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="TradeId"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="ceo" type="text" class="form-control form-center-element"/>
                        <div class="explanation"><fmt:message key="CEO"/></div>
                    </div>
                    
                    <div class="relative">
                        <spf:input path="email" type="text" class="form-control form-bottom-element"/>
                        <div class="explanation"><fmt:message key="EmailAddress"/></div>
                    </div>
                    
                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                </spf:form>
            </div>  
        </div>
    </div>
</div>
<jsp:include page="/jsp/include/footer.jsp"/>
