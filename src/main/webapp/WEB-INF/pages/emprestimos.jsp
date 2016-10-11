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
      <p class="lead">Empréstimos</p>      
    </div>
    
    <!-- MENSAGENS -->
    <jsp:include page = "comum/mensagens.jsp" />
    
    <!-- PESQUISA -->
    <div class="panel panel-default" style="margin: 10px;">
		<div class="panel-heading">
	    	<h3 class="panel-title">Pesquisa de Emprestimos</h3>
	  	</div>
	  	<div class="panel-body">
	  		<form id="formConsultaEmprestimos" class="navbar-form navbar-left" method="POST" action="consultaemprestimo" role="search">
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1">Consulta por Livro</span> 
					<input type="text" name="tLivro" size="32" class="form-control" placeholder="Digite o título ou autor do livro" aria-describedby="basic-addon1">				
				</div>
				<button type="submit" class="btn btn-default">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span> Buscar
				</button>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1">Consulta por Leitor</span> 
					<input type="text" name="tPessoa" size="32" class="form-control" placeholder="Digite o nome do leitor" aria-describedby="basic-addon1">					
				</div>
				<button type="submit" class="btn btn-default">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span> Buscar
				</button>				
			</form>	
	  	</div>
	</div>
	
	<!-- LISTAGEM -->
    <div class="panel panel-default" style="margin: 10px;">
		<div class="panel-heading">
	    	<h3 class="panel-title">Empréstimos</h3>
	  	</div>
	  	<div class="panel-body">
	  		<form class="navbar-form" role="redirect">
				<div class="btn-group" role="group" aria-label="">
					<button type="button" class="btn btn-default" onClick="location.href='listaemprestimosabertos'">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span> Listar Empréstimos em aberto
					</button>	
				</div>
				<div class="btn-group" role="group" aria-label="">
					<button type="button" class="btn btn-default" onClick="location.href='listaemprestimos'">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span> Listar todos os Empréstimos
					</button>	
				</div>
			</form>
	  	</div>
	</div>
      
    <!-- LISTAGEM -->
    <c:if test="${not empty listaemprestimos}">
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
							<c:forEach items="${listaemprestimos}" var="emprestimo">
								<li class="list-group-item">				
									&nbsp;&nbsp;
									<c:if test="${empty emprestimo.dataDevolucao}">
										<a href="javascript:carregarAcoes('devolver',${emprestimo.id},'devolucaolivro');" title="Devolver"><span class="glyphicon glyphicon-transfer" aria-hidden="true"></span></a> 
									</c:if>
									<c:if test="${not empty emprestimo.dataDevolucao}">
										<span class="glyphicon glyphicon-transfer" aria-hidden="true"></span> 
									</c:if>									
									&nbsp;&nbsp;&nbsp;&nbsp;
									Data: <c:out value="${emprestimo.dataEmprestimoFormatada}" /> -
									Livro: <c:out value="${emprestimo.livro.titulo}" /> - 
									Leitor: <c:out value="${emprestimo.pessoa.nome}" />	-									 
									Devolução Progr.: <c:out value="${emprestimo.dataDevolucaoProgramadaFormatada}" /> - 
									Devolução: <c:out value="${emprestimo.dataDevolucaoFormatada}" />																		
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