package geb.mobile.ios.views

import geb.spock.GebSpec

/**
 * Created by gmueksch on 01.09.14.
 */
class UICatalogAppSpec extends GebSpec {

    def "start UICatalog"(){
        expect:
            at UICatalogAppView
    }

}
