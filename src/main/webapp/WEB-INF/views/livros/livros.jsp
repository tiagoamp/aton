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
	
		<div>
	      <p class="lead"><strong>Gestão de Livros</strong></p>      
	    </div>
	    
	    <!-- MENSAGENS -->
	    <jsp:include page = "../comum/messages.jsp" />
	    
	    <!-- PESQUISA -->
	    <div class="panel panel-default">
			<div class="panel-heading">
		    	<h3 class="panel-title">Pesquisa de Livros</h3>
		  	</div>
		  	<div class="panel-body">
		  		<form id="formConsultaLivros" class="form-inline" method="POST" action="livros">
		  			<div class="row" align="center">
		  				<div class="col-md-5">
				  			<div class="form-group" >
							    <label for="tIsbnSearch">Consulta por ISBN:</label>
							    <input type="text" name="tISBN" class="form-control" size="40" id="tIsbnSearch" placeholder="Digite o ISBN do livro">
							</div>
							<button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span> Buscar </button>
						</div>
						<div class="col-md-7">					
							<div class="form-group">
							    <label for="tFieldsSearch">Consulta por Dados:</label>
							    <input type="text" name="tFields" class="form-control" size="40" id="tFieldsSearch" placeholder="Digite Título ou Autor do livro">
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
		    	<h3 class="panel-title">Cadastramento de Livros</h3>
		  	</div>
		  	<div class="panel-body">
		  		<div class="row" align="center">
		  			<div class="col-md-6">
			  			<div class="btn-group">
							<button type="button" class="btn btn-default btn-lg" onClick="location.href='livros/cadastro'">
								<span class="glyphicon glyphicon-user" aria-hidden="true"></span> Cadastrar Livro
							</button>							
						</div>
					</div>
					<div class="col-md-6">
						<div class="btn-group">
							<form id="formListaLivros" method="POST" action="livros">								
								<button type="submit" class="btn btn-default btn-lg">
									<span class="glyphicon glyphicon-th-list"></span> Listar Livros
								</button>
							</form>	
						</div>
					</div>
		  		</div>		  		
		  	</div>
		</div>
	    	      
	    <!-- LISTAGEM -->
	    <c:if test="${not empty listofbooks}">
		    <div class="panel panel-default" style="margin: 10px;">
				<div class="panel-heading">
			    	<h3 class="panel-title">Resultado da Pesquisa</h3>
			  	</div>
			  	<div class="panel-body">
			  		<div class="form-group">
						<div class="list-group">
							<form id="formAcoes">								
								<c:forEach items="${listofbooks}" var="book">
									<li class="list-group-item">
										&nbsp;&nbsp; <a href="livros/cadastro?acao=consultar&identificador=${livro.id}" title="Consultar"><span class="glyphicon glyphicon-eye-open"></span></a>
										&nbsp;&nbsp; <a href="livros/cadastro?acao=alterar&identificador=${livro.id}" title="Alterar"><span class="glyphicon glyphicon-edit"></span></a> 
										&nbsp;&nbsp; <a href="livros/cadastro?acao=excluir&identificador=${livro.id}" title="Excluir"><span class="glyphicon glyphicon-remove-circle"></span></a>
										&nbsp;&nbsp;&nbsp;&nbsp;
											
										<c:out value="${book.isbn}" /> - 
										<c:out value="${book.titulo}" /> <%-- - 
										<c:out value="${book.autoresAgrupados}" /> - --%>
										<c:if test="${book.status == 'DISPONIVEL'}">
											<span class="label label-success"><c:out value="${book.status}" /></span>
										</c:if>
										<c:if test="${book.status != 'DISPONIVEL'}">
											<span class="label label-danger"><c:out value="${book.status}" /></span>
										</c:if>
										<!-- aparece na ordem inversa os badges -->
										<span class="badge"><c:out value="${book.numberOfCopies}" /></span> 
										<span class="badge"><c:out value="${book.numberAvailable}" /></span>
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