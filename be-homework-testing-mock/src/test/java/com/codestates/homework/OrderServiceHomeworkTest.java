package com.codestates.homework;

import com.codestates.exception.BusinessLogicException;
import com.codestates.order.entity.Order;
import com.codestates.order.repository.OrderRepository;
import com.codestates.order.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Executable;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class OrderServiceHomeworkTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;
    @Test
    public void cancelOrderTest() {
        // TODO OrderService의 cancelOrder() 메서드를 테스트하는 테스트 케이스를 여기에 작성하세요.
        // TODO Mockito를 사용해야 합니다. ^^
        Order order = new Order(1L, Order.OrderStatus.ORDER_CONFIRM);
        BDDMockito.given(orderRepository.findById(Mockito.anyLong())).willReturn(Optional.of(order));

        assertThrows(BusinessLogicException.class, () -> orderService.cancelOrder(order.getOrderId()));
    }
}
