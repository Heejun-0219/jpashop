package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     *
     * @param memberId
     * @param itemId
     * @param count
     * @return orderId
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // Entity 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order); // CASCADE를 사용했기 떄문에 orderRepository에만 save를 해줘도 다른 리포지토리에도 저장이 된다.
        // CASCADE의 범위를 잘 생각해야한다. persist의 생명주기가 같은 것끼리만, 연결해서 사용하는 것이다.

        return order.getId();
    }

    /**
     * 주문 취소
     *
     * @param orderId
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 최소
        order.cancel();
        // JPA를 활용하면, 데이터만 바꿔주면, 더티체크를 활용해서 변경된 내역을 확인해서 변경된 내역으로 데이터베이스에 쿼리를 자동으로 날려준다.
        // 따라서, 해당 비즈니스 로직에서 쿼리를 작성하지 않아도 된다.
    }

    // 검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch);
//    }
}
