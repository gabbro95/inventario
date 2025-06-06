<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${empty sessionScope.utente}">
    <c:redirect url="${pageContext.request.contextPath}/index.jsp?errore=sessione_scaduta"/>
</c:if>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dashboard - Inventario</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<c:url value='/css/style.css' />" rel="stylesheet">
</head>
<body>
    <%-- Assicurati che _navbar.jsp si trovi in /webapp/jsp/_navbar.jsp --%>
    <jsp:include page="/jsp/_navbar.jsp" />

    <div class="container page-container">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3 class="dashboard-welcome">Bentornato, <c:out value="${not empty sessionScope.utente.nome ? sessionScope.utente.nome : sessionScope.utente.email}" />!</h3>
        </div>
        
        <c:if test="${not empty param.errore}">
            <div class="alert alert-danger" role="alert"><c:out value="${param.errore}" /></div>
        </c:if>
        <c:if test="${not empty param.successo}">
            <div class="alert alert-success" role="alert">Operazione completata! (<c:out value="${param.successo}" />)</div>
        </c:if>

        <div class="row">
            <div class="col-lg-8">
                <h4>I tuoi Contenitori</h4>
                <c:choose>
                    <c:when test="${not empty contenitori}">
                        <div class="list-group shadow-sm">
                            <c:forEach var="contenitore" items="${contenitori}">
                                <a href="<c:url value='/oggetti?contenitoreId=${contenitore.id}' />" 
                                   class="list-group-item list-group-item-action d-flex justify-content-between align-items-center contenitore-list-item">
                                    <span>
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-archive-fill me-2" viewBox="0 0 16 16"><path d="M12.643 1.5c.37.37.544 1.01.544 1.693V4h1.007a.5.5 0 0 1 .5.5v.5h-2.015a.5.5 0 0 1-.5-.5V4H10v1.5a.5.5 0 0 1-.5.5H3.5a.5.5 0 0 1-.5-.5V4H1.857V3.193c0-.683.174-1.323.544-1.693L3.89 0l.504.126L5 0h6l.606.126.504-.126zM2.55 5.5A.5.5 0 0 1 3 5h10a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5H3a.5.5 0 0 1-.5-.5zM0 8a.5.5 0 0 1 .5-.5h15a.5.5 0 0 1 .5.5v5a.5.5 0 0 1-.5.5H.5a.5.5 0 0 1-.5-.5z"/></svg>
                                        <c:out value="${contenitore.nome}" />
                                    </span>
                                    <span class="badge bg-primary rounded-pill">Apri</span>
                                </a>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-info">
                            Non hai ancora nessun contenitore. Creane uno per iniziare!
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="col-lg-4 mt-4 mt-lg-0">
                <div class="card shadow-sm">
                    <div class="card-header bg-success text-white">
                        <h5 class="mb-0">Nuovo Contenitore</h5>
                    </div>
                    <div class="card-body">
                        <form action="<c:url value='/contenitore' />" method="post">
                            <div class="mb-3">
                                <label for="nomeContenitore" class="form-label">Nome del Contenitore:</label>
                                <input type="text" class="form-control" id="nomeContenitore" name="nome" required placeholder="Es. Cucina, Garage, Ufficio">
                            </div>
                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary">Crea Contenitore</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
