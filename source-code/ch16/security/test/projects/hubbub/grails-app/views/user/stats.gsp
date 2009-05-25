
<html>
    <head>
        <title>Stats for ${userId}</title>
        <meta name="layout" content="main"/>
        <style type="text/css">
            dt {
                float: left;
                width: 7em;
            }
            dd {
                margin: .8em;
            }
        </style>
    </head>
    <body>

        <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
        </g:if>

        <h1>Stats for ${userId}</h1>
        <div>

            <g:pieChart type="3d"
                        title='Posts Per Day'
                        size="${[600,200]}"
                        labels="${postsOnDay.keySet()}"
                        dataType='text'
                        data='${postsOnDay.values().asList()}' />


        </div>
        <div>
            <g:barChart type="bvs"
                        title="Posts Per Date (bar)"
                        size="${[200,200]}"
                        colors="${['00FF00']}"
                        fill="${'bg,s,efefef'}"
                        axes="x,y"
                        axesLabels="${[0:postsOnDay.keySet(),1:[0,postsOnDay.values().max()/2,postsOnDay.values().max()]]}"
                        dataType="simple"
                        data="${postsOnDay.values().asList()}" />

        </div>

        <div>
            <%

            def doubleData = postsOnDay.values().collect { it * 2 }
            def seriesData = [ postsOnDay.values().asList(), doubleData ]
            %>

            <g:lineChart type="lc"
                         title="Posts Per Date (line)"
                         size="${[600,200]}"
                         colors="${['FF0000','0000FF']}"
                         axes="x,y"
                         lineStyles="${[[2,2,2],[2,8,4]]}"
                         legend="${[ 'Original', 'Doubler' ]}"
                         gridLines="16.6,25"
                         axesLabels="${[0:postsOnDay.keySet(),1:[0,doubleData.max()/2,doubleData.max()]]}"
                         dataType="simple"
                         data="${ seriesData }" />

        </div>


    </body>
</html>
