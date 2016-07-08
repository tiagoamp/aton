<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>

		<jsp:include page = "comum/cabecalho.jsp" />

	</head>
	
<body>

	<jsp:include page = "comum/menu.jsp" />
	
	<!-- Begin page content -->
    <div class="" style="margin: 10px;">
      <p class="lead">Administração</p>      
    </div>
    
    <!-- RELATÓRIOS -->
    <div class="panel panel-default" style="margin: 10px;">
		<div class="panel-heading">
	    	<h3 class="panel-title">Relatórios</h3>
	  	</div>
	  	<div class="panel-body">
	  	 <p> relatórios (total livros, livros em atraso, livros emprestados, livros disponíveis) </p>
	  	 <p> emitir etiquetas para livro </p>
	  	 <p> emitir carteirinha </p>	
	  	</div>
	</div>
		
	<jsp:include page = "comum/rodape.jsp" />

</body>
</html>