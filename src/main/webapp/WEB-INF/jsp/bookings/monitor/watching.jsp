<%@include file="/WEB-INF/jsp/include/include.jsp"%>
<joda:format value="${Booking.bookingTime}" pattern="HH:mm" var="time"/>
<div><fmt:message key="NotifyWhenAvailableActive"><fmt:param value="${Booking.offer}"/><fmt:param value="${time}"/></fmt:message></div>