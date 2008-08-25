<html>
    <head>
        <title>Hubbub &raquo; <g:layoutTitle default="Welcome" /></title>
        <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'hubbub.css')}" />
        <link rel="shortcut icon" href="${createLinkTo(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'reset-fonts-grids.css')}"/>
        <g:layoutHead />
        <g:javascript library="application" />				
    </head>
    <body>	
        <div id="doc3" class="yui-t5">
		            <div id="hd">
		                <img id="logo" src="${createLinkTo(dir: 'images', file: 'headerlogo.png')}" alt="hubbub logo"/>

		                <div id="hdtitle"><a href="<g:createLinkTo dir="/"/>">Hubbub</a>
						</div>

		                <div style="float: right; position: relative; margin-right: 7px; font-size: medium; ">
		                    <g:textField name="search" value="Search here..."/>
		                </div>

		            </div>
		            <div id="bd"><!-- start body -->

		                <div id="yui-main">
		                    <div class="yui-b">
		                        <g:if test="${flash.message}">
		                            <div class="flash">
		                                ${flash.message}
		                            </div>
		                        </g:if>

		                        <g:layoutBody/>
		                    </div>
		                </div>
		                <div class="yui-b">

		                    <g:render template="/post/sidebar"/>

		                </div>

		            </div>  <!-- end body -->
		            <div id="ft">
		                <div id="footerText">
		                Hubbub <g:meta name="app.version"/> on Grails <g:meta name="app.grails.version"/>.
		                </div>
		            </div>
		        </div>		
    </body>	
</html>