package geb.mobile.helper

import groovy.util.logging.Slf4j
import io.selendroid.SelendroidDriver
import org.openqa.selenium.WebElement

/**
 * Created by gmueksch on 11.07.14.
 */
@Slf4j
class SelendroidActivityHelper {

    def activities = [:]

    SelendroidDriver driver

    def parser = [TextView   : { WebElement e -> e.getText() },
                  Button     : { WebElement e -> e.getText() },
                  ImageButton: { WebElement e -> e.getText() },
                  EditText   : { WebElement e -> e.getText() },
                  CheckBox   : { WebElement e -> e.getText() }
    ]


    public SelendroidActivityHelper(def driver) {
        this.driver = driver
    }

    public void parseActivity() {
        def url = driver.currentUrl

        if( activities[url] ) {
            log.info("activity $url alread parsed")
            return
        }
        //StringBuffer sb = new StringBuffer()
        driver.findElementByXPath("//*").each { WebElement elem ->
            println "   " + parser[elem.tagName]?.call(elem)
        }

    }

    public Map inspect(WebElement elem) {
        return [id     : elem.getAttribute("id"),
                name   : elem.getAttribute("name"),
                tagname: elem.getAttribute("tagname"),
                text   : elem.getAttribute("text"),
                props  : elem.getProperties()
        ]
    }

    public String getFinderString(def map) {
        return map.id ? "\$('#${map.id}')" : "\$('linkText=${map.text}')"
    }

    public void parseApp(){
        parseActivity()

    }
}
