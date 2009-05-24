
<html>
<head>
   <title>Legacy DB Browser</title>
	<g:javascript library="prototype"/>
	<style>
		dt {
			float: left;
			margin-right: 1em;
		}
		body {
			font-family: "Trebuchet MS", Arial, sans-serif;
			font-size: smaller;
		}
		div {
			margin-top: 1em;
		}
	</style>
</head>
<body>
    <div>${flash.message}</div>
    <g:hasErrors>
        <g:eachError><p>${it.defaultMessage}</p></g:eachError>
    </g:hasErrors>

    <fieldset>
        <legend>Browse Branches</legend>
    <g:form action="showDetails">
        
        <label for="id">Branch:</label> <g:select name="id" from="${com.grailsinaction.legacy.db.Branch.list()}" optionKey="name" optionValue="name"/>
        
        <g:submitToRemote value="Show Details" update="details" url="[action: 'showDetails']"/>
    </g:form>
        </fieldset>

		<div id="details">
			
		</div>

</body>



</html>