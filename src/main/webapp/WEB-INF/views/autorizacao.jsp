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
	
	<div class="margemPadrao">
	
		<!-- Begin page content -->
	    <div>
	      <p class="lead">Autenticação requerida</p>      
	    </div>
	
		<div class="alert alert-danger" role="alert">
			<strong> <span class="glyphicon glyphicon-exclamation-sign"	aria-hidden="true"> </span> 
				Funcionalidade com autenticação requerida no sistema!
			</strong>
		</div>
	
	
		<div class="panel panel-default">
			<div class="panel-heading">
		    	<h3 class="panel-title">Login no sistema</h3>
		  	</div>
		  	<div class="panel-body">
		  		Acesse a página de <a href="login">LOGIN</a> do sistema.	  			
		  	</div>
		</div>
	
	</div>

	<jsp:include page = "comum/footer.jsp" />

</body>
</html>