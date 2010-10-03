<<g:applyLayout name="main">
<body>
  <div id="yui-main">
    <div class="yui-b">
      <g:if test="${flash.message}">
        <div class="flash">${flash.message}</div>
      </g:if>
      <g:layoutBody/>
    </div>
  </div>
</body>
</g:applyLayout>