<%@include file="/jsp/include/include.jsp"%>
<%@include file="/jsp/include/include.jsp"%>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        <div class="panel panel-info">
            <div class="panel-heading">
                <fmt:message key="NewAccount" var="NewAccount"/>
            </div>
            <div class="panel-body">
                <spf:form method="POST" class="form-signin" role="form" modelAttribute="Model">
                    <div class="alert alert-danger"><spf:errors path="*" cssClass="error"/></div>
                    <%@include file="/jsp/include/include.jsp"%>
                    <fmt:message key="ProjectName" var="ProjectName"/>
                    <fmt:message key="FirstName" var="FirstName"/>
                    <fmt:message key="LastName" var="LastName"/>
                    <fmt:message key="EmailAddress" var="EmailAddress"/>
                    <fmt:message key="PhoneNumber" var="PhoneNumber"/>
                    <fmt:message key="Password" var="Password"/>
                    <div class="relative">
                        <spf:input path="customer.name" type="text" class="form-control form-top-element"/>
                        <div class="explanation">${ProjectName}</div>
                    </div>
                    <div class="relative">
                        <spf:input path="player.firstName" type="text" class="form-control form-top-element"/>
                        <div class="explanation">${FirstName}</div>
                    </div>
                    <div class="relative">
                        <spf:input path="player.lastName" type="text" class="form-control form-center-element"/>
                        <div class="explanation">${LastName}</div>
                    </div>
                    <div class="relative">
                        <spf:input path="player.email" type="email"  class="form-control form-center-element"/>
                        <div class="explanation">${EmailAddress}</div>
                    </div>
                    <div class="relative">
                        <spf:input path="player.phone" type="tel"  class="form-control form-center-element"/>
                        <div class="explanation">${PhoneNumber}</div>
                    </div>
                    <span class="relative block">
                        <spf:select path="player.gender" class="select-simple form-control" data-style="${not empty param.showPassword ? 'form-center-element' : 'form-bottom-element'}" data-container="body">
                            <spf:option value="male"><fmt:message key="male"/></spf:option>
                            <spf:option value="female"><fmt:message key="female"/></spf:option>
                        </spf:select>
                        <span class="explanation-select"><fmt:message key="Gender"/></span>
                    </span>
                    <div class="relative">
                        <spf:input path="player.password" type="password"  class="form-control form-bottom-element"/>
                        <div class="explanation">${Password}</div>
                    </div>
                    
                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Create"/></button>
                </spf:form>
            </div>
        </div>
    </div>
</div>