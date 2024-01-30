package jpabook.jpashop.domain.item;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter // -> 세터를 사용하지 않고 비즈니스 로직을 활용해서 프로퍼티를 수정해줘야한다.
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items", fetch = FetchType.LAZY)
    private List<Category> categories = new ArrayList<>();

    // 비즈니스 로직

    /**
     * stock 증가
     *
     * @param quantity
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     *
     * @param quantity
     */
    public void removeStock(int quantity) {
        if (this.stockQuantity - quantity < 0) {
            throw new NotEnoughStockException("Need more Stock.");
        }
        this.stockQuantity -= quantity;
    }
}
