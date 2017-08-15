// RADIO BUTTON DE EXIBICAO DE SENHA DEPENDENDO DO PERFIL
$(document).ready(function() {
    $('input[type=radio][name=role]').change(function() {
        if (this.value == 'LEITOR') {
        	$('#formCadPessoas').find('#tSenha').removeClass("required");
        	$('#formCadPessoas').find('#tSenha').text("");
            document.getElementById("divSenha").style.display = "none";            
        }
        else {
            document.getElementById("divSenha").style.display = "block";
            $('#formCadPessoas').find('#tSenha').addClass("required");
        }
    });
});

//RADIO BUTTON DE EXIBICAO DE DOADOR DO LIVRO, DEPENDENDO DO TIPO DE AQUISICAO
$(document).ready(function() {
    $('input[type=radio][name=tipoAquisicao]').change(function() {
        if (this.value == 'COMPRA') {
        	document.getElementById("divDoador").style.display = "none";            
        }
        else {
            document.getElementById("divDoador").style.display = "block";            
        }
    });
});

// SETANDO PARAMETROS FORM DE ACOES
/*function carregarAcoes(pacao, pid, paction){
	var f = document.getElementById('formAcoes');
    f.method="get";
    f.acao.value = pacao;
    f.identificador.value = pid;
    f.action = paction;
    f.submit();
}

function carregarExclusao(pacao, pid, paction){
	var f = document.getElementById('formExclusao');
	f.method="post";
    f.acao.value = pacao;
    f.identificador.value = pid;
    f.action = paction;
    f.submit();
}*/