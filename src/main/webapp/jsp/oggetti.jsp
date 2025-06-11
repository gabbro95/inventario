<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Oggetti in: <c:out value="${contenitore.nome}" /></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<c:url value='/css/style.css'/>" rel="stylesheet"> <%-- ASSICURATI CHE IL PERCORSO SIA CORRETTO --%>
    <style>
        /* Questo blocco style può essere rimosso se le classi sono tutte nel CSS esterno */
        /* Alcuni stili sono già stati spostati nel tuo style.css, se ne mancano qui, puoi toglierli */
        body { background-color: #f8f9fa; }
        .table th { background-color: #343a40; color: white; }
        .details-list { font-size: 0.9em; padding-left: 0; list-style-type: none;}
        .details-list li { margin-bottom: 0.25rem; }
        /* La classe highlight è ora nel tuo style.css esterno, puoi toglierla da qui */
        /* .highlight {
            background-color: #ADD8E6;
            font-weight: bold;
        } */
    </style>
</head>
<body>
    <jsp:include page="_navbar.jsp" />

    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h2>Oggetti nel contenitore: <span class="text-primary"><c:out value="${contenitore.nome}" /></span></h2>
            <div>
                <a href="<c:url value='/dashboard' />" class="btn btn-outline-secondary">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left-circle me-1" viewBox="0 0 16 16"><path fill-rule="evenodd" d="M1 8a7 7 0 1 0 14 0A7 7 0 0 0 1 8m15 0A8 8 0 1 1 0 8a8 8 0 0 1 16 0m-4.5-.5a.5.5 0 0 1 0 1H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5z"/></svg>
                    Torna alla Dashboard
                </a>
                <a href="<c:url value='/nuovo-oggetto?contenitoreId=${contenitore.id}' />" class="btn btn-success">
                    + Aggiungi Nuovo Oggetto
                </a>
            </div>
        </div>

        <%-- Form per l'ordinamento --%>
        <div class="mb-3">
            <form action="oggetti" method="get" class="row g-3 align-items-center">
                <input type="hidden" name="contenitoreId" value="${contenitore.id}">
                <div class="col-md-auto">
                    <label for="sortBy" class="form-label mb-0">Ordina per:</label>
                </div>
                <div class="col-md-3">
                    <select class="form-select" id="sortBy" name="sortBy">
                        <option value="nome" ${param.sortBy == 'nome' ? 'selected' : ''}>Nome Oggetto</option>
                        <option value="numero" ${param.sortBy == 'numero' ? 'selected' : ''}>Quantità</option>
                        <option value="dataInserimento" ${param.sortBy == 'dataInserimento' ? 'selected' : ''}>Data Inserimento</option>
                    </select>
                </div>
                <div class="col-md-auto">
                    <label for="sortOrder" class="form-label mb-0">Direzione:</label>
                </div>
                <div class="col-md-3">
                    <select class="form-select" id="sortOrder" name="sortOrder">
                        <option value="asc" ${param.sortOrder == 'asc' ? 'selected' : ''}>Crescente</option>
                        <option value="desc" ${param.sortOrder == 'desc' ? 'selected' : ''}>Decrescente</option>
                    </select>
                </div>
                <div class="col-md-auto">
                    <button type="submit" class="btn btn-primary">Applica Ordine</button>
                </div>
            </form>
        </div>

        <%-- Nuova Form per la Ricerca --%>
        <div class="mb-3">
            <form action="oggetti" method="get" class="row g-3 align-items-center">
                <input type="hidden" name="contenitoreId" value="${contenitore.id}">
                <input type="hidden" name="sortBy" value="${param.sortBy}"> <%-- Mantiene l'ordinamento esistente --%>
                <input type="hidden" name="sortOrder" value="${param.sortOrder}"> <%-- Mantiene la direzione di ordinamento esistente --%>

                <div class="col-md-auto">
                    <label for="searchTerm" class="form-label mb-0">Cerca Oggetto:</label>
                </div>
                <div class="col-md-4">
                    <input type="text" class="form-control" id="searchTerm" name="searchTerm"
                           placeholder="Cerca per nome..." value="<c:out value="${searchTerm != null ? searchTerm : ''}" />">
                </div>
                <div class="col-md-auto">
                    <button type="submit" class="btn btn-info">Cerca</button>
                </div>
                <c:if test="${not empty searchTerm}">
                    <div class="col-md-auto">
                        <a href="<c:url value='/oggetti?contenitoreId=${contenitore.id}&sortBy=${param.sortBy}&sortOrder=${param.sortOrder}' />" class="btn btn-outline-secondary">Reset Ricerca</a>
                    </div>
                </c:if>
            </form>
        </div>


        <%-- Gestione dei messaggi di errore/successo --%>
        <c:if test="${not empty param.errore}"><div class="alert alert-danger" role="alert"><c:out value="${param.errore}" /></div></c:if>
        <c:if test="${not empty param.successo}"><div class="alert alert-success" role="alert"><c:out value="${param.successo}" /></div></c:if>

        <c:choose>
            <c:when test="${not empty listaOggetti}">
                <div class="table-responsive">
                    <table class="table table-hover table-bordered align-middle">
                        <thead class="table-dark">
                            <tr>
                                <th>Nome Oggetto</th>
                                <th class="text-center">Quantità</th>
                                <th class="text-center">Soglia Spesa</th>
                                <th>Data Inserimento</th>
                                <th>Dettagli Aggiuntivi</th>
                                <th class="text-center">Azioni</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="oggetto" items="${listaOggetti}">
                                <%-- Applica la classe 'highlight' se il nome dell'oggetto contiene il termine di ricerca --%>
                                <tr class="${oggetto.numero <= oggetto.sogliaMinima ? 'table-warning' : ''} ${not empty searchTerm && oggetto.nome.toLowerCase().contains(searchTerm.toLowerCase()) ? 'highlight' : ''}">
                                    <td><c:out value="${oggetto.nome}" /></td>
                                    <td class="text-center"><c:out value="${oggetto.numero}" /></td>
                                    <td class="text-center"><c:out value="${oggetto.sogliaMinima}" /></td>
                                    <td>
                                        <fmt:parseDate value="${oggetto.dataInserimento}" pattern="yyyy-MM-dd" var="parsedDate" />
                                        <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy" />
                                    </td>
                                    <td>
                                        <c:set var="listaKV" value="${mappaChiaviValore[oggetto.id]}" />
                                        <c:if test="${not empty listaKV}">
                                            <ul class="details-list mb-0">
                                                <c:forEach var="kv" items="${listaKV}">
                                                    <li>
                                                        <strong><c:out value="${kv.chiave}" />:</strong> <c:out value="${kv.valore}" />
                                                        <%-- LINK PER LA MODIFICA --%>
                                                        <a href="<c:url value='/modifica-dettaglio?kvId=${kv.id}&contenitoreId=${contenitore.id}' />" class="ms-2 text-warning icon-link" title="Modifica dettaglio">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" fill="currentColor" class="bi bi-pencil" viewBox="0 0 16 16"><path d="M12.146.146a.5.5 0 0 1 .708 0l3 3a.5.5 0 0 1 0 .708l-10 10a.5.5 0 0 1-.168.11l-5 2a.5.5 0 0 1-.65-.65l2-5a.5.5 0 0 1 .11-.168zM11.207 2.5 13.5 4.793 14.793 3.5 12.5 1.207zm1.586 3L10.5 3.207 4 9.707V10h.5a.5.5 0 0 1 .5.5v.5h.5a.5.5 0 0 1 .5.5v.5h.293zm-9.761 5.175-.106.106-1.528 3.821 3.821-1.528.106-.106A.5.5 0 0 1 5 12.5V12h-.5a.5.5 0 0 1-.5-.5V11h-.5a.5.5 0 0 1-.468-.325"/></svg>
                                                        </a>
                                                        <%-- FORM PER L'ELIMINAZIONE DEL DETTAGLIO --%>
                                                        <form action="<c:url value='/gestisci-dettaglio' />" method="post" style="display: inline;" class="ms-1">
                                                            <input type="hidden" name="action" value="delete">
                                                            <input type="hidden" name="kvId" value="${kv.id}">
                                                            <input type="hidden" name="contenitoreId" value="${contenitore.id}">
                                                            <button type="submit" class="btn btn-link text-danger p-0"
                                                                    style="vertical-align: baseline;"
                                                                    onclick="return confirm('Sei sicuro di voler eliminare questo dettaglio?');"
                                                                    title="Elimina dettaglio">
                                                                <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" fill="currentColor" class="bi bi-trash" viewBox="0 0 16 16"><path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0z"/><path d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4zM2.5 3h11V2h-11z"/></svg>
                                                            </button>
                                                        </form>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </c:if>
                                        <c:if test="${empty listaKV}"><small class="text-muted">Nessun dettaglio.</small></c:if>
                                    </td>
                                    <td class="text-center">

                                        <%-- 1. Pulsante Aggiungi Dettaglio --%>
                                        <a href="<c:url value='/nuova-chiave-valore?oggettoId=${oggetto.id}&contenitoreId=${contenitore.id}' />"
                                           class="btn btn-sm btn-outline-info" title="Aggiungi Dettaglio">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-journal-plus" viewBox="0 0 16 16">...</svg>
                                        </a>

                                        <%-- 2. Pulsante Modifica Oggetto --%>
                                        <a href="<c:url value='/modifica-oggetto?oggettoId=${oggetto.id}' />"
                                           class="btn btn-sm btn-outline-warning ms-1" title="Modifica Oggetto">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-pencil-square" viewBox="0 0 16 16">...</svg>
                                        </a>

                                        <%-- 3. FORM PER L'ELIMINAZIONE DELL'OGGETTO --%>
                                        <form action="<c:url value='/oggetti' />" method="post" class="d-inline">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="oggettoId" value="${oggetto.id}">
                                            <input type="hidden" name="contenitoreId" value="${contenitore.id}">
                                            <button type="submit" class="btn btn-sm btn-outline-danger ms-1" title="Elimina Oggetto"
                                                    onclick="return confirm('Sei sicuro di voler eliminare questo oggetto e tutti i suoi dettagli?');">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash3-fill" viewBox="0 0 16 16">...</svg>
                                            </button>
                                        </form>

                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info" role="alert">
                    Nessun oggetto trovato in questo contenitore.
                    <a href="<c:url value='/nuovo-oggetto?contenitoreId=${contenitore.id}' />" class="alert-link">Aggiungi il primo!</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>