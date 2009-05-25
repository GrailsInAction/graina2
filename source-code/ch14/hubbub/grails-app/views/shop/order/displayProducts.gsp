<html>
    <head>
        <title>Shop for Official Merchandise</title>
        <meta content="main" name="layout"/>
        
    </head>
<body>

<g:hasErrors bean="${orderDetails}">
    <div class="errors">
        <g:renderErrors bean="${orderDetails}"/>
    </div>
</g:hasErrors>

 <g:form action="order">
    Shirts: <g:textField name="numShirts" value="${orderDetails?.numShirts}"/>
    Hats: <g:textField name="numHats" value="${orderDetails?.numHats}"/>
    <g:submitButton name="next" value="Next"/>
    <g:submitButton name="cancel" value="Finished Shopping"/>
 </g:form>

</body>
</html>
