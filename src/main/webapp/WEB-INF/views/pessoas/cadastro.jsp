<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
	<head>
	
		<jsp:include page = "../comum/header.jsp" />
		
	</head>
	
<body>

	<jsp:include page = "../comum/menu.jsp" />
	
	<!-- Begin page content -->
    <div class="" style="margin: 10px;">
      <p class="lead">Pessoas</p>      
    </div>
    
    <strong>
    	<form:errors path="pessoa.*" element="div" cssClass="alert alert-danger error" />
    </strong>
    
    <!-- MENSAGENS -->
    <jsp:include page = "../comum/messages.jsp" />
        
    <c:if test="${acao eq 'excluir'}">
    	<form id="formExclusao">
    		<input type="hidden" id ="acao" name="acao">
			<input type="hidden" id ="identificador" name="identificador">
    		<div class="panel panel-default" style="margin: 10px;">
	    		<div class="panel-body">
	    			<button type="button" class="btn btn-default" onClick="carregarExclusao('excluir',${pessoa.id},'exclusaopessoa');">
						<span class="glyphicon glyphicon-trash" aria-hidden="true"></span> Excluir
					</button>
					<button type="button" class="btn btn-default" onClick="location.href='pessoas'">
						<span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Cancelar
					</button>
				</div>
			</div>
		</form>
	</c:if>
        
    <div class="panel panel-default" style="margin: 10px;">
		<div class="panel-heading">
	    	<h3 class="panel-title">Cadastro de Pessoas</h3>
	  	</div>
	  	<div class="panel-body">
	  		<form:form id="formCadPessoas" method="POST" cssClass="navbar" action="pessoacadastrada" modelAttribute="pessoa">
	  			<form:hidden path="id" />
	  			<div class="form-group">
    				<div class="input-group">
						<span class="input-group-addon" id="basic-addon1">E-mail</span> 
						<form:input path="email" cssClass="form-control required" placeholder="Digite o e-mail" aria-describedby="basic-addon1" size="50" disabled="${acao eq 'consultar' or acao eq 'excluir'}" />						
					</div>
  				</div>
  				<div class="form-group">
    				<div class="input-group">
						<span class="input-group-addon" id="basic-addon1">Nome</span>
						<form:input path="name" cssClass="form-control required" placeholder="Digite o nome" aria-describedby="basic-addon1" size="50" disabled="${acao eq 'consultar' or acao eq 'excluir'}" />						
					</div>
				</div>  								
				<div class="form-group">
    				<div class="input-group">
						<span class="input-group-addon" id="basic-addon1">Telefone</span> 
						<form:input path="phone" cssClass="form-control required" placeholder="Digite o telefone" aria-describedby="basic-addon1" size="50" disabled="${acao eq 'consultar' or acao eq 'excluir'}"/>
					</div>
				</div>
				<div class="panel panel-default">
					<div class="panel-heading">Perfil</div>
					<div class="panel-body">
						<div class="input-group">
							<span class="input-group-addon"><form:radiobutton path="role" value="LEITOR" disabled="${acao eq 'consultar' or acao eq 'excluir'}"/> LEITOR </span>
							<span class="input-group-addon"><form:radiobutton path="role" value="BIBLIOTECARIO" disabled="${acao eq 'consultar' or acao eq 'excluir'}"/> BIBLIOTECARIO </span>
							<span class="input-group-addon"><form:radiobutton path="role" value="ADMINISTRADOR" disabled="${acao eq 'consultar' or acao eq 'excluir'}"/> ADMINISTRADOR </span>						
						</div><!-- /input-group -->
					</div>
				</div>
				<c:if test="${acao != 'consultar' && acao != 'excluir'}">
					<div id="divSenha" class="form-group" style="display: block;">
						<div class="input-group">
							<span class="input-group-addon" id="basic-addon1">Senha</span> 
							<form:password id="tSenha" path="password" cssClass="form-control required" placeholder="Digite a senha (somente para perfis administrador e bibliotecário)" aria-describedby="basic-addon1" size="50"/>						
						</div>					
					</div>
				</c:if>
				<c:if test="${acao != 'consultar' && acao != 'excluir'}">
					<button type="submit" class="btn btn-default">
						<span class="glyphicon glyphicon-ok" aria-hidden="true"></span> Salvar
					</button>
					<button type="button" class="btn btn-default" onClick="location.href='pessoas'">
						<span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Cancelar
					</button>
				</c:if>
				<c:if test="${acao eq 'consultar'}">
					<button type="button" class="btn btn-default" onClick="location.href='pessoas'">
						<span class="glyphicon glyphicon-triangle-left" aria-hidden="true"></span> Voltar
					</button>
				</c:if>			
			</form:form>	
	  	</div>
	</div>

	<jsp:include page = "../comum/footer.jsp" />

</body>
</html>