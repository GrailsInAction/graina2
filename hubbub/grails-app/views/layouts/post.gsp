<g:applyLayout name="main">
<head>
  <nav:resources/>
</head>
<body>
  <div id="yui-main">
    <div class="yui-b">
      <nav:render group="tabs"/>
      <g:if test="${flash.message}">
        <div class="flash">${flash.message}</div>
      </g:if>
      <g:layoutBody/>
    </div>
  </div>
  <div class="yui-b">
    <sec:ifLoggedIn>
      <g:render template="/post/sidebar_profile"/>
    </sec:ifLoggedIn>
    <sec:ifNotLoggedIn>
      <g:render template="/post/sidebar_login"/>
    </sec:ifNotLoggedIn>
  </div>
</body>
</g:applyLayout>
