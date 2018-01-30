package geb.mobile.helper

import geb.Browser
import geb.Configuration
import geb.mobile.GebMobileNavigatorFactory
import geb.mobile.driver.GebMobileDriverFactory
import groovy.swing.SwingBuilder
import io.appium.java_client.ios.IOSDriver
import org.openqa.selenium.remote.RemoteWebDriver

import javax.swing.*
import java.awt.*

import static javax.swing.JFrame.EXIT_ON_CLOSE

/**
 * Created by thomaskastenhofer on 02.10.14.
 */
class UITester {
    static {
        //GebMobileDriverFactory.setAppiumIos('appUT.package': 'UICatalog')//,simulator:'true', language:'en')
        System.setProperty("apple.laf.useScreenMenuBar", "true")

        // set the application name for mac os
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "UITester")

    }

    static RemoteWebDriver _driver

    static def pageSourceMap = [:]


    public static drive(Closure closure) {
        if (!_driver) {
            _driver = GebMobileDriverFactory.createMobileDriverInstance()
        }
        Browser.drive(new Configuration([navigatorFactory: { br -> new GebMobileNavigatorFactory(br) }, driver: {
            _driver
        }]), closure)
    }

    public static void main(String[] args) {

        def swingBuilder = new SwingBuilder()
        swingBuilder.edt {  // edt method makes sure UI is build on Event Dispatch Thread.
            lookAndFeel 'nimbus'  // Simple change in look and feel.
            frame(id: 'frame', title: 'UITester', size: [1000, 800], show: true, locationRelativeTo: null, defaultCloseOperation: EXIT_ON_CLOSE) {
                borderLayout(vgap: 5)
                panel(id: 'panel', constraints: BorderLayout.WEST, border: compoundBorder([emptyBorder(10), titledBorder('Try some commands')])) {
                    tableLayout {
                        tr {
                            td {
                                textArea id: 'code', columns: 50, rows: 15, text: " ", lineWrap: false, autoscrolls: true
                            }
                        }
                        tr {
                            td {
                                scrollPane(constraints: gbc(gridx: 1, gridy: 1, gridwidth: GridBagConstraints.REMAINDER, fill: GridBagConstraints.VERTICAL, insets: [20, 300, 85, 0])) {
                                    textArea id: 'result', columns: 50, rows: 15, "", lineWrap: false, autoscrolls: true
                                }
                            }
                        }
                    }
                }

                tabbedPane(id: 'pages', alignmentX: 0f, constraints: BorderLayout.CENTER) {

                }
                panel(constraints: BorderLayout.SOUTH, border: compoundBorder([emptyBorder(10), titledBorder('Actions')])) {
                    button text: 'Run', actionPerformed: {

                        try {
                            drive {
                                GroovyShell sh = new GroovyShell()
                                sh.setVariable("browser", browser)
                                sh.setVariable("driver", browser.driver)

                                result.text = sh.evaluate(code.text)
                            }
                        } catch (e) {
                            result.text = e.message
                        }
                    }
                    button text: 'genGeb', actionPerformed: {

                        def res = new StringWriter()
                        res.withWriter { wr ->
                            getPageXml().'**'.each {
                                def name = it.'@name'.text()
                                String label = it.'@label'.text()
                                def enabled = it.'@enabled'.text()
                                def visible = it.'@visible'.text()
                                def tagname = it.name()
                                if (label) {

                                    wr.write "${label.replaceAll(/\W/, '_')} { \$('#${label}') } // $tagname , $visible \n"
                                }
                            }
                        }
                        result.text = res.toString()

                    }
                    button text: 'pageSource', actionPerformed: {
                        if (!_driver) {
                            _driver = GebMobileDriverFactory.createMobileDriverInstance()
                        }
                        result.text = _driver?.pageSource
                    }

                    button text: 'visualize Page', actionPerformed: {

                        String page = pageName.text
                        int pageIndex = pages.indexOfTab(page)

                        if (pageIndex < 0) {
                            pages.addTab(page, panel(alignmentX: 0f, layout: null))
                            pages.setSelectedIndex(pages.indexOfTab(page))
                        }

                        Closure clo = _driver instanceof IOSDriver ? addComponentIos : addComponentAndroid
                        clo.setResolveStrategy(Closure.DELEGATE_FIRST)
                        clo.setDelegate(swingBuilder)

                        getPageXml().'**'.each clo
                        pages.getSelectedComponent().revalidate();
                        frame.repaint()
                    }

                    textField id: 'pageName', columns: 15, text: 'MainPage'
                    textField id: 'filter', columns: 20, text: '.*(Button|Text)'

                }

                // Binding of textfield's to address object.

            }
        }
    }

    public static def getPageXml(){
        if (!_driver) _driver = GebMobileDriverFactory.createMobileDriverInstance()
        new XmlSlurper().parseText(_driver.pageSource)
    }

    public static elementMapping = [ '' ]

    public static getCompFilter(){
        //Pattern.compile(filter.text ?: ".*")
    }

    public static Closure addComponentIos = { it ->

        String label = it.'@label'.text()

        if (label) {
            def enabled = it.'@enabled'.text()
            def visible = it.'@visible'.text()
            def tagname = it.name()

            if (Boolean.valueOf(enabled) && Boolean.valueOf(visible) && tagname =~ /${filter.text}/ ) {

                def comp

                comp = new JButton(text: label.toLowerCase().replaceAll(/\W/, '_'), actionPerformed: {
                    drive { browser.find("#${label}").click() }
                })

                comp.setLocation(it.@x.toString().toInteger(), it.@y.toString().toInteger())
                comp.setSize(it.@width.toString().toInteger(), it.@height.toString().toInteger())

                pages.getSelectedComponent().add comp
            }
        }
    }

    public static Closure addComponentAndroid = { it ->
/*
    <android.widget.TextView index="0" text="" class="android.widget.TextView"
     package="com.jumio.qa" content-desc="Scan"
     checkable="false" checked="false" clickable="true"
     enabled="true" focusable="true" focused="false"
     scrollable="false" long-clickable="true"
     password="false" selected="false"
     bounds="[1244,102][1440,270]" resource-id=""
     instance="0"/>
     */
        String label = it.'@content-desc'.text()
        String resourceId = it.'@resource-id'.text()

        if (label) {
            def enabled = it.'@enabled'.text()
            def visible = it.'@clickable'.text()
            def tagname = it.name()

            if (Boolean.valueOf(enabled) && Boolean.valueOf(visible) && tagname =~ myFilter) {

                def b = new JButton(text: label.toLowerCase().replaceAll(/\W/, '_'), actionPerformed: {
                    drive { browser.find( resourceId? "#${resourceId}" : "description('$label')" ).click() }
                })

                b.setLocation(it.@x.toString().toInteger(), it.@y.toString().toInteger())
                b.setSize(it.@width.toString().toInteger(), it.@height.toString().toInteger())

                currentTab.add b
            }
        }
    }


}
