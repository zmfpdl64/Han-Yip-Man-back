package com.supercoding.hanyipman.repository;

import com.supercoding.hanyipman.entity.Address;
import com.supercoding.hanyipman.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByBuyer(Buyer buyer);

    @Query("select count(a) from Address a join a.buyer b  where b.id=:buyer_id")
    Integer findAllByUserCountId(@Param("buyer_id") Long buyerId);


    @Query("select count(a) from Address a where a.address=:address")
    Integer existsAllByAddress(@Param("address") String address);


    @Query("select a from Address a where a.buyer = ?1 and a.id = ?2")
    Optional<Address> findByBuyerAndId(Buyer buyer, Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Address a where a.buyer=?1 and a.id = ?2 ")
    void deleteAddressByAddress(Buyer buyer, Long id);

    Address findAddressById(Long addressId);
}
