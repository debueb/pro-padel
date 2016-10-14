<%@include file="/jsp/include/include.jsp"%>
<div class="stretch col-leyenda-header"><fmt:message key="Leyenda"/></div>
<div class="container-flex stretch">
    <c:forEach var="Offer" items="${SelectedOffers}">
        <span class="col-flex col-leyenda" style="background-color: ${Offer.hexColor}">${Offer.shortName} = ${Offer}</span>
    </c:forEach>
</div>