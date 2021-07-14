package org.disl.util.wiki.visjs

import org.disl.meta.Table

/**
 * Helper for building data model for vis.js network visualisation. See http://visjs.org/docs/network/.
 */
class DataModelNetwork {

    Set<Node> nodes = new HashSet<Node>();
    List<Edge> edges = [];

    public DataModelNetwork() {}

    public DataModelNetwork(Table table) {
        add(table)
    }

    void add(Table table) {
        if (nodes.add(new Node(table))) {
            table.foreignKeys.each {
                add(it.targetTable)
                edges.add(Edge.foreignKey(table, it))
            }
        }
    }

}
