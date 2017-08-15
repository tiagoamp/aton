<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	
		<jsp:include page = "../comum/subheader.jsp" />
						
	</head>
	
<body>

	<jsp:include page = "../comum/submenu.jsp" />
	
	<div class="margemPadrao">
	
		<!-- Begin page content -->
	    <div>
	      <p class="lead">Pessoas</p>      
	    </div>
	    
	    <strong>
	    	<form:errors path="person.*" element="div" cssClass="alert alert-danger error" />
	    </strong>
	    
	    <!-- MENSAGENS -->
	    <jsp:include page = "../comum/messages.jsp" />
	        
	    <%-- <c:if test="${acao eq 'excluir'}">
	    	<form id="formExclusao" action="exclusao" method="post">
	    		<!-- <input type="hidden" id ="acao" name="acao" value="excluir"> -->
				<input type="hidden" id ="identificador" name="identificador" value="${person.id}">
	    		<div class="panel panel-default" style="margin: 10px;">
		    		<div class="panel-body" align="center">
		    			<button type="button" class="btn btn-default btn-lg">
							<span class="glyphicon glyphicon-trash"></span> Excluir
						</button>
						<button type="button" class="btn btn-default btn-lg" onClick="location.href='../pessoas'">
							<span class="glyphicon glyphicon-remove"></span> Cancelar
						</button>
					</div>
				</div>
			</form>
		</c:if> --%>
	        
	    <div class="panel panel-default">
			<div class="panel-heading">
		    	<h3 class="panel-title">Cadastro de Pessoas</h3>
		  	</div>
		  	<div class="panel-body">
		  		<form:form id="formCadPessoas" method="POST" cssClass="form-horizontal" action="cadastro" modelAttribute="person">
		  			<form:hidden path="id" />
		  			<div class="form-group">
					    <label for="tEmail" class="col-sm-2 control-label">E-mail</label>
					    <div class="col-sm-10">
					      <form:input path="email" cssClass="form-control required" id="tEmail" placeholder="Digite o e-mail" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />
					    </div>
					</div>
					<div class="form-group">
					    <label for="tNome" class="col-sm-2 control-label">Nome</label>
					    <div class="col-sm-10">
					      <form:input path="name" cssClass="form-control required" id="tNome" placeholder="Digite o nome" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />
					    </div>
					</div>
					<div class="form-group">
					    <label for="tNome" class="col-sm-2 control-label">Telefone</label>
					    <div class="col-sm-10">
					      <form:input path="phone" cssClass="form-control" id="tTelefone" placeholder="Digite o telefone" readonly="${acao eq 'consultar' or acao eq 'excluir'}" />
					    </div>
					</div>
					<div class="form-group">
					    <label for="tPerfil" class="col-sm-2 control-label">Perfil</label>
					    <div class="col-sm-10">
					      <div class="radio">
							  <label>
							    <form:radiobutton path="role" value="LEITOR" disabled="${acao eq 'consultar'}" /> LEITOR
							  </label>
						  </div>
						  <div class="radio">
							  <label>
							    <form:radiobutton path="role" value="BIBLIOTECARIO" disabled="${acao eq 'consultar'}"/> BIBLIOTECÁRIO
							  </label>
						  </div>
						  <div class="radio">
							  <label>
							    <form:radiobutton path="role" value="ADMINISTRADOR" disabled="${acao eq 'consultar'}"/> ADMINISTRADOR
							  </label>
						  </div>
					    </div>
					</div>
		  			
					<c:if test="${acao != 'consultar' && acao != 'excluir'}">
						<div class="form-group" id="divSenha">
						    <label for="tSenha" class="col-sm-2 control-label">Senha</label>
						    <div class="col-sm-10">
						      <form:password id="tSenha" path="password" cssClass="form-control required" placeholder="Digite a senha (somente para perfis administrador e bibliotecário)" />
						    </div>
						</div>						
					</c:if>
					
					<c:if test="${acao != 'consultar' && acao != 'excluir'}">
						<div class="form-group">
						    <div class="col-sm-offset-2 col-sm-10">
						      <button type="submit" class="btn btn-default">
									<span class="glyphicon glyphicon-ok"></span> Salvar
							  </button>
							  <button type="button" class="btn btn-default" onClick="location.href='../pessoas'">
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
								<button type="button" class="btn btn-default btn-lg" onClick="location.href='../pessoas'">
									<span class="glyphicon glyphicon-remove"></span> Cancelar
								</button>
							</div>
						</div>
					</c:if>
					
					<c:if test="${acao eq 'consultar'}">
						<button type="button" class="btn btn-default" onClick="location.href='../pessoas'">
							<span class="glyphicon glyphicon-triangle-left"></span> Voltar
						</button>
					</c:if>	
							
				</form:form>
					
		  	</div>
		</div>

	</div>

	<jsp:include page = "../comum/footer.jsp" />

</body>
</html>