<html>
    <head>
        <title>Upload Image</title>
        <meta name="layout" content="main"/>
    </head>
    <body>


        <g:uploadForm action="upload">
            User Id:
            <g:select name="userId" from="${com.grailsinaction.User.list()}"
                      optionKey="userId" optionValue="userId" />
            <p/>
            Photo: <input name="photo" type="file" />
            <g:submitButton name="upload" value="Upload"/>
        </g:uploadForm>



    </body>
</html>
