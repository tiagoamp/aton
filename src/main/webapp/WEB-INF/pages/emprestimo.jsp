<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>

		<jsp:include page = "comum/cabecalho.jsp" />

	</head>
	
<body>

	<jsp:include page = "comum/menu.jsp" />
	
	<!-- Begin page content -->
    <div class="" style="margin: 10px;">
      <p class="lead">Emprestimo de Livros</p>      
    </div>
    
    <!-- MENSAGENS -->
    <jsp:include page = "comum/mensagens.jsp" />
    
    <!-- PESQUISA -->
    <div class="panel panel-default" style="margin: 10px;">
		<div class="panel-heading">
	    	<h3 class="panel-title">Emprestimo de Livros</h3>
	  	</div>
	  	<div class="panel-body">
	  		<form id="formConsultaLivross" class="navbar-form navbar-left" method="POST" action="consultalivro" role="search">
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1">Consulta por ISBN</span> 
					<input type="text" name="tISBN" class="form-control" placeholder="Digite o ISBN do livro" aria-describedby="basic-addon1">				
				</div>
				<button type="submit" class="btn btn-default">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span> Buscar
				</button>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1">Consulta por Dados</span> 
					<input type="text" name="tDados" class="form-control" size="40" placeholder="Digite Título ou Autor do livro" aria-describedby="basic-addon1">					
				</div>
				<button type="submit" class="btn btn-default">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span> Buscar
				</button>				
			</form>	
	  	</div>
	</div>
	
	<!-- INCLUSÃO -->
    <div class="panel panel-default" style="margin: 10px;">
		<div class="panel-heading">
	    	<h3 class="panel-title">Emprestimo de Livros</h3>
	  	</div>
	  	<div class="panel-body">
	  		<form class="navbar-form" role="redirect">
				<div class="btn-group" role="group" aria-label="">
					<button type="button" class="btn btn-default" onClick="location.href='cadastrolivro'">
						<span class="glyphicon glyphicon-book" aria-hidden="true"></span> Cadastrar Livro
					</button>	
				</div>
				<div class="btn-group" role="group" aria-label="">
					<button type="button" class="btn btn-default" onClick="location.href='listalivros'">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span> Listar Livros
					</button>	
				</div>
			</form>
	  	</div>
	</div>
      
    <!-- LISTAGEM -->
    <c:if test="${not empty listalivros}">
	    <div class="panel panel-default" style="margin: 10px;">
			<div class="panel-heading">
		    	<h3 class="panel-title">Resultado da Pesquisa</h3>
		  	</div>
		  	<div class="panel-body">
		  		<div class="form-group">
					<div class="list-group">
						<form id="formAcoes">
							<input type="hidden" id ="acao" name="acao">
							<input type="hidden" id ="identificador" name="identificador">
							<c:forEach items="${listalivros}" var="livro">
								<li class="list-group-item">				
									&nbsp;&nbsp; <a href="javascript:carregarAcoes('consultar',${livro.id},'cadastrolivro');" title="Consultar"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a>
									&nbsp;&nbsp; <a href="javascript:carregarAcoes('alterar',${livro.id},'cadastrolivro');" title="Alterar"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a> 
									&nbsp;&nbsp; <a href="javascript:carregarAcoes('excluir',${livro.id},'cadastrolivro');" title="Excluir"><span class="glyphicon glyphicon-remove-circle" aria-hidden="true"></span></a>
									&nbsp;&nbsp;&nbsp;&nbsp;
									<c:out value="${livro.isbn}" /> - 
									<c:out value="${livro.titulo}" /> - 
									<c:out value="${livro.autoresAgrupados}" />							
								</li>
							</c:forEach>
						</form>
					</div>
				</div>
		  	</div>
		</div>
	</c:if>

	<jsp:include page = "comum/rodape.jsp" />

</body>
</html>