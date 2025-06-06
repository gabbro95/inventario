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
    <title>Lista della Spesa</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <jsp:include page="_navbar.jsp" /> 

    <div class="container mt-4">
        <h2>Lista della Spesa</h2>
        <p class="text-muted">
            Oggetti con quantità bassa nel tuo inventario. Seleziona quelli che hai comprato e indica la quantità da aggiungere.
        </p>
        
        <c:if test="${not empty param.errore}"><div class="alert alert-danger"><c:out value="${param.errore}" /></div></c:if>
        <c:if test="${not empty param.successo}"><div class="alert alert-success"><c:out value="${param.successo}" /></div></c:if>

        <c:choose>
            <c:when test="${not empty carrelloItems}">
                <form action="<c:url value='/carrello' />" method="post">
                    <div class="card">
                        <ul class="list-group list-group-flush">
                            <c:forEach var="item" items="${carrelloItems}">
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <div class="form-check flex-grow-1">
                                        <input class="form-check-input" type="checkbox" name="oggettoId" value="${item.oggettoId}" 
                                               id="check-${item.oggettoId}" onchange="toggleQuantita(this)">
                                        <label class="form-check-label" for="check-${item.oggettoId}">
                                            <strong><c:out value="${item.oggettoNome}" /></strong>
                                            <span class="badge bg-secondary ms-2">In inventario: ${item.quantitaAttuale}</span>
                                        </label>
                                    </div>
                                    <div class="ms-3">
                                        <div class="input-group">
                                            <span class="input-group-text">Quantità comprata:</span>
                                            <input type="number" name="quantita_${item.oggettoId}" class="form-control" 
                                                   id="quantita-${item.oggettoId}" value="1" min="1" style="width: 80px;" disabled>
                                        </div>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                        <div class="card-footer text-end">
                            <button type="submit" class="btn btn-primary">Ho fatto la spesa! Aggiorna selezionati</button>
                        </div>
                    </div>
                </form>
            </c:when>
            <c:otherwise>
                <div class="alert alert-success">
                    Complimenti! Nessun oggetto del tuo inventario è sotto la soglia. La tua dispensa è piena!
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <script>
        function toggleQuantita(checkbox) {
            const oggettoId = checkbox.value;
            const quantitaInput = document.getElementById('quantita-' + oggettoId);
            if (quantitaInput) {
                quantitaInput.disabled = !checkbox.checked;
            }
        }
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>