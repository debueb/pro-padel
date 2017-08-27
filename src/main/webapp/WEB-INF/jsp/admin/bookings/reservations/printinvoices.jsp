<%@ page pageEncoding="UTF-8" contentType="text/html" %><?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/WEB-INF/jsp/invoices/include/invoice-header.jsp"/>
<body>
    <c:forEach var="Booking" items="${Bookings}" varStatus="status">
        <c:set var="Booking" value="${Booking}" scope="request"/>
        <jsp:include page="/WEB-INF/jsp/invoices/include/invoice-single.jsp"/>
        <c:if test="${not status.last}">
            <div class="page-break"></div>
        </c:if>
    </c:forEach>
</body>
</html>