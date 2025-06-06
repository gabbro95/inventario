<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Nuovo Oggetto</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <div class="card shadow p-4 mx-auto" style="max-width: 600px;">
        <h3 class="mb-4 text-center">Aggiungi un nuovo oggetto</h3>

        <form action="${pageContext.request.contextPath}/oggetto" method="post">
            <input type="hidden" name="contenitoreId" value="${param.contenitoreId}" />

            <div class="mb-3">
                <label for="nome" class="form-label">Nome oggetto</label>
                <input type="text" class="form-control" id="nome" name="nome" required>
            </div>

            <div class="mb-3">
                <label for="numero" class="form-label">Quantità</label>
                <input type="number" class="form-control" id="numero" name="numero" required min="1">
            </div>

            <div class="mb-3">
                <label for="data" class="form-label">Data inserimento</label>
                <input type="date" class="form-control" id="data" name="data" required>
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
