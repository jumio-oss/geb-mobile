package geb.mobile.ios.views

import geb.mobile.ios.IosBaseView

/**
 * Created by gmueksch on 01.09.14.
 */
class UICatalogAppView extends IosBaseView {

    static at = { textFields.isEnabled() }

    static content = {
        mytitle { $("#title") }
        buttons{ $("name='Buttons, Various uses of UIButton'")[0] }
        textFields{ $("name='TextFields, Uses of UITextField'")[0] }
    }

}
