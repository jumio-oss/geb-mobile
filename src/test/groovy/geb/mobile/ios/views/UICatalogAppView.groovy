package geb.mobile.ios.views

import geb.mobile.ios.IosBaseView

/**
 * Created by gmueksch on 01.09.14.
 */
class UICatalogAppView extends IosBaseView {

    static at = { true }

    static content = {
        mytitle { $("#title") }
    }

}
