package geb.mobile.helper

import groovy.util.logging.Slf4j
import org.spockframework.runtime.AbstractRunListener
import org.spockframework.runtime.model.ErrorInfo
import org.spockframework.runtime.model.FeatureInfo
import org.spockframework.runtime.model.IterationInfo
import org.spockframework.runtime.model.SpecInfo

/**
 *
 */
@Slf4j
class GebMobileSpecListener extends AbstractRunListener {

//    ThreadLocal<GebSpec> _currentDriver = new ThreadLocal<WebDriver>()

    public void error(ErrorInfo error) {
        log.info "Actual on error logic: $error"
        FeatureInfo fi = error.getMethod().getFeature()
        println "Feature: $fi.name "

    }

    @Override
    void beforeSpec(SpecInfo spec) {
        log.info "Actual on beforeSpec: $spec"
        def udid = System.properties.'appium_udid'
        if( udid ){
            spec.setName( udid+'_'+spec.getName() )
        }

    }

    @Override
    void beforeFeature(FeatureInfo feature) {
        log.info "Actual on beforeFeature: $feature"
    }

    @Override
    void beforeIteration(IterationInfo iteration) {
        log.info "Actual on beforeIteration: $iteration"
    }

    @Override
    void afterIteration(IterationInfo iteration) {
        log.info "Actual on afterIteration: $iteration"
    }

    @Override
    void afterFeature(FeatureInfo feature) {
        log.info "Actual on afterFeature: $feature"
    }

    @Override
    void afterSpec(SpecInfo spec) {
        log.info "Actual on afterSpec: $spec"
    }

    @Override
    void specSkipped(SpecInfo spec) {
        log.info "Actual on specSkipped: $spec"
    }

    @Override
    void featureSkipped(FeatureInfo feature) {
        log.info "Actual on featureSkipped: $feature"
    }
}
