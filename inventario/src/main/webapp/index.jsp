<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Se l'utente è già loggato, lo reindirizzo subito alla dashboard. --%>
<c:if test="${not empty sessionScope.utente}">
    <c:redirect url="${pageContext.request.contextPath}/dashboard"/>
</c:if>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Inventario - Accedi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<c:url value='/css/style.css' />" rel="stylesheet">
</head>
<body class="login-page-background">
    <%-- 
      Il contenuto viene inserito in un contenitore che eredita gli stili
      da .login-container definito in style.css. Non servono più tag <style> qui.
    --%>
    <div class="login-container card shadow-lg">
        <div class="card-header bg-dark text-white text-center py-3">
            <h4>Accesso Inventario</h4>
        </div>
        <div class="card-body p-4">
            
            <c:if test="${not empty requestScope.erroreLogin}">
                <div class="alert alert-danger text-center" role="alert">
                    <c:out value="${requestScope.erroreLogin}" />
                </div>
            </c:if>
            <c:if test="${not empty param.logout}">
                <div class="alert alert-success text-center" role="alert">
                    Logout effettuato con successo!
                </div>
            </c:if>
             <c:if test="${not empty param.erroreAuth}">
                <div class="alert alert-warning text-center" role="alert">
                    <c:out value="Errore di autenticazione: ${param.erroreAuth}" />
                </div>
            </c:if>

            <div class="d-grid mb-3">
                <a href="<c:url value='/oauth2login' />" class="btn btn-outline-danger btn-lg google-btn">
                    <img src="https://developers.google.com/identity/images/g-logo.png" alt="Logo Google">
                    Accedi con Google
                </a>
            </div>

            <div class="divider-text"><span>oppure</span></div>

            <form action="<c:url value='/login' />" method="post">
                <div class="mb-3">
                    <label for="email" class="form-label">Indirizzo Email:</label>
                    <input type="email" class="form-control form-control-lg" id="email" name="email" 
                           required placeholder="latuaemail@esempio.com" value="<c:out value='${param.email_persistente}'/>">
                </div>
                <div class="d-grid">
                    <button type="submit" class="btn btn-primary btn-lg">Entra con Email</button>
                </div>
            </form>
            
        </div>
        <div class="card-footer text-center py-3">
            <small class="text-muted">&copy; <jsp:expression>java.time.Year.now().getValue()</jsp:expression> Inventario App</small>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
