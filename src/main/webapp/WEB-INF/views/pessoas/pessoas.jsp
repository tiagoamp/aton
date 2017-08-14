<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>

		<jsp:include page = "../comum/header.jsp" />

	</head>
	
<body>

	<jsp:include page = "../comum/menu.jsp" />
	
	<div class="margemPadrao">
	
		<!-- Begin page content -->
	    <div>
	      <p class="lead"><strong>Gestão de Pessoas</strong></p>      
	    </div>
	    
	    <!-- MENSAGENS -->
	    <jsp:include page = "../comum/messages.jsp" />
	    
	    <!-- PESQUISA -->
	    <div class="panel panel-default">
			<div class="panel-heading">
		    	<h3 class="panel-title">Pesquisa de Pessoas</h3>
		  	</div>
		  	<div class="panel-body">
		  		<form id="formConsultaPessoas" class="form-inline" method="POST" action="pessoas">
		  			<div class="row" align="center">
		  				<div class="col-md-5">
				  			<div class="form-group" >
							    <label for="tEmailSearch">Consulta por E-mail:</label>
							    <input type="text" name="tEmail" class="form-control" size="40" id="tEmailSearch" placeholder="Digite o e-mail do leitor">
							</div>
							<button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span> Buscar </button>
						</div>
						<div class="col-md-7">					
							<div class="form-group">
							    <label for="tFieldsSearch">Consulta por Dados:</label>
							    <input type="text" name="tFields" class="form-control" size="40" id="tFieldsSearch" placeholder="Digite o nome ou perfil da pessoa">
							</div>
							<button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span> Buscar </button>
						</div>
					</div>								
				</form>	
		  	</div>
		</div>
		
		<!-- INCLUSÃO -->
	    <div class="panel panel-default">
			<div class="panel-heading">
		    	<h3 class="panel-title">Cadastramento de Pessoas</h3>
		  	</div>
		  	<div class="panel-body">
		  		<div class="row" align="center">
		  			<div class="col-md-6">
			  			<div class="btn-group">
							<button type="button" class="btn btn-default btn-lg" onClick="location.href='pessoas/cadastro'">
								<span class="glyphicon glyphicon-user" aria-hidden="true"></span> Cadastrar Pessoa
							</button>							
						</div>
					</div>
					<div class="col-md-6">
						<div class="btn-group">
							<form id="formListaPessoas" method="POST" action="pessoas">								
								<button type="submit" class="btn btn-default btn-lg">
									<span class="glyphicon glyphicon-th-list"></span> Listar Pessoas
								</button>
							</form>	
						</div>
					</div>
		  		</div>		  		
		  	</div>
		</div>
	      
	    <!-- LISTAGEM -->
	    <c:if test="${not empty listofpeople}">
		    <div class="panel panel-default">
				<div class="panel-heading">
			    	<h3 class="panel-title">Resultado da Pesquisa</h3>
			  	</div>
			  	<div class="panel-body">
			  		<div class="form-group">
						<div class="list-group">
							<form id="formAcoes">
								<input type="hidden" id ="acao" name="acao">
								<input type="hidden" id ="identificador" name="identificador">
								<c:forEach items="${listofpeople}" var="person">
									<li class="list-group-item">				
										&nbsp;&nbsp; <a href="javascript:carregarAcoes('consultar',${pessoa.id},'cadastropessoa');" title="Consultar"><span class="glyphicon glyphicon-eye-open"></span></a>
										&nbsp;&nbsp; <a href="javascript:carregarAcoes('alterar',${pessoa.id},'cadastropessoa');" title="Alterar"><span class="glyphicon glyphicon-edit"></span></a> 
										&nbsp;&nbsp; <a href="javascript:carregarAcoes('excluir',${pessoa.id},'cadastropessoa');" title="Excluir"><span class="glyphicon glyphicon-remove-circle"></span></a>
										&nbsp;&nbsp;&nbsp;&nbsp;
										<c:out value="${person.name}" /> - 
										<c:out value="${person.email}" /> - 
										<c:out value="${person.role}" />							
									</li>
								</c:forEach>
							</form>
						</div>
					</div>
			  	</div>
			</div>
		</c:if>
	
	</div>
	
	<jsp:include page = "../comum/footer.jsp" />

</body>
</html>