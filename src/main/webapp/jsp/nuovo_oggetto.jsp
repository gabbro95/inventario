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
    <title>Nuovo Oggetto in <c:out value="${contenitore.nome}"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-lg-6">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <%-- MODIFICA 1: TITOLO DINAMICO --%>
                        <h4 class="mb-0">Aggiungi un Oggetto a: <c:out value="${contenitore.nome}"/></h4>
                    </div>
                    <div class="card-body">
                        <%-- Assicurati che l'action punti a un servlet che gestisce la creazione dell'oggetto --%>
                        <form action="<c:url value='/nuovo-oggetto' />" method="post">
                            
                            <%-- MODIFICA 2: CAMPO NASCOSTO FONDAMENTALE --%>
                            <input type="hidden" name="contenitoreId" value="${contenitore.id}">

                            <div class="mb-3">
                                <label for="nomeOggetto" class="form-label">Nome oggetto:</label>
                                <input type="text" class="form-control" id="nomeOggetto" name="nomeOggetto" required>
                            </div>
                            <div class="mb-3">
                                <label for="quantitaIniziale" class="form-label">Quantità iniziale:</label>
                                <input type="number" class="form-control" id="quantitaIniziale" name="quantita" value="1" min="0" required>
                            </div>
                            <div class="text-end">
                                <a href="<c:url value='/oggetti?contenitoreId=${contenitore.id}' />" class="btn btn-secondary">Annulla</a>
                                <button type="submit" class="btn btn-success">Salva Oggetto</button>
                            </div>
                            <div class="mb-3">
							    <label for="sogliaMinima" class="form-label">Soglia per lista spesa:</label>
							    <input type="number" class="form-control" id="sogliaMinima" name="sogliaMinima" value="5" min="0" required>
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