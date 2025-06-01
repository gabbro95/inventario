<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Nuova Chiave-Valore</title>
    <link href="<c:url value='/css/bootstrap.min.css' />" rel="stylesheet">
</head>
<body class="container mt-5">

<h2>Nuova Chiave-Valore</h2>

<form method="post" action="<c:url value='/chiave-valore' />">
    <div class="mb-3">
        <label for="chiave" class="form-label">Chiave</label>
        <input type="text" class="form-control" id="chiave" name="chiave" required>
    </div>

    <div class="mb-3">
        <label for="valore" class="form-label">Valore</label>
        <input type="text" class="form-control" id="valore" name="valore" required>
    </div>

    <input type="hidden" name="oggettoId" value="${param.oggettoId}" />

    <button type="submit" class="btn btn-primary">Aggiungi</button>
    <a href="<c:url value='/oggetti?contenitoreId=${param.contenitoreId}' />" class="btn btn-secondary">Annulla</a>
</form>

</body>
</html>
