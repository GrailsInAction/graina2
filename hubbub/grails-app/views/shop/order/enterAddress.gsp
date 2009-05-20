<html>
    <head>
        <title>Shop for Official Merchandise</title>
        <meta content="main" name="layout"/>
        <style type="text/css">
            form dd {
                text-align: left;
                margin-left: 4em;
                padding-left: 4em;
            }
        </style>
    </head>
<body>

     Enter Address
     <g:form action="order">
         <dl>
             <dt>Address</dt>
             <dd></dd>
             <dt>State</dt>
             <dd></dd>
             <dt>Postcode</dt>
             <dd></dd>
             <dt>Custom Shipping?</dt>
             <dd><g:checkBox name="customShipping" value="${sc?.customShipping}"/></dd>
         </dl>
         
        <g:submitButton name="next" value="Next"/>
        <g:submitButton name="previous" value="Previous"/> 
     </g:form>

</body>
</html>
