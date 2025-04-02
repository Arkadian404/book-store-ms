package org.zafu.bookservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zafu.bookservice.dto.request.PublisherRequest;
import org.zafu.bookservice.dto.response.PublisherResponse;
import org.zafu.bookservice.exception.AppException;
import org.zafu.bookservice.exception.ErrorCode;
import org.zafu.bookservice.mapper.PublisherMapper;
import org.zafu.bookservice.models.Publisher;
import org.zafu.bookservice.repository.PublisherRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PUBLISHER-SERVICE")
public class PublisherService {
    private final PublisherRepository repository;
    private final PublisherMapper mapper;

    public PublisherResponse createPublisher(PublisherRequest request){
        Publisher publisher = mapper.toPublisher(request);
        return mapper.toPublisherResponse(repository.save(publisher));
    }

    public PublisherResponse updatePublisher(int id, PublisherRequest request){
        Publisher publisher = repository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.PUBLISHER_NOT_EXISTED));
        mapper.updatePublisher(publisher, request);
        return mapper.toPublisherResponse(repository.save(publisher));
    }

    public void deletePublisher(int id){
        repository.deleteById(id);
    }

    public List<PublisherResponse> getAll(){
        List<Publisher> publishers = repository.findAll();
        return publishers
                .stream()
                .map(mapper::toPublisherResponse)
                .toList();
    }

    public PublisherResponse getById(int id){
        Publisher publisher = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_EXISTED));
        return mapper.toPublisherResponse(repository.save(publisher));
    }
}
