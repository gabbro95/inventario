<%-- File: /webapp/jsp/modifica_dettaglio.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${empty sessionScope.utente || empty dettaglio}"><c:redirect url="/dashboard"/></c:if>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Modifica Dettaglio</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2>Modifica Dettaglio</h2>
    <form action="<c:url value='/modifica-dettaglio' />" method="post">
        <input type="hidden" name="kvId" value="${dettaglio.id}">
        <input type="hidden" name="contenitoreId" value="${param.contenitoreId}">

        <div class="mb-3">
            <label for="chiave" class="form-label">Chiave:</label>
            <input type="text" class="form-control" id="chiave" name="chiave" value="<c:out value='${dettaglio.chiave}'/>" required>
        </div>
        <div class="mb-3">
            <label for="valore" class="form-label">Valore:</label>
            <input type="text" class="form-control" id="valore" name="valore" value="<c:out value='${dettaglio.valore}'/>" required>
        </div>
        
        <a href="<c:url value='/oggetti?contenitoreId=${param.contenitoreId}'/>" class="btn btn-secondary">Annulla</a>
        <button type="submit" class="btn btn-primary">Salva Modifiche</button>
    </form>
</div>
</body>
</html>