package org.disl.util.wiki.visjs

import org.disl.meta.ForeignKeyMeta
import org.disl.meta.MappingSource
import org.disl.meta.Table

/**
 * Representation of vis.js network Edge.
 */
class Edge {

    String from
    String to
    String label = ''
    String color = 'blue'
    String arrows = 'to'
    String dashes = false
    String title

    static Edge foreignKey(Table table, ForeignKeyMeta fk) {
        new Edge(
                to: fk.targetTable.class.name,
                from: table.class.name,
                label: fk.name,
                title: "${table}.${fk.sourceColumns}=${fk.targetTable.toString()}.${fk.targetColumns}")
    }

    static Edge mappingSource(MappingSource src, MappingSource target) {
        new Edge(
                from: src.class.name,
                to: target.class.name,
                label: src.sourceAlias,
                title: (src.join ? src.join.condition : null)
        )
    }

    static Edge mappingTarget(MappingSource src, MappingSource target) {
        new Edge(
                to: target.class.name,
                from: src.class.name,
                label: 'target',
                color: 'red'
        )
    }

}
