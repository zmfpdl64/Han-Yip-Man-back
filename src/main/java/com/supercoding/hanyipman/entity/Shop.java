package com.supercoding.hanyipman.entity;

import com.supercoding.hanyipman.dto.shop.seller.request.RegisterShopRequest;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "shop")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "phone_num", nullable = false, length = 20)
    private String phoneNum;

    @Column(name = "min_order_price", nullable = false)
    private Integer minOrderPrice;

    @Column(name = "default_delivery_price", nullable = false)
    private Integer defaultDeliveryPrice;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Lob
    @Setter
    @Column(name = "thumbnail")
    private String thumbnail;

    @Lob
    @Setter
    @Column(name = "banner")
    private String banner;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuGroup> menuGroups = new ArrayList<>();

    @OneToOne(mappedBy = "shop",cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    public void addMenuGroup(MenuGroup menuGroup) {
        menuGroup.setShop(this);
        menuGroups.add(menuGroup);
    }

    public void removeMenuGroup(MenuGroup menuGroup) {
        menuGroups.remove(menuGroup);
        menuGroup.setShop(null);
    }

    public MenuGroup getMenuGroupById(Long menuGroupId) {
        return menuGroups.stream()
                .filter(menuGroup -> menuGroup.getId().equals(menuGroupId))
                .findFirst()
                .orElse(null);
    }

    public void setAddress(Address address) {
        if (address != null) {
            address.setShop(this);
            this.address = address;
        }
    }

    public static Shop from(RegisterShopRequest registerShopRequest, Seller seller, Category category) {
        return Shop.builder()
                .category(category)
                .seller(seller)
                .name(registerShopRequest.getShopName())
                .phoneNum(registerShopRequest.getShopPhone())
                .minOrderPrice(registerShopRequest.getMinOrderPrice())
                .defaultDeliveryPrice(2000)
                .description(registerShopRequest.getShowDescription())
                .build();
    }


}