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
	      <p class="lead">Gestão de Empréstimos</p>      
	    </div>
	    
	    <!-- MENSAGENS -->
	    <jsp:include page = "../comum/messages.jsp" />
	    
	    <!-- PESQUISA -->
	    <div class="panel panel-default">
			<div class="panel-heading">
		    	<h3 class="panel-title">Pesquisa de Empréstimos</h3>
		  	</div>
		  	<div class="panel-body">
		  		<form id="formConsultaEmprestimos" class="form-inline" method="POST" action="emprestimos">
		  			<div class="row" align="center">
		  				<div class="col-md-5">
				  			<div class="form-group" >
							    <label for="tLivroSearch">Consulta por Livro:</label>
							    <input type="text" name="tBook" class="form-control" size="40" id="tLivroSearch" placeholder="Digite o título ou autor do livro">
							</div>
							<button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span> Buscar </button>
						</div>
						<div class="col-md-7">					
							<div class="form-group">
							    <label for="tPersonSearch">Consulta por Leitor:</label>
							    <input type="text" name="tPerson" class="form-control" size="40" id="tPersonSearch" placeholder="Digite o nome do leitor">
							</div>
							<button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span> Buscar </button>
						</div>
					</div>								
				</form>	
		  	</div>
		</div>
	    
	   <!-- LISTAGEM -->
	   <div class="panel panel-default">
			<div class="panel-heading">
		    	<h3 class="panel-title">Empréstimos</h3>
		  	</div>
		  	<div class="panel-body">
		  		<div class="row" align="center">
		  			<div class="col-md-6">
			  			<div class="btn-group">
			  				<form id="formListaEmprestimosAbertos" method="POST" action="emprestimos">
			  					<input type="hidden" name="fAbertos" value="abertos" />
								<button type="submit" class="btn btn-default btn-lg">									
									<span class="glyphicon glyphicon-th-list"></span> Listar empréstimos em aberto
								</button>
							</form>							
						</div>
					</div>
					<div class="col-md-6">
						<div class="btn-group">
							<form id="formListaEmprestimos" method="POST" action="emprestimos">								
								<button type="submit" class="btn btn-default btn-lg">
									<span class="glyphicon glyphicon-th-list"></span> Listar todos os empréstimos
								</button>
							</form>	
						</div>
					</div>
		  		</div>		  		
		  	</div>
		</div>
	    	      
	    <!-- LISTAGEM -->
	    <c:if test="${not empty listofborrowing}">
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
								<c:forEach items="${listofborrowing}" var="borrowing">
									<li class="list-group-item">				
										&nbsp;&nbsp;
										<c:if test="${empty borrowing.dateOfReturn}">
											<a href="javascript:carregarAcoes('devolver',${borrowing.id},'devolucaolivro');" title="Devolver"><span class="glyphicon glyphicon-transfer" ></span></a> 
										</c:if>
										<c:if test="${not empty borrowing.dateOfReturn}">
											<span class="glyphicon glyphicon-transfer"></span> 
										</c:if>									
										&nbsp;&nbsp;&nbsp;&nbsp;
										<strong>Data:</strong> <c:out value="${borrowing.dateOfBorrowing}" /> -
										<strong>Livro:</strong> <c:out value="${borrowing.book.title}" /> - 
										<strong>Leitor:</strong> <c:out value="${borrowing.person.name}" />	-									 
										<strong>Devolução: </strong> Progr: <c:out value="${borrowing.dateOfScheduledReturn}" /> - 
										Realizada: <c:out value="${borrowing.dateOfReturn}" /> - 
										<c:if test="${not borrowing.overdue}">
											<span class="label label-success">Sem atraso</span>
										</c:if>
										<c:if test="${borrowing.overdue}">
											<span class="label label-danger">Atrasado</span>
										</c:if>																		
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