package iojjj.androidbootstrap.appcompat.utils;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class SharedPreferencesHelper {

    private static final String STRING_SET_ROOT      = "set";
    private static final String STRING_SET_ELEMENT   = "item";
    private static final String STRING_SET_ATTRIBUTE = "v";

    private static String toXMLString(Set<String> stringSet) {
        if (stringSet == null || stringSet.isEmpty())
            return "";

        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            Element root = doc.createElement(STRING_SET_ROOT);
            for (String s : stringSet) {
                Element item = doc.createElement(STRING_SET_ELEMENT);
                item.setAttribute(STRING_SET_ATTRIBUTE, s);

                root.appendChild(item);
            }
            doc.appendChild(root);

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            return writer.toString();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (TransformerException e) {
	        e.printStackTrace();
	        return null;
        }
    }

    private static Set<String> fromXMLString(String plainStringSet) {
        if (TextUtils.isEmpty(plainStringSet))
            return new HashSet<String>(0);

        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new InputSource(new StringReader(plainStringSet)));

            final NodeList nodes = doc.getElementsByTagName(STRING_SET_ELEMENT);

            String value;
            Set<String> stringSet = new HashSet<String>(nodes.getLength());
            for (int i = 0, imax = nodes.getLength(); i < imax; ++i) {
                Node n = nodes.item(i);

                if (STRING_SET_ELEMENT.equals(n.getNodeName())) {
                    NamedNodeMap nnm = n.getAttributes();

                    if (nnm == null)
                        continue;

                    Node item = nnm.getNamedItem(STRING_SET_ATTRIBUTE);
                    if (item == null || TextUtils.isEmpty(value = item.getNodeValue()))
                        continue;

                    stringSet.add(value);
                }
            }
            return stringSet;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
	        e.printStackTrace();
	        return null;
        } catch (IOException e) {
	        e.printStackTrace();
	        return null;
        }
    }

    public static SharedPreferences.Editor putStringSet(@NonNull SharedPreferences.Editor editor, String key, Set<String> stringSet) {
        if (key == null && stringSet == null)
            return editor;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            final String plainStringSet = toXMLString(stringSet);

            return TextUtils.isEmpty(plainStringSet)
                    ? editor : editor.putString(key, plainStringSet);
        } else {
            return editor.putStringSet(key, stringSet);
        }
    }

    public static Set<String> getStringSet(SharedPreferences prefs, String key, Set<String> defValues) {
        if (prefs == null || key == null)
            return null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            final String plainStringSet = prefs.getString(key, null);

            return !TextUtils.isEmpty(plainStringSet)
                    ? fromXMLString(plainStringSet) : new HashSet<String>(0);
        } else {
            return prefs.getStringSet(key, defValues);
        }
    }

}
