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
    <title>Nuovo Dettaglio per <c:out value="${oggetto.nome}"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-lg-6">
                <div class="card">
                    <div class="card-header bg-info text-white">
                        <h4 class="mb-0">Aggiungi Dettaglio a: <c:out value="${oggetto.nome}"/></h4>
                    </div>
                    <div class="card-body">
                        <form action="<c:url value='/nuova-chiave-valore' />" method="post">
                            
                            <%-- Campi nascosti per passare gli ID necessari --%>
                            <input type="hidden" name="oggettoId" value="${oggetto.id}">
                            <input type="hidden" name="contenitoreId" value="${contenitoreId}">

                            <div class="mb-3">
                                <label for="chiave" class="form-label">Nome Dettaglio (Chiave):</label>
                                <input type="text" class="form-control" id="chiave" name="chiave" required placeholder="Es. Scadenza, Colore, Marca...">
                            </div>
                            <div class="mb-3">
                                <label for="valore" class="form-label">Valore Dettaglio:</label>
                                <input type="text" class="form-control" id="valore" name="valore" required placeholder="Es. 12/12/2025, Rosso, DeCecco...">
                            </div>
                            <div class="text-end">
                                <a href="<c:url value='/oggetti?contenitoreId=${contenitoreId}' />" class="btn btn-secondary">Annulla</a>
                                <button type="submit" class="btn btn-success">Salva Dettaglio</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>