<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.gabbro95.inventario.model.Utente" %>
<%
    Utente utente = (Utente) session.getAttribute("utente");
    if (utente == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Profilo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <div class="card p-4 shadow-sm">
        <h2 class="mb-4">Profilo Utente</h2>
        <p><strong>Email:</strong> ${utente.email}</p>
        <a href="${pageContext.request.contextPath}/jsp/dashboard.jsp" class="btn btn-primary mt-3">Vai alla Dashboard</a>
    </div>
</div>
</body>
</html>
