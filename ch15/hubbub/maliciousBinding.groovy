import com.grailsinaction.*

grails.util.Environment.executeForCurrentEnvironment(new BootStrap().init)

println Post.list()
def post = Post.get(7)

println post.properties
post.properties["content"] = [content: "Pilates is killing me", 'user.id': "5"]

println post.properties