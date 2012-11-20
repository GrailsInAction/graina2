<html>
    <head>
        <title>Shop for Official Merchandise</title>
        <meta content="hubbub" name="layout"/>
        <style type="text/css">
            form dd {
                text-align: left;
                margin-left: 4em;
                padding-left: 4em;
            }
        </style>
    </head>
<body>

<g:hasErrors bean="${pc}">
    <div class="errors">
        <g:renderErrors bean="${pc}"/>
    </div>
</g:hasErrors>

     Enter Payment
     <g:form action="order">
         <dl>
         <dt>Credit Card:</dt>
           <!-- Sample Dummy Card: 4417123456789113 -->
         <dd><g:textField name="cardNumber" value="${pc?.cardNumber}"/> (eg. 4417123456789113)</dd>
         <dt>Name:</dt>
         <dd><g:textField name="name" value="${pc?.name}"/> </dd>
         <dt>Expiry: (xx/yy)</dt>
         <dd><g:textField name="expiry" value="${pc?.expiry}"/></dd>
         </dl>
        <g:submitButton name="next" value="Next"/>
        <g:submitButton name="previous" value="Previous"/>
     </g:form>

</body>
</html>