<!doctype html>
<html>
<head>
  <title>Hubbub &raquo; <g:layoutTitle default="Welcome" /></title>
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'hubbub.css')}"/>
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}"/>
  <g:layoutHead />
</head>
<body>
  <div>
    <div id="hd">
      <a href="${resource(dir: '/')}">
        <img id="logo" src="${resource(dir: 'images', file: 'headerlogo.png')}" 
             alt="hubbub logo"/>
      </a>
    </div>
    <div id="bd"><!-- start body -->
      <g:layoutBody/>
    </div>  <!-- end body -->
    <div id="ft">
      <div id="footerText">Hubbub - Social Networking on Grails</div>
    </div>
  </div>
</body>
</html>
