<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>

		<jsp:include page = "comum/header.jsp" />

	</head>
	
<body>

	<jsp:include page = "comum/menu.jsp" />
	
	<!-- Begin page content -->
    <div class="" style="margin: 10px;">
      <p class="lead">Pessoas</p>      
    </div>
    
    <!-- MENSAGENS -->
    <jsp:include page = "comum/messages.jsp" />
    
    <!-- PESQUISA -->
    <div class="panel panel-default" style="margin: 10px;">
		<div class="panel-heading">
	    	<h3 class="panel-title">Pesquisa de Pessoas</h3>
	  	</div>
	  	<div class="panel-body">
	  		<form id="formConsultaPessoas" class="navbar-form navbar-left" method="POST" action="consultapessoa" role="search">
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1">Consulta por E-mail</span> 
					<input type="text" name="tEmail" class="form-control" placeholder="Digite o e-mail do leitor" aria-describedby="basic-addon1">				
				</div>
				<button type="submit" class="btn btn-default">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span> Buscar
				</button>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1">Consulta por Dados</span> 
					<input type="text" name="tDados" class="form-control" size="48" placeholder="Digite o nome, telefone ou perfil da pessoa" aria-describedby="basic-addon1">					
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
	    	<h3 class="panel-title">Cadastramento de Pessoas</h3>
	  	</div>
	  	<div class="panel-body">
	  		<form class="navbar-form" role="redirect">
				<div class="btn-group" role="group" aria-label="">
					<button type="button" class="btn btn-default" onClick="location.href='cadastropessoa'">
						<span class="glyphicon glyphicon-user" aria-hidden="true"></span> Cadastrar Pessoa
					</button>	
				</div>
				<div class="btn-group" role="group" aria-label="">
					<button type="button" class="btn btn-default" onClick="location.href='listapessoas'">
						<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span> Listar Pessoas
					</button>	
				</div>
			</form>
	  	</div>
	</div>
      
    <!-- LISTAGEM -->
    <c:if test="${not empty listapessoas}">
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
							<c:forEach items="${listapessoas}" var="pessoa">
								<li class="list-group-item">				
									&nbsp;&nbsp; <a href="javascript:carregarAcoes('consultar',${pessoa.id},'cadastropessoa');" title="Consultar"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a>
									&nbsp;&nbsp; <a href="javascript:carregarAcoes('alterar',${pessoa.id},'cadastropessoa');" title="Alterar"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></a> 
									&nbsp;&nbsp; <a href="javascript:carregarAcoes('excluir',${pessoa.id},'cadastropessoa');" title="Excluir"><span class="glyphicon glyphicon-remove-circle" aria-hidden="true"></span></a>
									&nbsp;&nbsp;&nbsp;&nbsp;
									<c:out value="${pessoa.nome}" /> - 
									<c:out value="${pessoa.email}" /> - 
									<c:out value="${pessoa.perfil}" />							
								</li>
							</c:forEach>
						</form>
					</div>
				</div>
		  	</div>
		</div>
	</c:if>

	<jsp:include page = "comum/footer.jsp" />

</body>
</html>