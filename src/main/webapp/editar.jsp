<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Editar Contato</title>
    <link rel="icon" href="images/phone-call.png">
    <link rel="stylesheet" href="style.css">
</head>
<body>
<h1>Editar Contato</h1>
    <form action="update" method="post" name="frmContato">
        <table>
            <tr>
                <td><input type="text" name="id" id="Id" class="Id" value="<%System.out.println(request.getAttribute("id"));%> "required></td>
            </tr>
            <tr>
                <td><input type="text" name="nome" class="Nome" value="<%System.out.println(request.getAttribute("nome"));%>"required></td>
            </tr>
            <tr>
                <td><input type="text" name="telefone" pattern="\d" class="Telefone" value="<%System.out.println(request.getAttribute("telefone"));%>" required></td>
            </tr>

            <tr>
                <td><input type="text" name="email" class="Email" value="<%System.out.println(request.getAttribute("email"));%>"  required></td>
            </tr>

        </table>
        <input type="submit" value="Salvar" class="Botao1" onclick="validar()">
    </form>
    <script src="Scripts/validador.js"></script>
</body>
</html>