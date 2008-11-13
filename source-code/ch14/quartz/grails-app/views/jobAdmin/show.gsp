<html>
  <head>
	  <title>Job Admin</title>
	  <style>
		div#status {
		 	margin: 1em; 
			padding: 1em; 
			border: 1px solid blue; 
			background: lightblue;	
		}
		body {
			font-family: "Trebuchet MS",Helvetica;
		}
	  </style>
  </head>
  
  <body>
    <h1>Job Admin</h1>

	<g:if test="${status}">
		<div id="status">
			${status}
		</div>
	</g:if>
	
	<g:form action="show">
		<fieldset>
			<legend>Job Admin Operations</legend>
		<label for="operation">Select an operation:</label> 
	   	<g:select id="operation" name="operation" 
			from="${ [
				'pause', 'resume', 
				'pauseGroup', 'resumeGroup',
				'pauseAll', 'resumeAll' 
				] }" />  
		<g:submitButton name="go" value="Go"/>
		</fieldset>
	</g:form>

  </body>
</html>