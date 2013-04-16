import com.grailsinaction.*

//grails.util.Environment.executeForCurrentEnvironment(new BootStrap().init)

println "1 >> " + Post.where {
    user.id == 3
}.list()

println "2 >> " + Post.withCriteria {
   createAlias "tags", "t"

   user { eq "loginId", "phil" }

   projections {
      groupProperty "t.name"
      count "t.id"
   }
}

println "3 >> " + Post.executeQuery("select t.name, count(t.id) from Post p join p.tags as t where p.user.loginId = 'phil' group by t.name")

