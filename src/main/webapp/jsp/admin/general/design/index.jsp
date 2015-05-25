<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-lg-6 col-lg-offset-3">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>

        <div class="panel panel-info">
            <div class="panel-heading">
                <h4><fmt:message key="Design"/></h4>
            </div>
        </div>




        <form method="POST" class="form-signin">
            <c:forEach var="Model" items="${Models}">
                <div class="input-group color-picker">

                    <span class="input-group-addon form-center-element"><i></i></span>
                    <span class="relative">
                        <input type="text" name="${Model.name}" class="form-control form-center-element" value="${empty Model.cssValue ? Model.cssDefaultValue : Model.cssValue}" />
                        <span class="explanation"><fmt:message key="${Model.name}"/></span>
                    </span>
                </div>
            </c:forEach>

            <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
        </form>
    </div>
</div>
<jsp:include page="/jsp/admin/include/colorpicker.jsp"/>
