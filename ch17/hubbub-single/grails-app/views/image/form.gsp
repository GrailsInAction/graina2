<html>
<head>
    <title>Upload Image</title>
    <meta name="layout" content="main">
</head>
<body>
    <h1>Upload an image</h1>
    <g:uploadForm action="upload">
        User Id:
        <g:select name="loginId" from="${userList}"
                  optionKey="loginId" optionValue="loginId" />
        <p/>
        Photo: <input name="photo" type="file" />
        <g:submitButton name="upload" value="Upload"/>
    </g:uploadForm>
</body>
</html>
