package net.luminis.xmlbuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Embarrassingly simple xml writer, inspired by the Groovy XmlBuilder. Not much better than generating by hand,
 * but at least it saves you having to close all tags.
 */
public class Xml {
    public static XmlWithChildren xml(String node) {
        return new XmlWithChildren(node);
    }

    public static XmlWithValue xml(String node, String value) {
        return xml(node, value, false);
    }

    public static XmlWithValue xml(String node, String value, boolean cdata) {
        return new XmlWithValue(node, value, cdata);
    }
    
    private static String join(Collection<?> collection, String separator) {
        StringBuilder sb = new StringBuilder();
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
            if (it.hasNext()) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    private static class Base<SUBTYPE extends Base<SUBTYPE>> extends Xml {
        private final String m_node;
        private final List<String> m_attributes = new ArrayList<String>();

        private Base(String node) {
            m_node = node;
        }

        @SuppressWarnings("unchecked")
        public SUBTYPE attribute(String key, String value) {
            m_attributes.add(String.format("%s=\"%s\"", key, value));
            return (SUBTYPE) this;
        }

        protected String emptyNode() {
            return "<" + nodeWithAttributes() + "/>";
        }

        protected String startNode() {
            return "<" + nodeWithAttributes() + ">";
        }

        protected String endNode() {
            return "</" + m_node + ">";
        }

        private String nodeWithAttributes() {
            if (m_attributes.size() == 0) {
                return m_node;
            }
            else {
                return m_node + " " + join(m_attributes, " ");
            }
        }
    }

    public static class XmlWithValue extends Base<XmlWithValue> {
        private final String m_value;
		private final boolean m_cdata;

        public XmlWithValue(String node, String value, boolean cdata) {
            super(node);
            m_value = value;
			m_cdata = cdata;
        }
        
        @Override
        public String toString() {
            if (m_value == null) {
                return emptyNode();
            }
            else {
            	if (m_cdata) {
            		return startNode() + cdata(m_value) + endNode();
            	}
            	else {
            		return startNode() + m_value + endNode();
            	}
            }
        }
        
        private String cdata(String value) {
        	return "<![CDATA[" + value + "]]>";
        }
    }
    
    public static class XmlWithChildren extends Base<XmlWithChildren> {
        private final List<Xml> m_children = new ArrayList<Xml>();
        
        public XmlWithChildren(String node) {
            super(node);
        }

        public XmlWithChildren add(Xml xml) {
            m_children.add(xml);
            return this;
        }
        
        @Override
        public String toString() {
            if (m_children.isEmpty()) {
                return emptyNode();
            }
            else {
                return startNode() + children() + endNode();
            }
        }

        private StringBuilder children() {
            StringBuilder result = new StringBuilder();
            for (Xml child : m_children) {
                result.append(child.toString());
            }
            return result;
        }
    }
    
}

