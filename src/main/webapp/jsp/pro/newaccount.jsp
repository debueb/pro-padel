<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/pro/include/head.jsp"/>
    <div class="row">
        <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
            <spf:form method="POST" class="form-horizontal" modelAttribute="Model">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4><fmt:message key="Register"/></h4>
                    </div>

                    <div class="panel-body">
                        <div class="alert alert-info"><fmt:message key="RegisterAsOperatorInfo"/></div>
                        <div class="alert alert-danger"><spf:errors path="*" cssClass="error"/></div>


                        <fmt:message key="EmailAddress" var="EmailAddress"/>
                        <fmt:message key="PhoneNumber" var="PhoneNumber"/>
                        <fmt:message key="Password" var="Password"/>
                        <div class="form-group unit-2">
                            <label for="inputEmail3" class="col-sm-2 control-label"><fmt:message key="ProjectName"/></label>
                            <div class="col-sm-10">
                                <spf:input path="customer.name" type="text" class="form-control"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="inputEmail3" class="col-sm-2 control-label"><fmt:message key="FirstName"/></label>
                            <div class="col-sm-10">
                                <spf:input path="player.firstName" type="text" class="form-control"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="inputEmail3" class="col-sm-2 control-label"><fmt:message key="LastName"/></label>
                            <div class="col-sm-10">
                                <spf:input path="player.lastName" type="text" class="form-control"/>

                            </div>
                        </div>
                        <div class="form-group">
                            <label for="inputEmail3" class="col-sm-2 control-label"><fmt:message key="EmailAddress"/></label>
                            <div class="col-sm-10">
                                <spf:input path="player.email" type="text" class="form-control"/>

                            </div>
                        </div>

                        <div class="form-group">
                            <label for="inputEmail3" class="col-sm-2 control-label"><fmt:message key="PhoneNumber"/></label>
                            <div class="col-sm-10">
                                <spf:input path="player.phone" type="text" class="form-control"/>

                            </div>
                        </div>
                        <div class="form-group">
                            <label for="inputEmail3" class="col-sm-2 control-label"><fmt:message key="Gender"/></label>
                            <div class="col-sm-10">
                                <spf:select path="player.gender" class="select-simple form-control" data-style="${not empty param.showPassword ? 'form-center-element' : 'form-bottom-element'}" data-container="body">
                                    <spf:option value="male"><fmt:message key="male"/></spf:option>
                                    <spf:option value="female"><fmt:message key="female"/></spf:option>
                                </spf:select>

                            </div>
                        </div>
                        <div class="form-group">
                            <label for="inputEmail3" class="col-sm-2 control-label"><fmt:message key="Password"/></label>
                            <div class="col-sm-10">
                                <spf:input path="player.password" type="password" class="form-control"/>

                            </div>
                        </div>
                    </div>
                    <div class="panel-footer"><button class="btn btn-default btn-block btn-form-submit" type="submit"><fmt:message key="Register"/></button></div>
                </div>
            </spf:form>
        </div>
    </div>
<jsp:include page="/jsp/pro/include/footer.jsp"/>