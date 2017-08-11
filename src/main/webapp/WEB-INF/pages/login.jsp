<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
	<head>
	
		<jsp:include page = "comum/header.jsp" />
		
	</head>
	
<body>

	<jsp:include page = "comum/menu.jsp" />
	
	<!-- Begin page content -->
    <div class="" style="margin: 10px;">
      <p class="lead">Login</p>      
    </div>
    
    <!-- MENSAGENS -->
    <jsp:include page = "comum/messages.jsp" />
        
     
    <div class="panel panel-default" style="margin: 10px;">
		<div class="panel-heading">
	    	<h3 class="panel-title">Login no sistema</h3>
	  	</div>
	  	<div class="panel-body">
	  		<form:form id="formLogin" method="POST" cssClass="navbar" action="efetuarlogin" modelAttribute="person">
	  			<form:hidden path="id" />
	  			<div class="form-group">
    				<div class="input-group">
						<span class="input-group-addon" id="basic-addon1">E-mail</span> 
						<form:input path="email" cssClass="form-control required" placeholder="Digite o e-mail" aria-describedby="basic-addon1" size="40" />						
					</div>
  				</div>
  				<div id="divSenha" class="form-group" style="display: block;">
					<div class="input-group">
						<span class="input-group-addon" id="basic-addon1">Senha</span> 
						<form:password id="tSenha" path="password" cssClass="form-control required" placeholder="Digite a senha" aria-describedby="basic-addon1" size="40"/>						
					</div>					
				</div>
				<button type="submit" class="btn btn-default">
					<span class="glyphicon glyphicon-ok" aria-hidden="true"></span> Entrar
				</button>
				<button type="button" class="btn btn-default" onClick="location.href='aton'">
					<span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Cancelar
				</button>
										
			</form:form>	
	  	</div>
	</div>

	<jsp:include page = "comum/footer.jsp" />

</body>
</html>