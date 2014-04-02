import com.grailsinaction.*
import spock.lang.*

@TestFor(UrlMappings)
@Mock(PostController)
class UrlMappingsSpec extends Specification {

    def "Ensure basic mapping operations for user permalink"() {

        expect:
        assertForwardUrlMapping(url, controller: expectCtrl, action: expectAction) {
            id = expectId
        }

        where:
        url		            | expectCtrl| expectAction  | expectId
        '/users/glen'           | 'post'    | 'timeline'    | 'glen'
        '/timeline/chuck_norris'| 'post'    | 'timeline'    | 'chuck_norris'
    }
}
