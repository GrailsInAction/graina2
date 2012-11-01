import com.grailsinaction.PostController
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(UrlMappings)
@Mock(PostController)
class UrlMappingsSpec extends Specification {

    def "Ensure basic mapping operations for user permalink"() {

        expect:
        assertForwardUrlMapping(url, controller: controller, action: action)

        where:
        url                     | controller| action
        '/users/glen'           | 'post'    | 'timeline'
        '/timeline/chuck_norris'| 'post'    | 'timeline'
    }

}
