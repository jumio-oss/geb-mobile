package geb.mobile.android

import geb.mobile.driver.GebMobileDriverFactory
import groovy.xml.XmlUtil
import org.openqa.selenium.remote.RemoteWebDriver

/**
 * Created by gmueksch on 12.08.14.
 */
class AppiumPageSourceConverter {

    public static void main(String[] args) {
        System.setProperty("appUT.package", "io.selendroid")
        //System.setProperty("appium_app", new File(ClassLoader.getSystemResource("testapk/selendroid-test-app-0.9.0.apk").toURI()).absolutePath)
        System.setProperty("appium_appActivity", "MainActivity")
        //To run the test on a specific device
        //System.setProperty("appium_udid","192.168.56.10:5555")
        //System.setProperty("appium_udid","SH434WM02730")
        //Use Appium
        System.setProperty("framework", "appium")

        RemoteWebDriver driver = GebMobileDriverFactory.createMobileDriverInstance()

        sleep(1000)
        //println driver.findElementsByXPath("//*").size()

        writeActivityContent(driver,"")
        driver.quit()
    }

    public static String getPackageName(driver){
        def hierarchy = new XmlSlurper().parseText(driver.pageSource)
        hierarchy.'android.widget.FrameLayout'.@package.text()
    }

    public static writeActivityContent(RemoteWebDriver driver, String moduleName) {

        def xml = new XmlSlurper().parseText(driver.pageSource)
        def currAct = driver.currentActivity()
        def activityName = currAct.startsWith(".") ? currAct.substring(1) : currAct
        def packageName =  getPackageName(driver)
        def pkg = "${packageName}.activities"
        def modulePkg = "${packageName}.modules"
        def pkgDir = new File("qa-sdk/src/test/groovy/${pkg.replace('.', '/')}")
        def modulePkgDir = new File("qa-sdk/src/test/groovy/${modulePkg.replace('.', '/')}")
        if (!pkgDir.exists()) pkgDir.mkdirs()
        new File(pkgDir, "${activityName.split(/\./)[-1]}_${moduleName}.xml").withWriterAppend { wr ->
            wr.write(XmlUtil.serialize(xml))
        }

        def f = new File(pkgDir, "${activityName.split(/\./)[-1]}.groovy")
        println "writing Activity $activityName to $f.absolutePath"
        if (!f.exists()) {
            f.withWriterAppend { wr ->
                writeActivity(pkg, activityName.split(/\./)[-1], wr)
                writeContent(xml, wr)
                wr.write("\t}\n}\n")
            }
        }else if( moduleName ){
            f =  new File(modulePkgDir, "${moduleName}.groovy")
            f.withWriterAppend { wr ->
                writeModule(modulePkg, moduleName, wr)
                writeContent(xml, wr)
                wr.write("\t}\n}\n")
            }
        }else{
            throw new Exception("Activity exists already and no modulename is given, abort...")
        }
    }

    private static void writeModule(String pkg, String moduleName, def wr) {
        wr.write("package $pkg \n")
        wr.write("import geb.Module\n")
        wr.write("class ${moduleName} extends Module { \n")
        wr.write("\tstatic content = { \n\n")
    }

    private static void writeActivity(String pkg, String activityName, def wr) {
        wr.write("package $pkg \n")
        wr.write("import geb.mobile.android.AndroidBaseActivity\n")
        wr.write("class ${activityName} extends AndroidBaseActivity { \n")
        wr.write("\tstatic content = { \n\n")
    }

    private static void writeContent(def xml, def wr) {
        xml.'**'.each {
            def clas = it.'@class'.text()
            def type = clas.toString().split(/\./)[-1]
            def contentDesc = it.'@content-desc'.text()
            def text = it.'@text'.text()
            def index = it.'@index'.text()
            def resourceIdFull = it.'@resource-id'.text()
            println "ResourceID: $resourceIdFull , Text: '$text' , Type: $type"

            if (resourceIdFull) {
                String resourceId = resourceIdFull.split(/:/)[1].substring(3)
                def alias = "${resourceId.charAt(0).toUpperCase()}${resourceId.substring(1)}"
                wr.write("\t\t$alias { \$('#${resourceId}') } ${comment(type, it)} \n")
            } else if (contentDesc) {
                wr.write("\t\t$contentDesc { \$('#${contentDesc}') } ${comment(type, it)} \n")
            } else if (type == 'EditText') {
                wr.write("\t\t${type}_${text.replaceAll(" ", "_")} { \$('//${it.'@class'.text()}[@index=$index]') } ${comment(type, it)} \n")
            } else if (type == 'TextView') {
                wr.write("\t\t${type}_${text.replaceAll(" ", "_")} { \$('//${it.'@class'.text()}[@index=$index]') } ${comment(type, it)} \n")
            } else if (text) {
                wr.write("\t\t${type}_${text.replaceAll(" ", "_")} { \$(\"text('${text}')\") } ${comment(type, it)} \n")
            }
        }
    }

    private static String comment(def type, def it) {
        return "/* Type: $type ${it.'@clickable' == 'true' ? ',clickable' : ''}  */"
    }


}
