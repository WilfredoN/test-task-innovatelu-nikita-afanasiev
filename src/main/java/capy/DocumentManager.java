package capy;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class DocumentManager {

    /**
     * Storage for documents, where String is for id, and Document for the document itself.
     */
    private final Map<String, Document> storage = new HashMap<>();

    /**
     * @param document - document content and author data
     * @return saved document
     */
    public Document save(Document document) {
        if (document.getId() == null) {
            document.setId(UUID.randomUUID().toString());
            document.setCreated(Instant.now());
        } else if (storage.containsKey(document.getId())) {
            Document preDocument = storage.get(document.getId());
            document.setCreated(preDocument.getCreated());
        }
        storage.put(document.getId(), document);

        return document;
    }

    /**
     * @param request - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest request) {
        return storage.values().stream()
                .filter(document -> request.getTitlePrefixes() == null || request.getTitlePrefixes().isEmpty() || request.getTitlePrefixes().stream().anyMatch(document.getTitle()::startsWith))
                .filter(document -> request.getContainsContents() == null || request.getContainsContents().isEmpty() || request.getContainsContents().stream().anyMatch(document.getContent()::contains))
                .filter(document -> request.getAuthorIds() == null || request.getAuthorIds().isEmpty() || request.getAuthorIds().contains(document.getAuthor().getId()))
                .filter(document -> request.getCreatedFrom() == null || document.getCreated().isAfter(request.getCreatedFrom()))
                .filter(document -> request.getCreatedTo() == null || document.getCreated().isBefore(request.getCreatedTo()))
                .collect(Collectors.toList());
    }

    /**
     * @param id - document id
     * @return document if it exists, empty otherwise.
     */
    public Optional<Document> findById(String id) {
        if (storage.containsKey(id)) {
            return Optional.of(storage.get(id));
        }

        return Optional.empty();
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}