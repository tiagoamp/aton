	<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	
	<!-- mensagens -->
    <c:if test="${not empty mensagem}">
    	<c:if test="${mensagem.type == 'ERRO'}">
    		<div class="alert alert-danger" role="alert">
				<strong>								
					<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true" > </span>			
					<c:out value="${mensagem.text}" />
				</strong> 
			</div>
    	</c:if>
		<c:if test="${mensagem.type == 'ALERTA'}">
    		<div class="alert alert-warning" role="alert">
				<strong>								
					<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true" > </span>			
					<c:out value="${mensagem.text}" />
				</strong> 
			</div>
    	</c:if>
    	<c:if test="${mensagem.type == 'INFO'}">
    		<div class="alert alert-info" role="alert">
				<strong>								
					<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true" > </span>			
					<c:out value="${mensagem.text}" />
				</strong> 
			</div>
    	</c:if>
    	<c:if test="${mensagem.type == 'SUCESSO'}">
    		<div class="alert alert-success" role="alert">
				<strong>								
					<span class="glyphicon glyphicon-ok-sign" aria-hidden="true" > </span>			
					<c:out value="${mensagem.text}" />
				</strong> 
			</div>
    	</c:if>
	</c:if>
	<!-- mensagens -->