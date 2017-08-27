<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<div class="col-flex flip-col">
    <div class="flip-container">
        <div class="flipper">
            <div class="front">
                <a href="${param.url}">
                    <span class="title">${param.title}</span>
                    <span class="icon fa fa-${param.iconName}"></span>
                </a>
            </div>
            <div class="back">
                <a href="${param.url}">
                    <span class="title">
                        <c:choose>
                            <c:when test="${empty param.desc}">
                                ${param.title}
                            </c:when>
                            <c:otherwise>
                                ${param.desc}
                            </c:otherwise>
                        </c:choose>
                    </span>
                </a>
            </div>
        </div>
    </div>
 </div>