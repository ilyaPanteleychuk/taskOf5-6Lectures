package ilya.profitsoft.task1.service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


public class XmlFineFileProcessor {
    
    public void writeFinesToXml(Map<String, Double> sortedFines, File fileTo) {
        try (FileOutputStream outputStream = new FileOutputStream(fileTo)) {
            Document document = constructXMLDocument(sortedFines);
            writeXml(document, outputStream);
        } catch (IOException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Document constructXMLDocument(Map<String, Double> sortedFines) {
        Document document;
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();
            Element rootElement = document.createElement("fines");
            for (Map.Entry<String, Double> entry : sortedFines.entrySet()) {
                Element fine = document.createElement("fine");
                Element fineTitle = document.createElement("title");
                fineTitle.setTextContent(entry.getKey());
                Element fineAmount = document.createElement("amount");
                fineAmount.setTextContent(String.valueOf(entry.getValue()));
                fine.appendChild(fineTitle);
                fine.appendChild(fineAmount);
                rootElement.appendChild(fine);
            }
            document.appendChild(rootElement);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        return document;
    }
    
    private static void writeXml(Document document, OutputStream outputStream)
            throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult streamResult = new StreamResult(outputStream);
        transformer.transform(source, streamResult);
    }
}
