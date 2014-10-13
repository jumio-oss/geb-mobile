package geb.mobile.helper

import geb.Browser
import geb.Configuration
import geb.mobile.GebMobileNavigatorFactory
import geb.mobile.driver.GebMobileDriverFactory
import groovy.swing.SwingBuilder
import org.openqa.selenium.remote.RemoteWebDriver

import static javax.swing.JFrame.EXIT_ON_CLOSE
import java.awt.*

/**
 * Created by thomaskastenhofer on 02.10.14.
 */
class UITester {
    static {
        GebMobileDriverFactory.setAppiumIos('appUT.package': 'UICatalog')//,simulator:'true', language:'en')
    }

    static RemoteWebDriver _driver



    public static void main(String[] args){

        def swingBuilder = new SwingBuilder()
        swingBuilder.edt {  // edt method makes sure UI is build on Event Dispatch Thread.
            lookAndFeel 'nimbus'  // Simple change in look and feel.
            frame(title: 'UITester', size: [1000,800], show: true, locationRelativeTo: null, defaultCloseOperation: EXIT_ON_CLOSE) {
                borderLayout(vgap: 5)
                panel(constraints: BorderLayout.CENTER, border: compoundBorder([emptyBorder(10), titledBorder('Try some commands')])) {
                    tableLayout {

                        tr {
                            td {
                                textArea id: 'code' ,columns: 50, rows: 15, text: " ", lineWrap:true, autoscrolls:true
                            }
                        }
                        tr {
                            td {
                                textArea id: 'result', columns: 50, rows: 15, "", lineWrap:true, autoscrolls:true
                            }
                        }
                    }

                }

                panel(constraints: BorderLayout.SOUTH) {
                    button text: 'Run', actionPerformed: {
                        if(! _driver ){
                            _driver = GebMobileDriverFactory.createMobileDriverInstance()
                        }
                        try {
                              Browser.drive( new Configuration( [ navigatorFactory: { br -> new GebMobileNavigatorFactory(br) }, driver: { _driver }] ) ){
                                 GroovyShell sh = new GroovyShell()
                                 sh.setVariable("browser", browser)
                                 sh.setVariable("driver" , browser.driver )

                                 result.text = sh.evaluate(code.text)
                             }
                        }catch(e){
                            result.text = e.message
                        }
                     }
                    button text: 'genGeb', actionPerformed: {
                        if(! _driver ){
                            _driver = GebMobileDriverFactory.createMobileDriverInstance()
                        }
                        def aut = new XmlSlurper().parseText(_driver.pageSource)
                        def res = new StringWriter()
                        res.withWriter {wr ->
                            aut.'**'.each {
                                def name = it.'@name'.text()
                                def label = it.'@label'.text()
                                def enabled = it.'@enabled'.text()
                                def visible = it.'@visible'.text()
                                def tagname = it.name()
                                wr.write "$name , $label, $enabled, $visible, $tagname \n"
                            }
                        }
                        result.text = res.toString()

                    }

                    }

                // Binding of textfield's to address object.

             }
        }
    }
}
