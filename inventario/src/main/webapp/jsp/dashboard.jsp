<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard - Inventario</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">Inventario</a>
        <div class="d-flex align-items-center">
            <c:if test="${not empty sessionScope.immagineProfilo}">
                <img src="${sessionScope.immagineProfilo}" alt="Profilo" class="rounded-circle me-2" style="width: 40px; height: 40px;">
            </c:if>
            <span class="me-3">
                <c:choose>
                    <c:when test="${not empty sessionScope.nome}">
                        ${sessionScope.nome}
                    </c:when>
                    <c:otherwise>
                        ${sessionScope.email}
                    </c:otherwise>
                </c:choose>
            </span>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm">Logout</a>
        </div>
    </div>
</nav>

<div class="container mt-4">
    <h5>I tuoi contenitori:</h5>

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

    <form action="${pageContext.request.contextPath}/contenitore" method="post" class="mt-3">
        <input type="text" name="nome" placeholder="Nome contenitore" required class="form-control my-2">
        <button type="submit" class="btn btn-primary">Aggiungi</button>
    </form>
</div>

</body>
</html>
