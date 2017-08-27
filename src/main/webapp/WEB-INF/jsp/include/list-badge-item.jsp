<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<a href="${param.url}" class="list-group-item">
    <div class="list-item-text">
        <div class="text">${param.msg}</div>
        <div class="badge-container">
            <span class="badge">${param.badge}</span>
        </div>
    </div>
</a>