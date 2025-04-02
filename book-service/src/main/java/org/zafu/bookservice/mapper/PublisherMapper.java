package org.zafu.bookservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.zafu.bookservice.dto.request.PublisherRequest;
import org.zafu.bookservice.dto.response.PublisherResponse;
import org.zafu.bookservice.models.Publisher;

@Mapper(componentModel = "spring")
public interface PublisherMapper {
    Publisher toPublisher(PublisherRequest request);
    void updatePublisher(@MappingTarget Publisher author, PublisherRequest request);
    PublisherResponse toPublisherResponse(Publisher author);
}
