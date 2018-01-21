package geb.mobile.web.specification

import geb.mobile.GebMobileBaseSpec
import spock.lang.Stepwise

/**
 * Created by gmueksch on 30.08.14.
 */
@Stepwise
class MobileWebTestGoogleSearch extends GebMobileBaseSpec{

    static {
        System.setProperty "framework", "selendroid"
    }

    def "open Google"(){
        when:
        go("http://www.google.at")
        then:
        $("//*")
        true
    }
}
