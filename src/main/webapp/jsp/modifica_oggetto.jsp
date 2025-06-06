<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${empty sessionScope.utente || empty oggetto}">
    <c:redirect url="${pageContext.request.contextPath}/dashboard?errore=dati_mancanti"/>
</c:if>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Modifica: <c:out value="${oggetto.nome}"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2>Modifica Oggetto</h2>
    <%-- L'action del form punterà a un servlet "modifica-oggetto" --%>
    <form action="<c:url value='/modifica-oggetto' />" method="post">
        
        <%-- Campi nascosti per passare gli ID necessari --%>
        <input type="hidden" name="oggettoId" value="${oggetto.id}">
        <input type="hidden" name="contenitoreId" value="${oggetto.contenitoreId}">

        <div class="mb-3">
            <label for="nomeOggetto" class="form-label">Nome Oggetto:</label>
            <input type="text" class="form-control" id="nomeOggetto" name="nome" value="<c:out value='${oggetto.nome}'/>" required>
        </div>
        <div class="mb-3">
            <label for="quantita" class="form-label">Quantità Attuale:</label>
            <input type="number" class="form-control" id="quantita" name="numero" value="${oggetto.numero}" min="0" required>
        </div>
        <div class="mb-3">
            <label for="sogliaMinima" class="form-label">Soglia per Lista Spesa:</label>
            <input type="number" class="form-control" id="sogliaMinima" name="sogliaMinima" value="${oggetto.sogliaMinima}" min="0" required>
        </div>
        
        <a href="<c:url value='/oggetti?contenitoreId=${oggetto.contenitoreId}'/>" class="btn btn-secondary">Annulla</a>
        <button type="submit" class="btn btn-primary">Salva Modifiche</button>
    </form>
</div>
</body>
</html>