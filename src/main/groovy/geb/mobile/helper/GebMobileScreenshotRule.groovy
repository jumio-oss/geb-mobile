package geb.mobile.helper

import geb.mobile.GebMobileBaseSpec
import groovy.util.logging.Slf4j
import org.junit.rules.MethodRule
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement

import javax.imageio.ImageIO

/**
 * Created by gmueksch on 11.11.14.
 */
@Slf4j
class GebMobileScreenshotRule implements MethodRule{

    public GebMobileBaseSpec baseSpec

    @Override
    Statement apply(Statement base, FrameworkMethod method, Object target){

        return new Statement() {
            @Override
            void evaluate() throws Throwable {
                try {
                    base.evaluate()
                }catch(Throwable ex){
                    if( baseSpec ) {
                        log.warn("Caugth $ex.message --> take screenshot")
                        def img = baseSpec.getScreenShotAsImage()
                        def fName = method.getName().replaceAll(/[ ,\._\-:]/, "_")
                        try {
                            ImageIO.write(img, "png", new File( 'build', fName+ '.png'))
                        } catch (e1) {
                            log.warn "error writing image: $e1.message"
                        }
                        try{
                            new File( 'build', fName+'_pageSource.xml' ).withWriter { wr->
                                wr.write(baseSpec.getDriver().getPageSource())
                            }
                        }catch(e){
                            log.warn("problem creating pageSource: $e.message")
                        }
                    }
                    throw ex
                }
            }
        }

    }
}
