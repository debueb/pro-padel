<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/include/head.jsp"/>

<div class="row">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
        <jsp:include page="/jsp/include/back.jsp"/>

        <div class="page-header"></div>
        
        <ol class="unit-2 breadcrumb">
            <li><a href="/admin"><fmt:message key="Administration"/></a></li>
            <li><a href="/admin/general"><fmt:message key="General"/></a></li>
            <li class="active"><fmt:message key="Design"/></li>
        </ol>

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
                                        <input type="text" name="${Attribute.name}" class="form-control" value="${empty Attribute.cssValue ? Attribute.cssDefaultValue : Attribute.cssValue}" />
                                        <span class="explanation"><fmt:message key="${Attribute.name}"/></span>
                                    </span>
                                </div>
                            </c:when>
                            <c:when test="${Attribute.name eq 'backgroundRepeat'}">
                                <div class="text-center unit">
                                    <input type="checkbox" name="backgroundImageRepeat" id="backgroundImageRepeat" ${Attribute.cssValue eq 'repeat' ? 'checked' : ''}/>
                                    <label for="backgroundImageRepeat"><fmt:message key="BackgroundImageRepeat"/></label>
                                </div>
                            </c:when>
                            <c:when test="${Attribute.name eq 'backgroundSize'}">
                                <div class="text-center unit">
                                    <input type="checkbox" name="backgroundSizeCover" id="backgroundSizeCover" ${Attribute.cssValue eq 'cover' ? 'checked' : ''}/>
                                    <label for="backgroundSizeCover"><fmt:message key="BackgroundSizeCover"/></label>
                                </div>
                            </c:when>
                            <c:when test="${Attribute.name eq 'loaderOpacity'}">
                                <hr/>
                                <div class="text-center unit">
                                    <input type="checkbox" name="loaderOpacity" id="loaderOpacity" ${Attribute.cssValue eq '@loaderOpacity: 1' ? 'checked' : ''}/>
                                    <label for="loaderOpacity"><fmt:message key="ShowLoadingAnimation"/></label>
                                </div>   
                            </c:when>
                            <c:when test="${Attribute.name eq 'customCss'}">
                                <hr/>
                                <div class="unit">
                                    <div class="text-center"><fmt:message key="CustomCss"/></div>
                                    <!--new line break required by html spec :(-->
                                    <textarea name="customCss" class="unit" style="width: 100%; height: 300px;">
${Attribute.cssValue}</textarea>
                                </div>   
                            </c:when>
                            <c:otherwise>
                                <hr/>
                                <figure class="picture unit" style="overflow: hidden;">
                                    <div class="text-center"><fmt:message key="BackgroundImage"/>
                                    <div class="text-center"><fmt:message key="BackgroundImageDesc"/>
                                    <div class="picture-subtext text-center"><fmt:message key="ClickImageToChange"/></div>
                                    <div class="background-picture unit"></div>
                                    <style type="text/css">
                                        /* :after is required for FF */
                                        .background-picture, .background-picture:after{
                                            width: 100%;
                                            height: 100%;
                                            background-size: 100% 100%;
                                            content: ${empty Attribute.cssValue ? 'url(\'/images/bg.jpg\')' : Attribute.cssValue};
                                        }
                                    </style>
                                </figure>
                                <input type="file" capture="camera" accept="image/*" name="backgroundImage" class="picture-input hidden"/>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    
                    <hr/>
                    <figure class="picture unit">
                        <div class="text-center"><fmt:message key="TouchIcon"/>
                        <div class="text-center"><fmt:message key="TouchIconDesc"/></div>
                        <div class="picture-subtext text-center"><fmt:message key="ClickImageToChange"/></div>
                        <div class="unit" style="margin: 0 auto; width: 192px; height: 192px; background-size: 192px 192px; background-image: url('${sessionScope.customer.touchIconPath}');"></div>
                    </figure>
                    <input type="file" capture="camera" accept="image/*" name="touchIcon" class="picture-input hidden"/>
                    
                    <hr/>
                    <figure class="picture unit">
                        <div class="text-center"><fmt:message key="CompanyLogo"/></div>
                        <div class="text-center"><fmt:message key="CompanyLogoDesc"/></div>
                        <div class="picture-subtext text-center"><fmt:message key="ClickImageToChange"/></div>
                        <div class="unit" style="margin: 0 auto; width: auto; height: 44px; background-size: auto 44px; content: url('${sessionScope.customer.companyLogoPath}');"></div>
                    </figure>
                    <input type="file" capture="camera" accept="image/*" name="companyLogo" class="picture-input hidden"/>
                    
                    <button class="btn btn-primary btn-block btn-form-submit unit-2" type="submit"><fmt:message key="Save"/></button>
                </form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/jsp/admin/include/colorpicker.jsp"/>
<jsp:include page="/jsp/include/footer.jsp"/>