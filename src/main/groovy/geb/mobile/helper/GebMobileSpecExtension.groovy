package geb.mobile.helper;

import groovy.util.logging.Slf4j;
import org.spockframework.runtime.extension.IGlobalExtension;
import org.spockframework.runtime.model.SpecInfo;

@Slf4j
class GebMobileSpecExtension implements IGlobalExtension {

    @Override
    void visitSpec(SpecInfo specInfo) {
        if( specInfo.getSuperSpec()?.getName() == "GebMobileBaseSpec" ) {
            log.info("--------- GebMobileSpecListener is activated for $specInfo.name -----------")
            specInfo.addListener(new GebMobileSpecListener())
        }
    }
}