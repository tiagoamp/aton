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
	      <p class="lead">Livros</p>      
	    </div>
	    
	    <strong>
	    	<form:errors path="book.*" element="div" cssClass="alert alert-danger error" />
	    </strong>
	    
	    <!-- MENSAGENS -->
	    <jsp:include page = "../comum/messages.jsp" />
	        
	    <div class="panel panel-default">
			<div class="panel-heading">
		    	<h3 class="panel-title">Cadastro de Livros</h3>
		  	</div>
		  	<div class="panel-body">
		  		<form:form id="formCadLivros" method="POST" cssClass="form-horizontal" action="cadastro" modelAttribute="book">
		  			<form:hidden path="id" />
		  			<div class="form-group">
					    <label for="tIsbn" class="col-sm-2 control-label">ISBN</label>
					    <div class="col-sm-3">
					      <form:input path="isbn" cssClass="form-control required" id="tIsbn" placeholder="Digite o isbn igual ao registrado no livro" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />
					    </div>
					    <c:if test="${acao eq 'consultar' or acao eq 'excluir'}">
							<label for="tDateOfRegistration" class="col-sm-2 control-label">Data do cadastro</label>
							<div class="col-sm-3">
								<form:input path="dateOfRegistration" cssClass="form-control required" id="tDateOfRegistration" readonly="true" />			
							</div>
						</c:if>
					</div>
					<div class="form-group">
				    	<label for="tTitle" class="col-sm-2 control-label">Título</label>
				    	<div class="col-sm-8">
				    		<form:input path="title" cssClass="form-control required" id="tTitle" placeholder="Digite o título do livro" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />
				    	</div>
		  			</div>
		  			<div class="form-group">
		  				<label for="tSubtitle" class="col-sm-2 control-label">Sub-Título</label>
		  				<div class="col-sm-8">
		  					<form:input path="subtitle" cssClass="form-control required" id="tSubtitle" placeholder="Digite o sub-título do livro, se houver" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />						
						</div>
					</div>  
					<div class="form-group">
		  				<label for="tAuthor" class="col-sm-2 control-label">Autor(es)</label>
						<div class="col-sm-8">
							<form:input path="authorsNameInline" cssClass="form-control required" id="tAuthor" placeholder="Digite o(s) autor(es) separados por ';' (ponto-e-vírgula)" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />						
						</div>
					</div>
					<div class="form-group">
		  				<label for="tPublishing" class="col-sm-2 control-label">Editora</label>
						<div class="col-sm-3">
							<form:input path="publishingCompany" cssClass="form-control required" id="tPublishing" placeholder="Digite a editora do livro" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />			
						</div>
						<label for="tPublishingCity" class="col-sm-2 control-label">Local de Publicação</label>
						<div class="col-sm-3">
							<form:input path="publishingCity" cssClass="form-control required" id="tPublishingCity" placeholder="Digite o local de publicação do livro" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />			
						</div>
					</div>					
					<div class="form-group">
		  				<label for="tPublishingYear" class="col-sm-2 control-label">Ano de Publicação</label>
						<div class="col-sm-3">
							<form:input path="publishingYear" cssClass="form-control required" id="tPublishingYear" placeholder="Digite o ano de publicação do livro" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />			
						</div>
						<label for="tNumberOfPages" class="col-sm-2 control-label">Número de Páginas</label>
						<div class="col-sm-3">
							<form:input path="numberOfPages" cssClass="form-control required" id="tNumberOfPages" placeholder="Digite o nro de páginas do livro" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />			
						</div>
					</div>
					<div class="form-group">
		  				<label for="tGenre" class="col-sm-2 control-label">Gênero</label>
						<div class="col-sm-3">
							<form:input path="genre" cssClass="form-control required" id="tGenre" placeholder="Digite o gênero do livro" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />			
						</div>
						<label for="tClassification" class="col-sm-2 control-label">Classificação</label>
						<div class="col-sm-3">
							<form:input path="classification" cssClass="form-control required" id="tClassification" placeholder="Digite a classificação do livro" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />			
						</div>
					</div>
					<div class="form-group">
		  				<label for="tTargetAudience" class="col-sm-2 control-label">Público Alvo</label>
						<div class="col-sm-3">
							<form:input path="targetAudience" cssClass="form-control required" id="tTargetAudience" placeholder="Digite o público alvo do livro" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />			
						</div>
						<label for="tDateOfAcquisition" class="col-sm-2 control-label">Data de Aquisição</label>
						<div class="col-sm-3">
							<form:input path="dateOfAcquisition" cssClass="form-control required" id="tDateOfAcquisition" placeholder="Digite a data de aquisição (formato 'dd/mm/aaaa')" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />			
						</div>
					</div>
					<div class="form-group">
		  				<label for="tNumberOfCopies" class="col-sm-2 control-label">Exemplares</label>
						<div class="col-sm-3">
							<form:input path="numberOfCopies" cssClass="form-control required" id="tNumberOfCopies" placeholder="Digite a quantidade de exemplares do livro" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />			
						</div>
						<c:if test="${acao eq 'consultar' or acao eq 'excluir' or acao eq 'alterar'}">
							<label for="tNumberAvailable" class="col-sm-2 control-label">Disponível</label>
							<div class="col-sm-3">
								<form:input path="numberAvailable" cssClass="form-control required" id="tNumberAvailable" placeholder="Digite a quantidade de exemplares disponíveis" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />			
							</div>
						</c:if>
					</div>
					<div class="form-group">
					    <label for="tTypeOfAcquisition" class="col-sm-2 control-label">Tipo de Aquisição</label>
					    <div class="col-sm-8">
					      <div class="radio">
							  <label>
							    <form:radiobutton path="typeOfAcquisition" value="DOACAO" disabled="${acao eq 'consultar'}" /> DOAÇÃO
							  </label>
						  </div>
						  <div class="radio">
							  <label>
							    <form:radiobutton path="typeOfAcquisition" value="COMPRA" disabled="${acao eq 'consultar'}" /> COMPRA
							  </label>
						  </div>
					    </div>
					</div>
					<div class="form-group" id="divDoador">
		  				<label for="tDonorName" class="col-sm-2 control-label">Nome do Doador</label>
						<div class="col-sm-8">
							<form:input path="donorName" cssClass="form-control required" id="tDonorName" placeholder="Digite o nome do doador do livro, se for o caso" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />						
						</div>
					</div>
					<div class="form-group">
		  				<label for="tComments" class="col-sm-2 control-label">Observações</label>
						<div class="col-sm-8">
							<form:input path="comments" cssClass="form-control required" id="tComments" placeholder="Digite alguma informaçao complementar do livro, se houver" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />						
						</div>
					</div>
													
					<!-- BOTOES -->
					<c:if test="${acao != 'consultar' && acao != 'excluir'}">
						<div class="form-group">
						    <div class="col-sm-offset-2 col-sm-10">
						      <button type="submit" class="btn btn-default">
									<span class="glyphicon glyphicon-ok"></span> Salvar
							  </button>
							  <button type="button" class="btn btn-default" onClick="location.href='../livros'">
									<span class="glyphicon glyphicon-remove"></span> Cancelar
							  </button>
						    </div>
						</div>						
					</c:if>
					
					<c:if test="${acao eq 'excluir'}">
						<input type="hidden" id ="exclusao" name="exclusao" value="exclusao">
						<div class="panel panel-default" style="margin: 10px;">
				    		<div class="panel-body" align="center">
				    			<button type="submit" class="btn btn-default btn-lg">
									<span class="glyphicon glyphicon-trash"></span> Excluir
								</button>
								<button type="button" class="btn btn-default btn-lg" onClick="location.href='../livros'">
									<span class="glyphicon glyphicon-remove"></span> Cancelar
								</button>
							</div>
						</div>
					</c:if>
							
				</form:form>
				
				<c:if test="${acao eq 'consultar'}">
					<form id="formAcoesEmprestar" action="emprestimolivro" method="POST">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="button" class="btn btn-default" onClick="location.href='../livros'">
								<span class="glyphicon glyphicon-triangle-left"></span> Voltar
							</button>
							<c:if test="${book.status == 'DISPONIVEL'}">
								<input type="hidden" name="identificador" value="${book.id}" />
								<input type="hidden" name="acao" value="emprestar" />
								<button type="submit" class="btn btn-default" >
									<span class="glyphicon glyphicon-home"></span> Emprestar Livro
								</button>													
							</c:if>
						</div>
					</form>
				</c:if>
										
		  	</div>
		</div>
		
	</div>

	<jsp:include page = "../comum/footer.jsp" />

</body>
</html>