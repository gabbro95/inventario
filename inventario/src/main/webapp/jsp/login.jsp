<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <div class="card shadow p-4 mx-auto" style="max-width: 500px;">
        <h3 class="text-center mb-4">Accedi a Inventario</h3>

        <div class="d-grid">
            <a href="${pageContext.request.contextPath}/oauth2/authorization/google" class="btn btn-outline-primary btn-lg">
                <img src="https://developers.google.com/identity/images/g-logo.png" alt="Google logo" style="width:20px; margin-right:10px;">
                Login con Google
            </a>
        </div>

        <c:if test="${not empty errore}">
            <div class="alert alert-danger mt-3 text-center">${errore}</div>
        </c:if>
    </div>
</div>
</body>
</html>
