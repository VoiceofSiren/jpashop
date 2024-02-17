package jpabook.jpashop.domain;

import jakarta.persistence.*;
//import jpabook.jpashop.domain.item.CategoryItem;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

/*    @OneToMany(mappedBy = "category")
    private List<CategoryItem> itemCategories = new ArrayList<>();*/

    @ManyToMany
    @JoinTable(name = "category_item",
               joinColumns = {@JoinColumn(name = "category_id")},
               inverseJoinColumns = {@JoinColumn(name = "item_id")})
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    //==연관 관계 메서드==//
    public void addChildCategory(Category child) {
        this.children.add(child);
        child.setParent(this);
    }

}


// Category -> @OneToMany, Item -> @OneToMany
/*
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
*/
