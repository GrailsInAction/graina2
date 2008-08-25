<html>
<head>
    <title>${heading}</title>
    <meta name="layout" content="hubbub"/>
</head>
<body>
<div id="followers">

    <h2>${heading}</h2>

     <g:each var="u" in="${users}">

         <div class="postImage">
            <img src="<g:createLink action="show" controller="image" id="${u.userId}"/>" alt="${u.userId}"/>
         </div>
         <div class="postEntry">
             <div class="postText">

                 <a href="<g:createLink action="show" id="${u.userId}"/>">${u.userId}</a>


             </div>
             <div class="postDate">
                 stop following / block button
             </div>
         </div>

     </g:each>

 </div>

</body>
</html>