package org.zafu.bookservice.repository.criteria;

import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zafu.bookservice.exception.AppException;
import org.zafu.bookservice.exception.ErrorCode;
import org.zafu.bookservice.models.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class FilterCriteriaConsumer implements Consumer<FilterCriteria> {
    private static final Logger log = LoggerFactory.getLogger(FilterCriteriaConsumer.class);
    private CriteriaBuilder builder;
    private Root<?> root;
    private List<Predicate> predicates;

    public FilterCriteriaConsumer(CriteriaBuilder builder, Root<?> root){
        this.builder = builder;
        this.root = root;
        this.predicates  = new ArrayList<>();
    }

    @Override
    public void accept(FilterCriteria param) {
        log.info(param.getKey());
        String[] keyParts = param.getKey().split("\\.");
        for(String s:keyParts) log.info("key {}", s);
        if(keyParts.length == 1){
            String fieldName = keyParts[0];
            applyPredicate(root, fieldName, param.getOperator(), param.getValue());
        }else if(keyParts.length == 2){
            String entityName = keyParts[0];
            String fieldName = keyParts[1];
            Join<Book, ?> join = root.join(entityName);
            applyPredicate(join, fieldName, param.getOperator(), param.getValue());
        }else{
            throw new AppException(ErrorCode.INVALID_KEY_FILTER);
        }
    }

    private void applyPredicate(Path<?> path, String fieldName, String operator, Object value){
        switch (operator){
            case ":" -> {
                if(path.get(fieldName).getJavaType().equals(String.class)){
                    predicates.add(
                            builder.like(
                                    path.get(fieldName),
                                    "%" + value.toString() + "%"
                            )
                    );
                }else{
                    predicates.add(
                            builder.equal(path.get(fieldName), value)
                    );
                }
            }
            case "!" ->
                    predicates.add(
                            builder.notEqual(path.get(fieldName), value)
                    );

            case ">" ->{
                if (path.get(fieldName).getJavaType().equals(Integer.class)) {
                    predicates.add(builder.greaterThanOrEqualTo(
                            path.get(fieldName),
                            Integer.parseInt(value.toString())
                    ));
                } else {
                    predicates.add(builder.greaterThanOrEqualTo(
                            path.get(fieldName),
                            value.toString()
                    ));
                }
            }
            case "<" ->{
                if (path.get(fieldName).getJavaType().equals(Integer.class)) {
                    predicates.add(builder.lessThanOrEqualTo(
                            path.get(fieldName),
                            Integer.parseInt(value.toString())
                    ));
                } else {
                    predicates.add(builder.lessThanOrEqualTo(
                            path.get(fieldName),
                            value.toString()
                    ));
                }
            }
            default -> throw new AppException(ErrorCode.INVALID_OPERATOR_FILTER);
        }
    }

    public Predicate getPredicate(){
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
