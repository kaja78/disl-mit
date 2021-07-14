package org.disl.util.wiki.visjs

import org.disl.meta.Lookup
import org.disl.meta.Mapping
import org.disl.meta.Table
import org.disl.util.wiki.WikiHelper

/**
 * Representation of vis.js network Node.
 */
class Node {
    String id
    String label
    String title
    String color = 'LightGrey'
    String shape = 'box'
    String targetUrl
    int size = 25


    Node(Table t) {
        id = t.class.name
        label = t.toString()
        title = t.description
        targetUrl = WikiHelper.url(t)
        try {
            switch (t['stereotype']) {
                case 'dimension':
                    color = 'LightBlue'
                    break
                case 'fact':
                    color = 'Yellow'
                    break
            }
        } catch (MissingPropertyException e) {
            //do nothing
        }

    }

    Node(Mapping m) {
        id = m.class.name
        label = m.toString()
        title = m.description
        color = 'LightGreen'
        shape = 'ellipse'
        targetUrl = WikiHelper.url(m)
    }

    Node(Lookup l) {
        id = l.class.name
        label = l.toString()
        title = l.description
        color = 'DarkGreen'
        shape = 'ellipse'
        targetUrl = WikiHelper.url(l)
    }

    @Override
    boolean equals(def obj) {
        if (obj instanceof Node) {
            return id.equals(obj.id)
        }
        return false
    }

    @Override
    int hashCode() {
        return id.hashCode()
    }
}
