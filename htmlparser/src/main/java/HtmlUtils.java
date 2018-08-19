import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Util class for processing html document.
 */
public class HtmlUtils {

    /**
     * Find element with specified id.
     *
     * @param id           id of element.
     * @param documentPath path to document.
     * @return element with specified id.
     */
    public static Element getElementById(String id, String documentPath) {
        Optional<Document> documentOptional = getDocumentByPath(documentPath);
        return documentOptional.map(document -> document.getElementById(id)).orElse(null);
    }

    /**
     * Find the most appropriate element with specified attributes.
     *
     * @param attributes   map with name of attribute and its value.
     * @param documentPath path to document.
     * @return the most appropriate element.
     */
    public static Element getElementByAttributes(Map<String, String> attributes, String documentPath) {
        Optional<Document> documentOptional = getDocumentByPath(documentPath);
        if (!documentOptional.isPresent()) {
            return null;
        }
        Document document = documentOptional.get();
        Map<Element, Integer> identityMap = new HashMap<>(document.getAllElements().stream().collect(Collectors.toMap(Function.identity(), item -> {
            int identityLevel = 0;
            for (Map.Entry<String, String> attribute : attributes.entrySet()) {
                if (item.attr(attribute.getKey()).contains(attribute.getValue())) {
                    identityLevel++;
                }
            }
            return identityLevel;
        })));
        return identityMap.entrySet().stream().min((elem1, elem2) -> elem2.getValue() - elem1.getValue()).map(Map.Entry::getKey).orElse(null);

    }

    /**
     * Print absolute path to element.
     *
     * @param element element for printing.
     */
    public static void printPath(Element element) {
        if (Objects.nonNull(element)) {
            Elements parents = element.parents();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = parents.size() - 1; i > 0; i--) {
                Element parent = parents.get(i);
                stringBuilder.append(parent.nodeName());
                if (parent.elementSiblingIndex() != 0) {
                    stringBuilder.append("[").append(parent.elementSiblingIndex()).append("]");
                }
                stringBuilder.append(" > ");
            }
            stringBuilder.append(element.nodeName());
            System.out.println(stringBuilder.toString());
        }
    }

    private static Optional<Document> getDocumentByPath(String path) {
        File htmlFile = new File(path);
        try {
            return Optional.ofNullable(Jsoup.parse(htmlFile, Charset.defaultCharset().name()));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
