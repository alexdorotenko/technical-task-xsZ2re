import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;

public class Demo {

    public static void main(String[] args) {
        String originalElementId = args[0];
        String originalHtml = args[1];
        Element element = HtmlUtils.getElementById(originalElementId, originalHtml);
        System.out.println(element);
        HtmlUtils.printPath(element);
        Map<String, String> attributesMap = new HashMap<>();
        for (Attribute attribute : element.attributes()) {
            attributesMap.put(attribute.getKey(), attribute.getValue());
        }
        for (int i = 2; i < args.length; i++) {
            HtmlUtils.printPath(HtmlUtils.getElementByAttributes(attributesMap, args[i]));
        }
    }
}
