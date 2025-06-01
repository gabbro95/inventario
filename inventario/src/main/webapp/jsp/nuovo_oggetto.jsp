<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Nuovo Oggetto</title>
    <link href="<c:url value='/css/bootstrap.min.css' />" rel="stylesheet">
</head>
<body class="container mt-5">

<h2>Nuovo Oggetto</h2>

<form method="post" action="<c:url value='/oggetto' />">
    <div class="mb-3">
        <label for="nome" class="form-label">Nome oggetto</label>
        <input type="text" class="form-control" id="nome" name="nome" required>
    </div>

    <div class="mb-3">
        <label for="numero" class="form-label">Quantità</label>
        <input type="number" class="form-control" id="numero" name="numero" required>
    </div>

    <div class="mb-3">
        <label for="data" class="form-label">Data di inserimento</label>
        <input type="date" class="form-control" id="data" name="data" required>
    </div>

    <input type="hidden" name="contenitoreId" value="${param.contenitoreId}" />

    <button type="submit" class="btn btn-primary">Aggiungi</button>
    <a href="<c:url value='/dashboard' />" class="btn btn-secondary">Annulla</a>
</form>

</body>
</html>
