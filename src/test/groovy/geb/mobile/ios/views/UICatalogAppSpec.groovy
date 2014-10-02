package geb.mobile.ios.views

import geb.mobile.driver.GebMobileDriverFactory
import geb.spock.GebSpec

/**
 * Created by gmueksch on 01.09.14.
 */
class UICatalogAppSpec extends GebSpec {

    static {
        GebMobileDriverFactory.setAppiumIos('appUT.package': 'UICatalog')//,simulator:'true', language:'en')
    }

    def "start UICatalog"(){
        expect:
            at UICatalogAppView

    }

    def "open TextFields"(){
        given:
        at UICatalogAppView
        when:
        textFields.click()
        then:
        at UICatalogAppView


    }

}
