function confirmar(id){
    let resposta = confirm("Deseja excluir o registro?");
    if(resposta){
        window.location.href = "delete?id=" + id;// redireciona a outra tela delete vai para outro doc
        // e id é o parametro
    }
}