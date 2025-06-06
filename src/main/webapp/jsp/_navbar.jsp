<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Aggiunta della libreria di funzioni JSTL. Questa è la modifica chiave. --%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%-- 
  Questa è la barra di navigazione riutilizzabile.
  Viene inclusa nelle altre pagine con: <jsp:include page="/jsp/_navbar.jsp" />
  (Assumendo che questo file si trovi in /webapp/jsp/_navbar.jsp)
--%>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm">
    <div class="container-fluid">
        <a class="navbar-brand d-flex align-items-center" href="<c:url value='/dashboard' />">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-box-seam-fill me-2" viewBox="0 0 16 16">
                <path fill-rule="evenodd" d="M15.528 2.973a.75.75 0 0 1 .472.696v8.662a.75.75 0 0 1-.472.696l-7.25 2.9a.75.75 0 0 1-.557 0l-7.25-2.9A.75.75 0 0 1 0 12.331V3.669a.75.75 0 0 1 .471-.696L7.75.073a.75.75 0 0 1 .5 0zM10.404 2 4.25 4.461 1.846 3.5 1 3.839v.4l6.5 2.6v7.922l.5.2.5-.2V6.84l6.5-2.6v-.4l-.846-.339L10.404 2z"/>
            </svg>
            Inventario App
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavDropdown">
            <%-- Solo se l'utente è loggato, mostra i link di navigazione e le info utente --%>
            <c:if test="${not empty sessionScope.utente}">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <%-- CORREZIONE: Utilizzo di fn:endsWith per la compatibilità --%>
                        <a class="nav-link <c:if test="${fn:endsWith(pageContext.request.servletPath, '/dashboard')}">active</c:if>" aria-current="page" href="<c:url value='/dashboard' />">Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link <c:if test="${fn:endsWith(pageContext.request.servletPath, '/carrello')}">active</c:if>" href="<c:url value='/carrello' />">Lista Spesa</a>
                    </li>
                </ul>
                <<%-- Blocco utente aggiornato con menu a tendina --%>
				<div class="dropdown">
				    <a href="#" class="nav-link dropdown-toggle d-flex align-items-center text-white" 
				       id="navbarDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
				        
				        <c:if test="${not empty sessionScope.utente.immagineProfilo}">
				            <img src="<c:out value='${sessionScope.utente.immagineProfilo}'/>" alt="Profilo" class="rounded-circle me-2" style="width: 30px; height: 30px; object-fit: cover;">
				        </c:if>
				        <c:out value="${not empty sessionScope.utente.nome ? sessionScope.utente.nome : sessionScope.utente.email}" />
				    </a>
				    
				    <ul class="dropdown-menu dropdown-menu-end dropdown-menu-dark" aria-labelledby="navbarDropdownMenuLink">
				        <li>
				            <a class="dropdown-item" href="<c:url value='/profilo' />">
				                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person-fill me-2" viewBox="0 0 16 16"><path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6"/></svg>
				                Il Mio Profilo
				            </a>
				        </li>
				        <li><hr class="dropdown-divider"></li>
				        <li>
				            <a class="dropdown-item" href="<c:url value='/logout' />">
				                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-box-arrow-right me-2" viewBox="0 0 16 16"><path fill-rule="evenodd" d="M10 12.5a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v2a.5.5 0 0 0 1 0v-2A1.5 1.5 0 0 0 9.5 2h-8A1.5 1.5 0 0 0 0 3.5v9A1.5 1.5 0 0 0 1.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-2a.5.5 0 0 0-1 0z"/><path fill-rule="evenodd" d="M15.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L14.293 7.5H5.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708z"/></svg>
				                Logout
				            </a>
				        </li>
				    </ul>
				</div>
            </c:if>
        </div>
    </div>
</nav>
