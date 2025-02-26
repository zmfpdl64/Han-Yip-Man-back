package com.supercoding.hanyipman.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "menu_group")
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "menuGroup",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus  = new ArrayList<>();


    public static MenuGroup from(Shop shop, String name, Integer sequence) {
        return MenuGroup.builder()
                .shop(shop)
                .name(name)
                .sequence(sequence)
                .build();
    }

}