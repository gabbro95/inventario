<%-- File: /webapp/jsp/profilo.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${empty sessionScope.utente}"><c:redirect url="/index.jsp"/></c:if>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Il Mio Profilo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="_navbar.jsp" /> 

    <div class="container mt-4">
        <h2>Il Mio Profilo</h2>
        
        <c:if test="${not empty param.successo}"><div class="alert alert-success">Profilo aggiornato con successo!</div></c:if>
        <c:if test="${not empty param.errore}"><div class="alert alert-danger">Errore: <c:out value="${param.errore}"/></div></c:if>

        <div class="card">
            <div class="card-body">
                <form action="<c:url value='/profilo' />" method="post">
                    <div class="mb-3">
                        <label class="form-label">Email (non modificabile):</label>
                        <input type="email" class="form-control" value="<c:out value='${sessionScope.utente.email}'/>" disabled>
                    </div>
                    <div class="mb-3">
                        <label for="nome" class="form-label">Nome Visualizzato:</label>
                        <input type="text" class="form-control" id="nome" name="nome" value="<c:out value='${sessionScope.utente.nome}'/>">
                    </div>
                    <div class="mb-3">
                        <label for="immagineProfilo" class="form-label">URL Immagine Profilo:</label>
                        <input type="text" class="form-control" id="immagineProfilo" name="immagineProfilo" value="<c:out value='${sessionScope.utente.immagineProfilo}'/>">
                    </div>
                    <button type="submit" class="btn btn-primary">Salva Modifiche</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>