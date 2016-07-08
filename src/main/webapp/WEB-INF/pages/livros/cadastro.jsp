<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
	<head>
	
		<jsp:include page = "../comum/cabecalho.jsp" />
		
	</head>
	
<body>

	<jsp:include page = "../comum/menu.jsp" />
	
	<!-- Begin page content -->
    <div class="" style="margin: 10px;">
      <p class="lead">Livros</p>      
    </div>
    
    <strong>
    	<form:errors path="livro.*" element="div" cssClass="alert alert-danger error" />
    </strong>
    
    <!-- MENSAGENS -->
    <jsp:include page = "../comum/mensagens.jsp" />
        
    <c:if test="${acao eq 'excluir'}">
    	<form id="formExclusao">
    		<input type="hidden" id ="acao" name="acao">
			<input type="hidden" id ="identificador" name="identificador">
    		<div class="panel panel-default" style="margin: 10px;">
	    		<div class="panel-body">
	    			<button type="button" class="btn btn-default" onClick="carregarExclusao('excluir',${livro.id},'exclusaolivro');">
						<span class="glyphicon glyphicon-trash" aria-hidden="true"></span> Excluir
					</button>
					<button type="button" class="btn btn-default" onClick="location.href='livros'">
						<span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Cancelar
					</button>
				</div>
			</div>
		</form>
	</c:if>
        
    <div class="panel panel-default" style="margin: 10px;">
		<div class="panel-heading">
	    	<h3 class="panel-title">Cadastro de Livros</h3>
	  	</div>
	  	<div class="panel-body">
	  		<form:form id="formCadLivros" method="POST" cssClass="navbar" action="livrocadastrado" modelAttribute="livro">
	  			<form:hidden path="id" />
	  			<div class="panel panel-default">
					<div class="panel-body">
				    	<div class="form-group">
		    				<div class="input-group">
								<span class="input-group-addon" id="basic-addon1">ISBN</span> 
								<form:input path="isbn" cssClass="form-control required" placeholder="Digite o isbn igual registrado no livro" aria-describedby="basic-addon1" size="50" disabled="${acao eq 'consultar' or acao eq 'excluir'}" />			
							</div>
			  			</div>
				  		<div class="form-group">
			    			<div class="input-group">
								<span class="input-group-addon" id="basic-addon1">Título</span> 
								<form:input path="titulo" cssClass="form-control required" placeholder="Digite o título do livro" aria-describedby="basic-addon1" size="50" disabled="${acao eq 'consultar' or acao eq 'excluir'}" />			
							</div>
			  			</div>
			  			<div class="form-group">
			    			<div class="input-group">
								<span class="input-group-addon" id="basic-addon1">Sub-Título</span>
								<form:input path="subtitulo" cssClass="form-control required" placeholder="Digite o sub-título do livro, se houver" aria-describedby="basic-addon1" size="50" disabled="${acao eq 'consultar' or acao eq 'excluir'}" />						
							</div>
						</div>  								
						<div class="form-group">
			    			<div class="input-group">
								<span class="input-group-addon" id="basic-addon1">Autor(es)</span>
								<form:input path="autoresAgrupados" cssClass="form-control required" placeholder="Digite o(s) autor(es) separados por ';' (ponto-e-vírgula)" aria-describedby="basic-addon1" size="50" disabled="${acao eq 'consultar' or acao eq 'excluir'}" />		
							</div>
						</div>	
			  		</div>
				</div>
	  			<div class="panel panel-default">
					<div class="panel-body">
						<div class="form-group">
		    				<div class="input-group">
								<span class="input-group-addon" id="basic-addon1">Editora</span> 
								<form:input path="editora" cssClass="form-control required" placeholder="Digite a editora do livro" aria-describedby="basic-addon1" size="50" disabled="${acao eq 'consultar' or acao eq 'excluir'}"/>
							</div>
						</div>
						<div class="form-group">
		    				<div class="input-group">
								<span class="input-group-addon" id="basic-addon1">Local de Publicação</span> 
								<form:input path="localPublicacao" cssClass="form-control required" placeholder="Digite o local de publicação do livro" aria-describedby="basic-addon1" size="40" disabled="${acao eq 'consultar' or acao eq 'excluir'}"/>
							</div>
						</div>
						<div class="form-group">
		    				<div class="input-group navbar-left">
								<span class="input-group-addon" id="basic-addon1">Ano de Publicação</span> 
								<form:input path="anoPublicacao" cssClass="form-control required" placeholder="Digite o ano de publicação do livro" aria-describedby="basic-addon1" size="30" disabled="${acao eq 'consultar' or acao eq 'excluir'}"/>
								<span class="input-group-addon" id="basic-addon1">Número de Páginas</span> 
								<form:input path="nroPaginas" cssClass="form-control required" placeholder="Digite o nro de páginas do livro" aria-describedby="basic-addon1" size="30" disabled="${acao eq 'consultar' or acao eq 'excluir'}"/>
							</div>							
						</div>
						&nbsp;						
						<div class="form-group">
							<div class="input-group navbar-left">
								<span class="input-group-addon" id="basic-addon1">Gênero</span> 
								<form:input path="genero" cssClass="form-control required" placeholder="Digite o gênero do livro" aria-describedby="basic-addon1" size="50" disabled="${acao eq 'consultar' or acao eq 'excluir'}"/>
								<span class="input-group-addon" id="basic-addon1">Classificação</span> 
								<form:input path="classificacao" cssClass="form-control required" placeholder="Digite a classificação do livro" aria-describedby="basic-addon1" size="50" disabled="${acao eq 'consultar' or acao eq 'excluir'}"/>
							</div>
						</div>
						&nbsp;
						<div class="form-group">
		    				<div class="input-group">
								<span class="input-group-addon" id="basic-addon1">Público Alvo</span> 
								<form:input path="publicoAlvo" cssClass="form-control required" placeholder="Digite o público alvo do livro" aria-describedby="basic-addon1" size="50" disabled="${acao eq 'consultar' or acao eq 'excluir'}"/>
							</div>
						</div>															
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-body">
						<div class="form-group">
		    				<div class="input-group">
								<span class="input-group-addon" id="basic-addon1">Data de Aquisição</span> 
								<form:input path="dataAquisicaoFormatada" cssClass="form-control required" placeholder="Digite a data de aquisição do livro no formato 'dd/mm/aaaa' " aria-describedby="basic-addon1" size="50" disabled="${acao eq 'consultar' or acao eq 'excluir'}" />								
							</div>
						</div>
						<div class="panel panel-default">
							<div class="panel-heading">Tipo de Aquisição</div>
							<div class="panel-body">
								<div class="input-group">
									<span class="input-group-addon"><form:radiobutton path="tipoAquisicao" value="DOACAO" disabled="${acao eq 'consultar' or acao eq 'excluir'}"/> DOAÇÃO </span>
									<span class="input-group-addon"><form:radiobutton path="tipoAquisicao" value="COMPRA" disabled="${acao eq 'consultar' or acao eq 'excluir'}"/> COMPRA </span>
								</div><!-- /input-group -->
							</div>
						</div>
						<div class="form-group">
							<div id="divDoador" class="form-group" style="display: block;">
		    					<div class="input-group">
									<span class="input-group-addon" id="basic-addon1">Nome do Doador</span> 
									<form:input path="nomeDoador" cssClass="form-control required" placeholder="Digite o nome do doador do livro, se for o caso" aria-describedby="basic-addon1" size="50" disabled="${acao eq 'consultar' or acao eq 'excluir'}"/>
								</div>
							</div>
						</div>
					</div>
				</div>		
				<div class="panel panel-default">
					<div class="panel-body">
						<div class="form-group">
		    				<div class="input-group">
								<span class="input-group-addon" id="basic-addon1">Observações</span> 
								<form:input path="observacoes" cssClass="form-control required" placeholder="Digite alguma informação complementar do livro, se houver" aria-describedby="basic-addon1" size="50" disabled="${acao eq 'consultar' or acao eq 'excluir'}"/>
							</div>
						</div>
					</div>
				</div>
						
				<!-- BOTOES -->
				<c:if test="${acao != 'consultar' && acao != 'excluir'}">
					<button type="submit" class="btn btn-default">
						<span class="glyphicon glyphicon-ok" aria-hidden="true"></span> Salvar
					</button>
					<button type="button" class="btn btn-default" onClick="location.href='livros'">
						<span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Cancelar
					</button>
				</c:if>
				<c:if test="${acao eq 'consultar'}">
					<button type="button" class="btn btn-default" onClick="location.href='livros'">
						<span class="glyphicon glyphicon-triangle-left" aria-hidden="true"></span> Voltar
					</button>					
				</c:if>			
			</form:form>
		
			<form:form id="formCapaLivro" method="POST" cssClass="navbar" action="uploadFile" enctype="multipart/form-data">	
				<div class="panel panel-default">
					<div class="panel-body">
						<div class="form-group">
		    				<div class="input-group">
								<span class="input-group-addon" id="basic-addon1">Foto do livro</span> 
								<%-- <form:input path="nomeFotoCapa" cssClass="form-control required" placeholder="Digite o diretório e arquivo com foto do livro" aria-describedby="basic-addon1" size="50" disabled="${acao eq 'consultar' or acao eq 'excluir'}"/> --%>
								<input type="file" name="file" id="file" />
							</div>
						</div>	
						<input type="submit" value="Upload"> Press here to upload the file!					
					</div>
				</div>
			</form:form>
				
	  	</div>
	</div>

	<jsp:include page = "../comum/rodape.jsp" />

</body>
</html>