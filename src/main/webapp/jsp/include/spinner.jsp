<!--<c:if test="${empty clientIdentifier}">
    <div id="spinner" style="display: none;">
        <div class="bubblingG">
            <span id="bubblingG_1">
            </span>
            <span id="bubblingG_2">
            </span>
            <span id="bubblingG_3">
            </span>
        </div>
    </div>
</c:if>-->
<c:if test="${empty clientIdentifier}">
<div id="ballWrapper">
    <div id="ball"></div>
    <div id="ballShadow"></div>			
</div>	
</c:if>