function validar() {
    const form = document.forms["frmContato"];
    const nome = form["nome"].value.trim();
    const telefone = form["telefone"].value.trim();

    // Verifica se o campo nome está vazio
    if (nome === "") {
        alert("Preencha o campo nome");
        form["nome"].focus();
        return false;
    }

    // Verifica se o campo telefone está vazio
    if (telefone === "") {
        alert("Preencha o campo telefone");
        form["telefone"].focus();
        return false;
    }

    // Verifica se o telefone contém apenas números
    if (!/^\d+$/.test(telefone)) {
        alert("Digite apenas números no telefone");
        form["telefone"].focus();
        return false;
    }

    // Se tudo estiver certo, envia o formulário
    form.submit();
}