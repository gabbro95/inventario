<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Nuova Chiave</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <div class="card shadow p-4 mx-auto" style="max-width: 600px;">
        <h3 class="mb-4 text-center">Aggiungi una chiave personalizzata</h3>

        <form action="${pageContext.request.contextPath}/chiave" method="post">
            <input type="hidden" name="oggettoId" value="${param.oggettoId}" />

            <div class="mb-3">
                <label for="chiave" class="form-label">Nome chiave</label>
                <input type="text" class="form-control" id="chiave" name="chiave" required>
            </div>

            <div class="mb-3">
                <label for="valore" class="form-label">Valore</label>
                <input type="text" class="form-control" id="valore" name="valore" required>
            </div>

            <div class="d-grid">
                <button type="submit" class="btn btn-primary">Salva</button>
            </div>
        </form>

        <c:if test="${not empty errore}">
            <div class="alert alert-danger mt-3">${errore}</div>
        </c:if>
    </div>
</div>
</body>
</html>
