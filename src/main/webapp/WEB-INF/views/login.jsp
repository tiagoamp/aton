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
	<div class="container">
	
	    <!-- <div>
	      <p class="lead">Autenticação</p>      
	    </div> -->
	    
	    <!-- MENSAGENS -->
	    <jsp:include page = "comum/messages.jsp" />
     
	    <div class="panel panel-default" >
			<div class="panel-heading">
		    	<h3 class="panel-title">Login</h3>
		  	</div>
		  	<div class="panel-body">		  	
		  		<form:form id="formLogin" cssClass="form-horizontal" method="POST" action="efetuarlogin" modelAttribute="person">
		  			<form:hidden path="id" />		  			
		  			<div class="form-group">
					    <label for="tEmail" class="col-sm-2 control-label">Email</label>
					    <div class="col-sm-10">
					      <form:input path="email" cssClass="form-control required" placeholder="Digite o e-mail" id="tEmail"  />
					    </div>
					</div>									  
				    <div class="form-group">
				    	<label for="tSenha" class="col-sm-2 control-label">Senha</label>
				    	<div class="col-sm-10">				      
				      		<form:password id="tSenha" path="password" cssClass="form-control required" placeholder="Digite a senha" />
				    	</div>
				  	</div>				  			  
				  	<div class="form-group">
					    <div class="col-sm-offset-2 col-sm-10">
					      <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-ok" ></span> Entrar</button>
					      <button type="button" class="btn btn-default" onClick="location.href='aton'"><span class="glyphicon glyphicon-remove"></span> Cancelar</button>
					    </div>
				  	</div>				  
				</form:form>		  			
		  	</div>
		</div>
	
	</div>

	<jsp:include page = "comum/footer.jsp" />

</body>
</html>