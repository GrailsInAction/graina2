import com.grailsinaction.*
import static java.util.Calendar.*

class BootStrap {

    def searchableService

    def init = { servletContext ->
        environments {
            development {
                if (!Post.count()) createSampleData()
            }
            test {
                if (!Post.count()) createSampleData()
            }
        }

        // Admin user is required for all environments
        createAdminUserIfRequired()
    }

    private createSampleData() {
        println "Creating sample data"

        // Search mirroring (where saved domain instances are automatically indexed) is
        // fragile and has problems with the data below.
        searchableService.stopMirroring()

        def now = new Date()
        def chuck = new User(
                loginId: "chuck_norris",
                password: "highkick",
                profile: new Profile(fullName: "Chuck Norris", email: "chuck@nowhere.net"),
                dateCreated: now).save(failOnError: true)
        def glen = new User(
                loginId: "glen",
                password: "sheldon",
                profile: new Profile(fullName: "Glen Smith", email: "glen@nowhere.net"),
                dateCreated: now).save(failOnError: true)
        def peter = new User(
                loginId: "peter",
                password: "mandible",
                profile: new Profile(fullName: "Peter Ledbrook", email: "peter@nowhere.net"),
                dateCreated: now).save(failOnError: true)
        def frankie = new User(
                loginId: "frankie",
                password: "testing",
                profile: new Profile(fullName: "Frankie Goes to Hollywood", email: "frankie@nowhere.net"),
                dateCreated: now).save(failOnError: true)
        def sara = new User(
                loginId: "sara",
                password: "crikey",
                profile: new Profile(fullName: "Sara Miles", email: "sara@nowhere.net"),
                dateCreated: now - 2).save(failOnError: true)
        def phil = new User(
                loginId: "phil",
                password: "thomas",
                profile: new Profile(fullName: "Phil Potts", email: "phil@nowhere.net"),
                dateCreated: now)
        def dillon = new User(loginId: "dillon",
                password: "crikey",
                profile: new Profile(fullName: "Dillon Jessop", email: "dillon@nowhere.net"),
                dateCreated: now - 2).save(failOnError: true)

        chuck.addToFollowing(phil)
        chuck.addToPosts(content: "Been working my roundhouse kicks.")
        chuck.addToPosts(content: "Working on a few new moves. Bit sluggish today.")
        chuck.addToPosts(content: "Tinkering with the hubbub app.")
        chuck.save(failOnError: true)

        phil.addToFollowing(frankie)
        phil.addToFollowing(sara)
        phil.save(failOnError: true)

        phil.addToPosts(content: "Very first post")
        phil.addToPosts(content: "Second post")
        phil.addToPosts(content: "Time for a BBQ!")
        phil.addToPosts(content: "Writing a very very long book")
        phil.addToPosts(content: "Tap dancing")
        phil.addToPosts(content: "Pilates is killing me")
        phil.save(failOnError: true)

        sara.addToPosts(content: "My first post")
        sara.addToPosts(content: "Second post")
        sara.addToPosts(content: "Time for a BBQ!")
        sara.addToPosts(content: "Writing a very very long book")
        sara.addToPosts(content: "Tap dancing")
        sara.addToPosts(content: "Pilates is killing me")
        sara.save(failOnError: true)

        dillon.addToPosts(content: "Pilates is killing me as well")
        dillon.save(failOnError: true, flush: true)

        // We have to update the 'dateCreated' field after the initial save to
        // work around Grails' auto-timestamping feature. Note that this trick
        // won't work for the 'lastUpdated' field.
        def postsAsList = phil.posts as List
        postsAsList[0].addToTags(user: phil, name: "groovy")
        postsAsList[0].addToTags(user: phil, name: "grails")
        postsAsList[0].dateCreated = now.updated(year: 2004, month: MAY)

        postsAsList[1].addToTags(user: phil, name: "grails")
        postsAsList[1].addToTags(user: phil, name: "ramblings")
        postsAsList[1].addToTags(user: phil, name: "second")
        postsAsList[1].dateCreated = now.updated(year: 2007, month: FEBRUARY, date: 13)

        postsAsList[2].addToTags(user: phil, name: "groovy")
        postsAsList[2].addToTags(user: phil, name: "bbq")
        postsAsList[2].dateCreated = now.updated(year: 2009, month: OCTOBER)

        postsAsList[3].addToTags(user: phil, name: "groovy")
        postsAsList[3].dateCreated = now.updated(year: 2011, month: MAY, date: 1)

        postsAsList[4].dateCreated = now.updated(year: 2011, month: DECEMBER, date: 4)
        postsAsList[5].dateCreated = now.updated(year: 2012, date: 10)
        phil.save(failOnError: true)

        postsAsList = sara.posts as List
        postsAsList[0].dateCreated = now.updated(year: 2007, month: MAY)
        postsAsList[1].dateCreated = now.updated(year: 2008, month: APRIL, date: 13)
        postsAsList[2].dateCreated = now.updated(year: 2008, month: APRIL, date: 24)
        postsAsList[3].dateCreated = now.updated(year: 2011, month: NOVEMBER, date: 8)
        postsAsList[4].dateCreated = now.updated(year: 2011, month: DECEMBER, date: 4)
        postsAsList[5].dateCreated = now.updated(year: 2012, month: AUGUST, date: 1)
        
        sara.dateCreated = now - 2
        sara.save(failOnError: true)

        dillon.dateCreated = now - 2
        dillon.save(failOnError: true, flush: true)

        // Now that the data has been persisted, we can index it and re-enable mirroring.
        searchableService.index()
        searchableService.startMirroring()
    }

    private createAdminUserIfRequired() {
        println "Creating admin user"
        if (!User.findByLoginId("admin")) {
            println "Fresh Database. Creating ADMIN user."

            def profile = new Profile(email: "admin@yourhost.com", fullName: "Administrator")
            new User(loginId: "admin", password: "secret", profile: profile).save(failOnError: true)
        }
        else {
            println "Existing admin user, skipping creation"
        }
    }

}
