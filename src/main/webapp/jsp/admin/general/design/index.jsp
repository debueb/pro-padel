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
            <div class="panel-body">

                <form method="POST" class="form-signin" enctype="multipart/form-data">
                    <c:forEach var="Attribute" items="${Colors}">
                        <c:choose>
                            <c:when test="${fn:endsWith(Attribute.name, 'Color')}">
                                <div class="input-group color-picker unit">
                                    <span class="input-group-addon"><i></i></span>
                                    <span class="relative">
                                        <input type="text" name="${Attribute.id}" class="form-control" value="${empty Attribute.cssValue ? Attribute.cssDefaultValue : Attribute.cssValue}" />
                                        <span class="explanation"><fmt:message key="${Attribute.name}"/></span>
                                    </span>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <figure id="picture" class="unit">
                                    <div class="text-center"><fmt:message key="BackgroundImage"/></div>
                                    <div class="unit" style="width: 100%; height: 100%; background-size: 100% 100%; content: ${empty Attribute.cssValue ? Attribute.cssDefaultValue : Attribute.cssValue}"></div>
                                    <div id="picture-subtext" class="unit text-center"><fmt:message key="ClickImageToChange"/></div>
                                </figure>
                                <input type="file" capture="camera" accept="image/*" id="picture-input" name="backgroundImage" class="hidden"/>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <button class="btn btn-primary btn-block btn-form-submit unit" type="submit"><fmt:message key="Save"/></button>
                </form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/admin/include/colorpicker.jsp"/>
