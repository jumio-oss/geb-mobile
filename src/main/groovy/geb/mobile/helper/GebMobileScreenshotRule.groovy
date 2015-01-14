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

    public File getSnapshotDir(){
        def dir = getJenkinsWorkspace()?:'snapshots'
        if( !dir.exists() ){
            dir.mkdirs()
        }

        return dir
    }

    public static String getJenkinsWorkspace() {
        return System.getenv('WORKSPACE')
    }

    public static String getJobName() {
        return System.getenv("JOB_NAME")
    }

    public static String getBuildNumber() {
        return System.getenv("BUILD_NUMBER")
    }

    public static String getBuildURL() {
        return System.getenv("BUILD_URL")
    }

    public static String getArtifactURL() {
        return getBuildURL() + "artifact/"
    }

    public static boolean isJenkinsExecution() {
        return getJenkinsWorkspace() != null
    }

    /**
     * Method to get an URL to a file
     * @param file , to generate the URL for
     * @return an URL, when on Jenkins to the archived artifact, when local the URL to file is returned
     */
    public static String getRefUrlToArchivedFileInBuild(File file) {

        if (file == null) return null
        if (!isJenkinsExecution()) return file.toURI().toURL()

        File ws = new File(getJenkinsWorkspace())
        if (!file.absolutePath.startsWith(ws.absolutePath)) {
            log.warn("File $file.absolutePath is not part of the Workspace")
            return ""
        }

        String url = getArtifactURL() + (file.absolutePath - ws.absolutePath)
        url = url.replace('\\','/')

        log.debug("URL in build ${getBuildNumber()} to archived file: $file --> $url")
        return url

    }

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
                        def snapDir = getSnapshotDir()
                        try {
                            def pngFile = new File( snapDir, fName+ '.png')
                            ImageIO.write(img, "png", pngFile)
                            log.warn("SnapShot is accessibly after end of build on: ${getRefUrlToArchivedFileInBuild(pngFile)}")
                        } catch (e1) {
                            log.warn "error writing image: $e1.message"
                        }
                        def xmlFile = new File( snapDir, fName+'_pageSource.xml' )
                        try{
                            xmlFile.withWriter { wr->
                                wr.write(baseSpec.getDriver().getPageSource())
                            }
                            log.warn("SnapShot is accessibly after end of build on: ${getRefUrlToArchivedFileInBuild(xmlFile)}")
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
