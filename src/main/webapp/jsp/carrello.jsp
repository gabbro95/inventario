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
    <link href="<c:url value='/css/style.css'/>" rel="stylesheet"> <%-- Assicurati che il percorso sia corretto --%>
</head>
<body>
    <jsp:include page="_navbar.jsp" />

    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2>Lista della Spesa</h2>
            <div>
                <a href="<c:url value='/dashboard' />" class="btn btn-outline-secondary">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left-circle me-1" viewBox="0 0 16 16"><path fill-rule="evenodd" d="M1 8a7 7 0 1 0 14 0A7 7 0 0 0 1 8m15 0A8 8 0 1 1 0 8a8 8 0 0 1 16 0m-4.5-.5a.5.5 0 0 1 0 1H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5z"/></svg>
                    Torna alla Dashboard
                </a>
            </div>
        </div>

        <p class="text-muted">
            Oggetti con quantità bassa nel tuo inventario. Seleziona quelli che hai comprato e indica la quantità da aggiungere.
        </p>

        <c:if test="${not empty param.errore}"><div class="alert alert-danger"><c:out value="${param.errore}" /></div></c:if>
        <c:if test="${not empty param.successo}"><div class="alert alert-success"><c:out value="${param.successo}" /></div></c:if>

        <%-- Form per il filtro per contenitore --%>
        <div class="mb-3">
            <form action="carrello" method="get" class="row g-3 align-items-center">
                <div class="col-md-auto">
                    <label for="filterContenitore" class="form-label mb-0">Filtra per Contenitore:</label>
                </div>
                <div class="col-md-4">
                    <select class="form-select" id="filterContenitore" name="filterContenitoreId" onchange="this.form.submit()">
                        <option value="">Tutti i Contenitori</option>
                        <c:forEach var="contenitore" items="${listaContenitori}">
                            <option value="${contenitore.id}" ${selectedContenitoreId == contenitore.id ? 'selected' : ''}>
                                <c:out value="${contenitore.nome}" />
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <c:if test="${not empty selectedContenitoreId && selectedContenitoreId != ''}">
                    <div class="col-md-auto">
                        <a href="<c:url value='/carrello' />" class="btn btn-outline-secondary">Reset Filtro</a>
                    </div>
                </c:if>
            </form>
        </div>


        <c:choose>
            <c:when test="${not empty carrelloItems}">
                <form action="<c:url value='/carrello' />" method="post">
                    <%-- Mantiene il filtro attivo anche dopo il POST --%>
                    <c:if test="${not empty selectedContenitoreId && selectedContenitoreId != ''}">
                        <input type="hidden" name="filterContenitoreId" value="${selectedContenitoreId}">
                    </c:if>

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
                                            <small class="text-muted ms-2">(Contenitore: <c:out value="${item.contenitoreNome}" />)</small> <%-- Mostra il nome del contenitore --%>
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