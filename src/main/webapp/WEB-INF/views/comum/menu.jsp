	<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

	<nav class="navbar navbar-default">
	  	<div class="container-fluid">
	    	<!-- Brand and toggle get grouped for better mobile display -->
	    	<div class="navbar-header">
	      		<!-- <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
	        		<span class="sr-only">Toggle navigation</span>
	        		<span class="icon-bar"></span>	        		
	      		</button> -->
	      		<a class="navbar-brand" href="aton.jsp"><img alt="aton logo" src="resources/images/aton.png" width="60" height="30" /></a>
	    	</div>
	
	    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
	      <ul class="nav navbar-nav">
	      	<li><a href="aton">Aton</a></li>
	      	<li><a href="livros">Livros</a></li>
	        <li><a href="pessoas">Pessoas</a></li>
	        <li><a href="emprestimos">Emprestimos</a></li>
	        <li><a href="sobre">Sobre</a></li>	        
	      </ul>
	      <ul class="nav navbar-nav navbar-right">
	      	<li><a href="#"></a></li>	
	      	<c:if test="${empty usuario}"> 
	      		<li><a href="login">Login</a></li>
	      	</c:if>       
	        <li><a href="logout">Logout</a></li>	        
	      </ul>
	      
	      <c:if test="${empty usuario}">
	      		<p class="navbar-text navbar-right"><a href="#" class="navbar-link">Usuário não autenticado</a></p>
	      </c:if>
	      <c:if test="${not empty usuario}">
	      		<p class="navbar-text navbar-right"><a href="#" class="navbar-link"> ${usuario.name} </a></p>
	   	  </c:if>
	    </div><!-- /.navbar-collapse -->
	  </div><!-- /.container-fluid -->
	</nav>