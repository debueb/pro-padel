<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-6 col-sm-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info unit">
            <div class="panel-heading"><h4><fmt:message key="Register"/></h4></div>
            <div class="panel-body">
                <spf:form class="form-signin" modelAttribute="Model" action="/login/register">
                    <div class="alert alert-danger"><spf:errors path="*"/></div>
                    <spf:input path="id" type="hidden"/>
                    <jsp:include page="/jsp/include/player-input.jsp"><jsp:param name="showPassword" value="true"/></jsp:include>
                    <input type="checkbox" name="stay-logged-in" id="stay-logged-in" checked/><label class="checkbox" for="stay-logged-in"><fmt:message key="StayLoggedIn"/></label>
                    <button class="btn btn-primary btn-block" type="submit"><fmt:message key="Register"/></button>
                </spf:form>
            </div>
        </div>


    </div>
</div>

<jsp:include page="/jsp/include/footer.jsp"/>
