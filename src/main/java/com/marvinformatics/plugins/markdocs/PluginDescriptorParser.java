package com.marvinformatics.plugins.markdocs;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class PluginDescriptorParser {

    public PluginDescriptor parse(File pluginDescriptorFile) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(pluginDescriptorFile);
        doc.getDocumentElement().normalize();

        PluginDescriptor descriptor = new PluginDescriptor();

        Element root = doc.getDocumentElement();
        
        descriptor.setGroupId(getTextContent(root, "groupId"));
        descriptor.setArtifactId(getTextContent(root, "artifactId"));
        descriptor.setVersion(getTextContent(root, "version"));
        descriptor.setName(getTextContent(root, "name"));
        descriptor.setDescription(getTextContent(root, "description"));

        NodeList mojosList = root.getElementsByTagName("mojos");
        if (mojosList.getLength() > 0) {
            Element mojosElement = (Element) mojosList.item(0);
            NodeList mojoNodes = mojosElement.getElementsByTagName("mojo");
            
            for (int i = 0; i < mojoNodes.getLength(); i++) {
                Element mojoElement = (Element) mojoNodes.item(i);
                Goal goal = parseMojo(mojoElement);
                descriptor.addGoal(goal);
            }
        }

        return descriptor;
    }

    private Goal parseMojo(Element mojoElement) {
        Goal goal = new Goal();
        
        goal.setName(getTextContent(mojoElement, "goal"));
        goal.setDescription(getTextContent(mojoElement, "description"));
        goal.setImplementation(getTextContent(mojoElement, "implementation"));
        goal.setPhase(getTextContent(mojoElement, "phase"));

        NodeList parametersNodeList = mojoElement.getElementsByTagName("parameters");
        if (parametersNodeList.getLength() > 0) {
            Element parametersElement = (Element) parametersNodeList.item(0);
            NodeList parameterNodes = parametersElement.getElementsByTagName("parameter");
            
            for (int i = 0; i < parameterNodes.getLength(); i++) {
                Element paramElement = (Element) parameterNodes.item(i);
                Parameter parameter = parseParameter(paramElement);
                goal.addParameter(parameter);
            }
        }

        return goal;
    }

    private Parameter parseParameter(Element paramElement) {
        Parameter parameter = new Parameter();
        
        parameter.setName(getTextContent(paramElement, "name"));
        parameter.setType(getTextContent(paramElement, "type"));
        parameter.setDescription(getTextContent(paramElement, "description"));
        parameter.setDefaultValue(getTextContent(paramElement, "defaultValue"));
        
        String required = getTextContent(paramElement, "required");
        parameter.setRequired("true".equals(required));
        
        String editable = getTextContent(paramElement, "editable");
        parameter.setEditable(!"false".equals(editable));

        return parameter;
    }

    private String getTextContent(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node != null) {
                return node.getTextContent();
            }
        }
        return null;
    }
}