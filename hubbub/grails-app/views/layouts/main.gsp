<html>
<head>
  <title>Hubbub &raquo; <g:layoutTitle default="Welcome" /></title>
  <link rel="shortcut icon" href="${createLinkTo(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
  <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'reset-fonts-grids.css')}"/>
  <link rel="stylesheet" href="<g:createLinkTo dir='css' file='hubbub.css'/>"/>
  <g:layoutHead />
  <g:javascript library="application" />
  <g:javascript library="scriptaculous"/>
</head>
<body>
  <div id="doc3" class="yui-t5">
    <div id="hd">
      <a href="${resource(dir:'/')}"><img id="logo" src="${resource(dir: 'images', file: 'headerlogo.png')}" alt="Hubbub logo"/></a>
    </div>
    
    <div id="bd"><!-- start body -->
      <g:layoutBody/>
    </div>  <!-- end body -->
    
    <div id="ft">
        <div id="footerText">
        Hubbub <g:meta name="app.version"/> on Grails <g:meta name="app.grails.version"/>.
        </div>
    </div>
  </div>
  <r:layoutResources/>		
</body>	
</html>
