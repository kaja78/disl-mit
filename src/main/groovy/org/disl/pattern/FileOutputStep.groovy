package org.disl.pattern

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import java.nio.charset.Charset
;

@CompileStatic
@Slf4j
abstract class FileOutputStep extends Step {

    String charset

    abstract File getFile()

    String getCharset() {
        if (!charset) {
            return Charset.defaultCharset()
        }
        return charset
    }

    @Override
    public int executeInternal() {
        if (file.getParentFile().mkdirs()) {
            log.info("Created directory ${file.getParentFile().getAbsolutePath()}")
        }
        file.createNewFile()
        file.write(getCode(), getCharset())
        log.info("Created file ${file.getAbsolutePath()}")
        return 1
    }

}
