<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
	<head>
		<jsp:include page = "../comum/subheader.jsp" />		
	</head>
	
<body>

	<jsp:include page = "../comum/submenu.jsp" />
	
	<div class="margemPadrao">
	
		<div>
	      <p class="lead">Efetuar Empréstimo</p>      
	    </div>
	    
	    <!-- MENSAGENS -->
	    <jsp:include page = "../comum/messages.jsp" />
	        
	    <div class="panel panel-default">
			<!-- <div class="panel-heading">
		    	<h3 class="panel-title">Empréstimo de Livros</h3>
		  	</div> -->
		  	<div class="panel-body">	  		
		  					  								
						<div class="row margemPadrao">
						
							<!-- BOX DE LIVRO -->
								<div class="panel panel-default">
								  <div class="panel-heading">
								    <h3 class="panel-title"><strong>Livro selecionado</strong></h3>
								  </div>
								  <div class="panel-body form-horizontal">
								    	<div class="form-group">
										    <label for="tIsbn" class="col-sm-2 control-label">ISBN</label>
										    <div class="col-sm-3">
										      <form:input path="borrowing.book.isbn" cssClass="form-control required" id="tIsbn" readonly="true" />
										    </div>										    
										</div>
										<div class="form-group">
									    	<label for="tTitle" class="col-sm-2 control-label">Título</label>
									    	<div class="col-sm-8">
									    		<form:input path="borrowing.book.title" cssClass="form-control required" id="tTitle" readonly="true" />
									    	</div>
							  			</div>
							  			<div class="form-group">
							  				<label for="tSubtitle" class="col-sm-2 control-label">Sub-Título</label>
							  				<div class="col-sm-8">
							  					<form:input path="borrowing.book.subtitle" cssClass="form-control required" id="tSubtitle" readonly="true" />						
											</div>
										</div>  
										<div class="form-group">
							  				<label for="tAuthor" class="col-sm-2 control-label">Autor(es)</label>
											<div class="col-sm-8">
												<form:input path="borrowing.book.authorsNameInline" cssClass="form-control required" id="tAuthor" readonly="true" />						
											</div>
										</div>
								  </div>
								</div>						
							
						</div>
						
						<div class="row margemPadrao">
							<!-- BOX DE PESSOA/LEITOR -->
							<div>
								<div class="panel panel-default">
								  <div class="panel-heading">
								    <h3 class="panel-title"><strong>Seleção de Leitor</strong></h3>
								  </div>
								  <div class="panel-body">
								    	<form id="formConsultaPessoas" class="form-inline" method="POST" action="../emprestimos/consultapessoaemprestimo" role="search">
											<input type="hidden" id ="tIdBook" name="tIdBook" value=${borrowing.book.id}>
											<div class="row col-md-6" align="center">								  				
										  		<div class="form-group" >
												    <label for="tEmailSearch">Consulta por E-mail:</label>
												    <input type="text" name="tEmail" class="form-control" size="40" id="tEmailSearch" placeholder="Digite o e-mail do leitor">
												</div>
												<button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span> Buscar </button>
											</div>		
											<p/>
											<div class="row col-md-6" align="center">					
												<div class="form-group">
												    <label for="tFieldsSearch">Consulta por Dados:</label>
												    <input type="text" name="tFields" class="form-control" size="40" id="tFieldsSearch" placeholder="Digite o nome ou perfil da pessoa">
												</div>
												<button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span> Buscar </button>
											</div>											 
										</form>
										
										
										<!-- LISTAGEM -->
									    <c:if test="${not empty listofpeople}">
									    	<p/><br/><p/><br/>
									    	<div class="row margemPadrao">
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
																	<input type="hidden" id ="idLivro" name="idLivro" value=${borrowing.book.id}>
																	<c:forEach items="${listofpeople}" var="person">
																		<li class="list-group-item">				
																			&nbsp;&nbsp; <a href="javascript:carregarAcoes('selecionar',${person.id},'emprestimoselecionarpessoa');" title="Selecionar"><span class="glyphicon glyphicon-open" aria-hidden="true"></span></a>
																			&nbsp;&nbsp;&nbsp;&nbsp;
																			<c:out value="${person.name}" /> - 
																			<c:out value="${person.email}" />							
																		</li>
																	</c:forEach>
																</form>
															</div>
														</div>
												  	</div>
												</div>
											</div>
										</c:if>
								  </div>
								</div>
														
							</div>
						</div> <!-- div row -->
						
					<!-- BOX DE EMPRESTIMO -->
					<strong> 
						<form:errors path="borrowing.*" element="div" cssClass="alert alert-danger error" />
					</strong>
					
					<form:form id="formEmpLivros" method="POST" cssClass="form-horizontal margemPadrao" action="livroemprestado" modelAttribute="borrowing">
			  				<form:hidden path="id" />
			  				<form:hidden path="book.id" />
			  				<form:hidden path="person.id" />
			  				<div class="form-group">
						    	<label for="tLeitor" class="col-sm-2 control-label">Leitor selecionado:</label>
						    	<div class="col-sm-8">
						    		<form:input path="person.name" cssClass="form-control required" id="tLeitor" placeholder="Selecione o leitor" readonly="true" />
						    	</div>
				  			</div>
		  					<div class="form-group">
				  				<label for="tDateBorrow" class="col-sm-2 control-label">Data do Empréstimo</label>
								<div class="col-sm-3">
									<form:input path="dateOfBorrowing" cssClass="form-control required" id="tDateBorrow" placeholder="Digite a data do empréstimo" />			
								</div>
							</div>
							<div class="form-group">
								<label for="tDateReturn" class="col-sm-2 control-label">Data da Devolução prevista</label>
								<div class="col-sm-3">
									<form:input path="dateOfScheduledReturn" cssClass="form-control required" id="tDateReturn" placeholder="Digite a data da devolução" />			
								</div>
							</div>
			  										
							<!-- BOTOES -->
							<div class="row" align="center">
								<button type="submit" class="btn btn-default">
									<span class="glyphicon glyphicon-ok"></span> Emprestar
								</button>
								<button type="button" class="btn btn-default" onClick="location.href='../livros'">
									<span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Cancelar
								</button>
							</div>
													
						</form:form>
												
		  	</div>
		</div>

	</div>

	<jsp:include page = "../comum/footer.jsp" />

</body>
</html>