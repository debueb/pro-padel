<%@include file="/jsp/include/include.jsp"%>
<jsp:include page="/jsp/${path}include/head.jsp"/>
<c:if test="${not empty Module and not empty Module.description}">
    <div class="row">
        <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
            ${Module.description}
        </div>
    </div>
</c:if>
<div class="row">
        <div class="col-xs-12 col-sm-10 col-sm-offset-1 col-lg-8 col-lg-offset-2">
<div class="container-flex stretch">
    <c:forEach var="StaffMember" items="${Models}">
        <div class="col-flex staff-col">
            <div class="staff-container">
                <div class="staff-flipper">
                    <div class="staff-front staff-bg">
                        <img src="/images/image/${StaffMember.profileImage.sha256}">
                        <div class="text">
                            <div class="btn btn-primary btn-large" style="display: inline;">${StaffMember.name}</div>
                            <div class="unit-2">${StaffMember.teaser}</div>
                        </div>
                    </div>
                    <div class="staff-back staff-bg">
                        <div class="text">
                            <div class="btn btn-primary btn-large" style="display: inline;">${StaffMember.name}</div>
                            <div class="unit-2">${StaffMember.description}</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</div>
        </div>
</div>

<jsp:include page="/jsp/${path}include/footer.jsp"/>
