package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.ItemUpdateDTO;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional(readOnly = false)
    public Long save(Item item) {
        return itemRepository.save(item);
    }

    @Transactional
    public void updateBook(Long itemId, ItemUpdateDTO itemUpdateDTO) {

        // 영속 상태
        Book foundBook = (Book) itemRepository.findOne(itemId);
        foundBook.setName(itemUpdateDTO.getName());
        foundBook.setPrice(itemUpdateDTO.getPrice());
        foundBook.setStockQuantity(itemUpdateDTO.getStockQuantity());
        foundBook.setAuthor(itemUpdateDTO.getAuthor());
        foundBook.setIsbn(itemUpdateDTO.getIsbn());

        // Dirty check -> em.persist() 및 tx.commit() 필요 없이 자동으로 update됨.
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }
}
