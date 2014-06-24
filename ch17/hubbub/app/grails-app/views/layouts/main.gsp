<!doctype html>
<html ${pageProperty(name:'page.htmlAttrs')}>
<head>
  <title>Hubbub &raquo; <g:layoutTitle default="Welcome" /></title>
  <r:require module="baseCss"/>
  <g:layoutHead />
  <r:layoutResources />
  <nav:resources/>
</head>
<body>
  <div>
    <div id="hd">
      <g:link uri="/">
        <g:img id="logo" uri="/images/headerlogo.png" alt="hubbub logo"/>
      </g:link>
    </div>
    <div id="bd"><!-- start body -->
      <nav:render group="tabs"/>
      <g:layoutBody/>
    </div>  <!-- end body -->
    <div id="ft">
      <div id="footerText">Hubbub - Social Networking on Grails</div>
    </div>
  </div>
  <r:layoutResources />
</body>
</html>
