<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.gabbro95.inventario.model.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Oggetti</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container py-4">
    <h2 class="mb-4">Oggetti del contenitore: <%= request.getAttribute("nomeContenitore") %></h2>

    <a href="dashboard" class="btn btn-secondary mb-3">Torna alla Dashboard</a>

    <table class="table table-bordered table-hover bg-white">
        <thead class="table-dark">
            <tr>
                <th>Nome</th>
                <th>Quantità</th>
                <th>Data Inserimento</th>
                <th>Chiavi/Valori</th>
            </tr>
        </thead>
        <tbody>
        <%
            List<Oggetto> oggetti = (List<Oggetto>) request.getAttribute("oggetti");
            Map<Integer, List<ChiaveValore>> mappaChiavi = (Map<Integer, List<ChiaveValore>>) request.getAttribute("mappaChiavi");

            if (oggetti != null) {
                for (Oggetto o : oggetti) {
        %>
            <tr class="<%= o.getNumero() <= 0 ? "table-danger" : "" %>">
                <td><%= o.getNome() %></td>
                <td><%= o.getNumero() %></td>
                <td><%= o.getDataInserimento() %></td>
                <td>
                    <%
                        List<ChiaveValore> attributi = mappaChiavi.get(o.getId());
                        if (attributi != null) {
                            for (ChiaveValore kv : attributi) {
                    %>
                        <strong><%= kv.getChiave() %>:</strong> <%= kv.getValore() %><br>
                    <%
                            }
                        }
                    %>
                </td>
            </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>
</div>
</body>
</html>
