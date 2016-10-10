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
      <p class="lead">Empréstimo</p>      
    </div>
    
    <!-- MENSAGENS -->
    <jsp:include page = "../comum/mensagens.jsp" />
        
    <div class="panel panel-default" style="margin: 10px;">
		<div class="panel-heading">
	    	<h3 class="panel-title">Empréstimo de Livros</h3>
	  	</div>
	  	<div class="panel-body">	  		
	  			<div class="panel panel-default">
	  								
					<div class="row">
					
						<!-- BOX DE LIVRO -->
						<div class="col-sm-6 col-md-6">
							<div class="thumbnail">
								<img src="${emprestimo.livro.pathFotoCapa}" alt="[Sem capa cadastrada]" width="180px" height="200px">
								<div class="caption">
									<h4>Título: ${emprestimo.livro.titulo}</h4>
									<p>${emprestimo.livro.subtitulo}</p>
									<p>ISBN: ${emprestimo.livro.isbn}</p>
									<p>Autores: ${emprestimo.livro.autoresAgrupados}</p>
								</div>
							</div>
						</div>
						
						<!-- BOX DE PESSOA/LEITOR -->
						<div class="col-sm-6 col-md-6">
							<div class="thumbnail">
								<div class="caption">
									<div class="panel-heading">
										<h3 class="panel-title">Pesquisa de Leitor</h3>
									</div>			
									<form id="formConsultaPessoas" class="navbar-form navbar-left" method="POST" action="consultapessoaemprestimo" role="search">
										<input type="hidden" id ="tIdLivro" name="tIdLivro" value=${emprestimo.livro.id}>
										<div class="col-lg-12">																		
											<div class="input-group">
												<span class="input-group-addon" id="basic-addon1">Consulta por E-mail</span>
												<input type="text" name="tEmail" class="form-control"	placeholder="Digite o e-mail do leitor"> 
													<span class="input-group-btn">
														<button class="btn btn-default" type="submit"> 
															<span class="glyphicon glyphicon-search" aria-hidden="true"></span> Buscar
														</button>
													</span>
											</div>
										</div>
										&nbsp;&nbsp;&nbsp;&nbsp;
										<div class="col-lg-12">									
											<div class="input-group">
												<span class="input-group-addon" id="basic-addon1">Consulta por Dados</span>
												<input type="text" name="tDados" class="form-control" placeholder="Digite o nome, telefone ou perfil do leitor"> 
													<span class="input-group-btn">
													<button class="btn btn-default" type="submit"> 
														<span class="glyphicon glyphicon-search" aria-hidden="true"></span> Buscar
													</button>
												</span>
											</div>
										</div>
									</form>
									<p/> &nbsp;&nbsp;&nbsp;&nbsp;
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
															<input type="hidden" id ="idLivro" name="idLivro" value=${emprestimo.livro.id}>
															<c:forEach items="${listapessoas}" var="pessoa">
																<li class="list-group-item">				
																	&nbsp;&nbsp; <a href="javascript:carregarAcoes('selecionar',${pessoa.id},'emprestimoselecionarpessoa');" title="Selecionar"><span class="glyphicon glyphicon-open" aria-hidden="true"></span></a>
																	&nbsp;&nbsp;&nbsp;&nbsp;
																	<c:out value="${pessoa.nome}" /> - 
																	<c:out value="${pessoa.email}" />							
																</li>
															</c:forEach>
														</form>
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
					<form:errors path="emprestimo.*" element="div" cssClass="alert alert-danger error" />
				</strong>
				<form:form id="formEmpLivros" method="POST" cssClass="navbar" action="livroemprestado" modelAttribute="emprestimo">
		  				<form:hidden path="id" />
		  				<form:hidden path="livro.id" />
		  				<form:hidden path="pessoa.id" />
		  				<div class="form-group">
							<div class="input-group">
								<span class="input-group-addon" id="basic-addon1">Leitor selecionado</span>
								<form:input path="pessoa.nome" cssClass="form-control required" placeholder="Selecione o leitor" aria-describedby="basic-addon1" size="50" disabled="true" />
							</div>
						</div>
						<div class="form-group">
							<div class="input-group">
								<span class="input-group-addon" id="basic-addon1">Data do Empréstimo</span>
								<form:input path="dataEmprestimoFormatada" cssClass="form-control required" placeholder="Digite a data do emprestimo" aria-describedby="basic-addon1" size="20" disabled="false" />
							</div>
						</div>
						<div class="form-group">
							<div class="input-group">
								<span class="input-group-addon" id="basic-addon1">Data da Devolução Prevista</span>
								<form:input path="dataDevolucaoProgramadaFormatada" cssClass="form-control required" placeholder="Digite a data de devolução" aria-describedby="basic-addon1" size="20" disabled="false" />
							</div>
						</div>
						
						<!-- BOTOES -->
						<button type="submit" class="btn btn-default">
							<span class="glyphicon glyphicon-ok" aria-hidden="true"></span> Emprestar
						</button>
						<button type="button" class="btn btn-default" onClick="location.href='livros'">
							<span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Cancelar
						</button>
												
					</form:form>
					
			</div>					
	  	</div>
	</div>

	<jsp:include page = "../comum/rodape.jsp" />

</body>
</html>