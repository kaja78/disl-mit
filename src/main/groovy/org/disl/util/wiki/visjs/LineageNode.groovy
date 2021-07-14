package org.disl.util.wiki.visjs

import org.disl.meta.MappingSource

/**
 * Representation of vis.js network lienage Node.
 */
class LineageNode extends Node {
    int level

    LineageNode(MappingSource m, int level) {
        super(m)
        this.level = level
    }

}
