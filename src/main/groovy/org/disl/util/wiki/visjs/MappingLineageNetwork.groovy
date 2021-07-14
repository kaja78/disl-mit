package org.disl.util.wiki.visjs

import org.disl.meta.Lookup
import org.disl.meta.Mapping
import org.disl.meta.Table
import org.disl.meta.TableMapping
import org.disl.util.doc.MetaManager

/**
 * Helper for building lineage model for vis.js network visualisation. See http://visjs.org/docs/network/.
 */
class MappingLineageNetwork {

    private int maxDepth = 10
    Set<LineageNode> nodes = new HashSet<LineageNode>();
    List<Edge> edges = [];

    MappingLineageNetwork(Table table) {
        initLineage(table, 1)
    }

    MappingLineageNetwork(Mapping mapping) {
        if (mapping instanceof TableMapping) {
            Node target = new LineageNode(mapping.target, 0)
            nodes.add target
            edges.add Edge.mappingTarget(mapping, mapping.target)
        }
        initLineage(mapping, 1)
    }

    boolean initLineage(Mapping mapping, int depth) {
        if (depth > maxDepth) {
            return false
        }
        Node node = new LineageNode(mapping, depth)
        if (depth == 1) {
            node.color = 'Red'
        }


        MetaManager.getSourceDependencies(mapping).each {
            edges.add Edge.mappingSource(it, mapping)
            initLineage(it, depth + 1)
        }

        return nodes.add(node)
    }

    boolean initLineage(Table table, int depth) {
        if (depth > maxDepth) {
            return false
        }
        return nodes.add(new LineageNode(table, depth))

    }

    boolean initLineage(Lookup lookup, int depth) {
        if (depth > maxDepth) {
            return false
        }
        return nodes.add(new LineageNode(lookup, depth))

    }

}
