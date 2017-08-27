<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<div class="row pageentry">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2 relative">
        <div class="blog-entry bg-white-translucent">
            <div class="blog-header">
                <div class="table-full-width">
                    <div class="table-row">
                        <div class="col-xs-6 col-md-6 col-md-height blog-title">
                            <a href="${Module.url}/${PageEntry.url}">${PageEntry.title}</a>
                        </div>
                        <div class="col-xs-6 col-md-6 col-md-height col-md-top text-right">
                            <a href="/players/player/${PageEntry.author.UUID}">
                            <c:choose>
                                <c:when test="${empty PageEntry.author.profileImage}">
                                    <span class="fa-stack fa-lg blog-author">
                                        <i class="fa fa-circle fa-stack-2x fa-inverse"></i>
                                        <i class="fa fa-user-circle fa-stack-1x"></i>
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <img src="/images/image/${PageEntry.author.profileImage.sha256}" class="blog-author"/>
                                </c:otherwise>
                            </c:choose>
                            </a>
                            <div><a href="/players/player/${PageEntry.author.UUID}">${PageEntry.author}</a></div>
                            <div>${PageEntry.lastModified}</div>    
                        </div>
                    </div>
                </div>
            </div>
            <div class="divider"></div>
            <div class="blog-body">
                <c:if test="${fn:contains(sessionScope.privileges,'ManageGeneral')}">
                    <%-- do not ajaxify edit link because tinymce breaks --%>
                    <a class="no-ajaxify edit-page" href="/admin/general/modules/blog/${Module.id}/edit/${PageEntry.id}"><i class="fa fa-edit"></i></a>
                </c:if>
                ${PageEntry.message}
            </div>
        </div>
    </div>
</div>