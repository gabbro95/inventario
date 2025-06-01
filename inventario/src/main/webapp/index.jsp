<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Inventario - Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="card shadow p-4 mx-auto" style="max-width: 400px;">
        <h3 class="mb-4 text-center">Login</h3>

        <form action="login" method="post">
            <div class="mb-3">
                <label for="email" class="form-label">Email Google</label>
                <input type="email" class="form-control" id="email" name="email" required placeholder="email@gmail.com">
            </div>
            <div class="d-grid">
                <button type="submit" class="btn btn-primary">Entra</button>
            </div>
        </form>

        <%
            String errore = (String) request.getAttribute("errore");
            if (errore != null) {
        %>
        <div class="alert alert-danger mt-3"><%= errore %></div>
        <% } %>
    </div>
</div>
</body>
</html>
