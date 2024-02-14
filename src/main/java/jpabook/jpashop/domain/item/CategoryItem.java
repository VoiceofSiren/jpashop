package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CategoryItem {

    @Id
    @GeneratedValue
    @Column(name = "item_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
