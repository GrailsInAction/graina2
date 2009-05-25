<html>
    <head>
        <title>Shipping Type</title>
        <meta content="main" name="layout"/>

    </head>
<body>

<g:form action="customShipping">
    <dl>
        <dt>Shipping Type</dt> 
        <dd><g:textField name="shippingType" value="${sc?.shippingType}"/></dd>
    </dl>

    <g:submitButton name="next" value="Next"/>
    <g:submitButton name="standardShipping" value="Standard Shipping"/>
    <g:submitButton name="previous" value="Previous"/>
</g:form>

</body>
</html>
