package capy;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        DocumentManager documentManager = new DocumentManager();

        DocumentManager.Document document1 = DocumentManager.Document.builder()
                .title("Title 1")
                .content("Content 1")
                .author(DocumentManager.Author.builder().id("1").name("Author 1").build())
                .build();
        documentManager.save(document1);

        DocumentManager.Document document2 = DocumentManager.Document.builder()
                .title("Title 2")
                .content("Content 2")
                .author(DocumentManager.Author.builder().id("2").name("Author 2").build())
                .build();
        System.out.println(documentManager.save(document2));
        
        documentManager.findById(document2.getId()).ifPresent(System.out::println);
        documentManager.search(DocumentManager.SearchRequest.builder().titlePrefixes(List.of("Title")).build()).forEach(System.out::println);
    }
}

