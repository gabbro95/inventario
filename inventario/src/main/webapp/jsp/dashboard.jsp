<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard - Inventario</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h2>Ciao ${sessionScope.email}!</h2>
    <h4>I tuoi contenitori:</h4>

    <c:if test="${not empty contenitori}">
        <div class="list-group">
            <c:forEach var="contenitore" items="${contenitori}">
                <a href="oggetti?contenitoreId=${contenitore.id}" class="list-group-item list-group-item-action">
                    ${contenitore.nome}
                </a>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${empty contenitori}">
        <p>Nessun contenitore trovato.</p>
    </c:if>

    <form action="nuovo-contenitore" method="post" class="mt-3">
        <div class="mb-3">
            <label for="nome" class="form-label">Nuovo Contenitore</label>
            <input type="text" class="form-control" name="nome" id="nome" required>
        </div>
        <button type="submit" class="btn btn-primary">Aggiungi</button>
    </form>

    <a href="${pageContext.request.contextPath}/logout">Logout</a>

</div>
</body>
</html>
