package org.disl.util.clipboard

import groovy.util.logging.Slf4j

import java.awt.*
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

/**
 * Helper class for working with system clipboard.
 * To simplify unit testing, for headless systems the clipboard is mocked using static variable.
 * */
@Slf4j
class ClipboardHelper {
    private static StringSelection clipboardMock

    static void copy(StringSelection text) {
        try {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(text, null)
        } catch (Exception e) {
            clipboardMock = text
            log.warn("Unable to access system clipboard. Saved to clipboard mock.", e)
        }
    }

    static String getContent() {
        try {
            Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.stringFlavor)
        } catch (Exception e) {
            log.warn("Unable to access system clipboard. Content read from clipboard mock.", e)
            return clipboardMock.getTransferData(DataFlavor.stringFlavor)
        }
    }
}
