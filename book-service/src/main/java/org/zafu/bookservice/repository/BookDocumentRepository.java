package org.zafu.bookservice.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.zafu.bookservice.models.BookDocument;

public interface BookDocumentRepository extends ElasticsearchRepository<BookDocument, Integer> {
}
