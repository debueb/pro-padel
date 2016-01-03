<%@include file="/jsp/include/include.jsp"%>
<div class="unit-2 border-leyenda">
    <div class="stretch col-leyenda-header"><fmt:message key="Leyenda"/></div>
    <div class="container-flex flex-wrap stretch">
        <c:forEach var="Offer" items="${Offers}">
            <span class="col-flex col-leyenda" style="background-color: ${Offer.hexColor}">${Offer}</span>
        </c:forEach>
    </div>
</div>