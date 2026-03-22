package org.zafu.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zafu.orderservice.client.BookClient;
import org.zafu.orderservice.client.CartClient;
import org.zafu.orderservice.dto.PageResponse;
import org.zafu.orderservice.dto.request.*;
import org.zafu.orderservice.dto.response.*;
import org.zafu.orderservice.exception.AppException;
import org.zafu.orderservice.exception.ErrorCode;
import org.zafu.orderservice.mapper.OrderMapper;
import org.zafu.orderservice.model.Order;
import org.zafu.orderservice.model.OrderItem;
import org.zafu.orderservice.model.OrderStatus;
import org.zafu.orderservice.repository.OrderRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ORDER-SERVICE")
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartClient cartClient;
    private final BookClient bookClient;
    private final OrderMapper orderMapper;
    private final List<OrderPayment> opList;


    private record CartPreparation(CartResponse cartResponse, Map<Integer, BookResponse> booksById) {}

    public List<OrderResponse> getAll(){
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(item -> orderMapper.toOrderResponse(item, bookClient))
                .toList();
    }


    @Transactional
    public OrderResponse createOrder(Integer userId, CreateOrderRequest request){
        CartPreparation cartPreparation = prepareCart(userId);
        Order order = createPendingOrder(userId, request, cartPreparation);
        List<OrderItem> items = order.getItems();

        OrderPayment op = opList.stream()
                .filter(f -> f.supports() == request.getPaymentMethod())
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_METHOD_NOT_SUPPORTED));
        return op.process(order, items);
    }

    private CartPreparation prepareCart(Integer userId) {
        CartResponse cartResponse = cartClient.getCartByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND))
                .getResult();

        if (cartResponse.getItems().isEmpty()) {
            throw new AppException(ErrorCode.CART_IS_EMPTY);
        }
        Map<Integer, BookResponse> booksById = new LinkedHashMap<>();

        for (CartItemResponse item : cartResponse.getItems()){
            BookResponse bookResponse = bookClient.getBookById(item.getBookId())
                    .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND))
                    .getResult();
            if (bookResponse.getStockQuantity() < item.getQuantity()){
                throw new AppException(ErrorCode.STOCK_NOT_ENOUGH);
            }
            booksById.put(item.getBookId(), bookResponse);
        }
        return new CartPreparation(cartResponse, booksById);
    }

    private Order createPendingOrder(Integer userId, CreateOrderRequest request, CartPreparation cartPreparation){
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.PENDING);
        order.setFirstname(request.getFirstname());
        order.setLastname(request.getLastname());
        order.setEmail(request.getEmail());
        order.setPhone(request.getPhone());
        order.setAddress(request.getAddress());
        order.setProvince(request.getProvince());
        order.setDistrict(request.getDistrict());
        order.setWard(request.getWard());
        order.setNotes(request.getNotes());
        order.setPaymentMethod(request.getPaymentMethod());

       List<OrderItem> orderItems = cartPreparation.cartResponse().getItems().stream()
               .map(item -> toOrderItem(order, item, cartPreparation.booksById()))
               .collect(Collectors.toCollection(ArrayList::new));

        order.setItems(orderItems);
        order.setTotalAmount(orderItems.stream()
                .mapToDouble(item -> item.getBookPrice() * item.getBookQuantity())
                .sum());
        return orderRepository.save(order);
    }

    private OrderItem toOrderItem(Order order, CartItemResponse item, Map<Integer, BookResponse> booksById){
        BookResponse bookResponse = booksById.get(item.getBookId());
        if (bookResponse == null) {
            throw new AppException(ErrorCode.BOOK_NOT_FOUND);
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setBookId(item.getBookId());
        orderItem.setBookQuantity(item.getQuantity());
        orderItem.setBookPrice(bookResponse.getPrice());
        orderItem.setOrder(order);
        return orderItem;
    }

    public PageResponse<OrderResponse> getAllOrdersPaging(int page, int size){
        if(page < 1 || size < 1){
            throw new AppException(ErrorCode.PAGE_OR_SIZE_MUST_BE_VALID);
        }
        Pageable pageable = PageRequest.of(page - 1, size)
                .withSort(Sort.Direction.DESC, "createdAt");
        Page<Order> orderPage = orderRepository.findAll(pageable);
        List<OrderResponse> orderResponses = orderPage.getContent().stream()
                .map(item -> orderMapper.toOrderResponse(item, bookClient))
                .toList();
        return PageResponse.<OrderResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .data(orderResponses)
                .totalPages(orderPage.getTotalPages())
                .totalElements(orderPage.getTotalElements())
                .build();
    }

    public PageResponse<OrderResponse> getAllUserOrdersPaging(Integer userId, int page, int size){
        if(page < 1 || size < 1){
            throw new AppException(ErrorCode.PAGE_OR_SIZE_MUST_BE_VALID);
        }
        Pageable pageable = PageRequest.of(page - 1, size)
                .withSort(Sort.Direction.DESC, "createdDate");
        Page<Order> orderPage = orderRepository.findAllByUserId(userId, pageable);
        List<OrderResponse> orderResponses = orderPage.getContent().stream()
                .map(item -> orderMapper.toOrderResponse(item, bookClient))
                .toList();
        return PageResponse.<OrderResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .data(orderResponses)
                .totalPages(orderPage.getTotalPages())
                .totalElements(orderPage.getTotalElements())
                .build();
    }

    public OrderResponse getOrderByOrderCode(String orderCode){
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toOrderResponse(order, bookClient);
    }


    @Transactional
    public void updateOrderStatus(String orderCode, UpdateOrderStatusRequest request){
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (!order.getStatus().canTransition(request.getStatus())) {
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS_TRANSITION);
        }
        order.setStatus(request.getStatus());
        orderRepository.save(order);
    }

}
