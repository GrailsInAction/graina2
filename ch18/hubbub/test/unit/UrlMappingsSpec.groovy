import com.grailsinaction.PostController
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(UrlMappings)
@Mock(PostController)
class UrlMappingsSpec extends Specification {

    def "Ensure basic mapping operations for user permalink"() {

        expect:
        assertForwardUrlMapping(url, controller: expectCtrl, action: expectAction) {
            id = expectId
        }

        where:
        url                     | expectCtrl| expectAction  | expectId
        '/users/glen'           | 'post'    | 'timeline'    | 'glen'
        '/timeline/chuck_norris'| 'post'    | 'timeline'    | 'chuck_norris'
    }

}
