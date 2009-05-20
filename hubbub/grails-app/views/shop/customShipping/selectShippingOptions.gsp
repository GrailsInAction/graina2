<html>
    <head>
        <title>Shipping Options</title>
        <meta content="main" name="layout"/>

    </head>
<body>

<g:form action="customShipping">
    <dl>
        <dt>Shipping Options</dt>
        <dd><g:textField name="shippingOptions" value="${sc?.shippingOptions}"/></dd>
    </dl>

    <g:submitButton name="next" value="Next"/>
    <g:submitButton name="previous" value="Previous"/>
</g:form>


</body>
</html>
