<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.agenda.model.JavaBeans" %>
<%@ page import="java.util.ArrayList" %>

<%
    ArrayList<JavaBeans> list = (ArrayList<JavaBeans>) request.getAttribute("contacts");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Agenda de Contatos</title>
    <link rel="icon" href="images/phone-call.png">
    <link rel="stylesheet" href="style.css">
</head>
<body>
<main>
    <h1>Agenda de Contatos</h1>
    <a href="novoContato.html" class="BotaoNovo">Novo Contato</a>
    <br><br>
    <a href="report" class="Botao2">Relatorio</a>

    <table id="table">
        <caption>Lista de Contatos</caption>
        <thead>
        <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Telefone</th>
            <th>Email</th>
            <th>Opções</th>
        </tr>
        </thead>
        <tbody>
        <% if (list != null && !list.isEmpty()) { %>
        <% for (JavaBeans c : list) { %>
        <tr>
            <td><%= c.getId() %></td>
            <td><%= c.getNome() %></td>
            <td><%= c.getTelefone() %></td>
            <td><%= c.getEmail() %></td>
            <td>
                <a href="select?id=<%= c.getId() %>" class="Botao1">Editar</a>
                <a href="javascript:confirmar(<%= c.getId() %>)" class="Botao2">Excluir</a>
            </td>
        </tr>
        <% } %>
        <% } else { %>
        <tr>
            <td colspan="5" style="text-align: center;">Nenhum contato encontrado.</td>
        </tr>
        <% } %>
        </tbody>
    </table>
</main>

<script src="Scripts/confirmador.js"></script>
</body>
</html>
