package com.supercoding.hanyipman.service;

import com.supercoding.hanyipman.dto.address.request.ShopAddressRequest;
import com.supercoding.hanyipman.dto.Shop.seller.request.RegisterShopRequest;
import com.supercoding.hanyipman.dto.Shop.seller.response.ShopDetailResponse;
import com.supercoding.hanyipman.dto.Shop.seller.response.ShopManagementListResponse;
import com.supercoding.hanyipman.entity.*;
import com.supercoding.hanyipman.error.CustomException;
import com.supercoding.hanyipman.error.domain.*;
import com.supercoding.hanyipman.repository.CategoryRepository;
import com.supercoding.hanyipman.repository.SellerRepository;
import com.supercoding.hanyipman.repository.shop.ShopRepository;
import com.supercoding.hanyipman.repository.UserRepository;
import com.supercoding.hanyipman.enums.FilePath;
import com.supercoding.hanyipman.security.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerShopService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ShopRepository shopRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    public void registerShop(RegisterShopRequest registerShopRequest, ShopAddressRequest shopAddressRequest, MultipartFile bannerFile, MultipartFile thumbnailFile, User user) {

        Seller seller = validSellerUser(user);
        Category category = categoryRepository.findById(registerShopRequest.getCategoryId()).orElseThrow(() -> new CustomException(ShopErrorCode.NOT_FOUND_CATEGORY));
        Shop shop = Shop.from(registerShopRequest, seller, category);

        shopRepository.save(shop);
        shop.setBanner(uploadImageFile(bannerFile, shop));
        shop.setThumbnail(uploadImageFile(thumbnailFile, shop));
        shop.setAddress(Address.from(shopAddressRequest));
    }

    public void deleteShop(Long shopId, User user) {

        Seller seller = validSellerUser(user);
        Shop shop = validShop(shopId, seller.getId());
        shopRepository.deleteById(shop.getId());
    }


    public List<ShopManagementListResponse> findManagementList(User user) {
        Seller seller = validSellerUser(user);

        List<Shop> shopList = shopRepository.findAllBySellerAndIsDeletedFalse(seller);

        return shopList.stream().map(ShopManagementListResponse::from).collect(Collectors.toList());
    }

    public ShopDetailResponse detailShop(Long shopId) {

        Seller seller = validSellerUser(JwtToken.user());
        Shop shop = validShop(shopId, seller.getId());
        return ShopDetailResponse.from(shop);
    }

    private String uploadImageFile(MultipartFile multipartFile, Shop shop) {
        String uniqueIdentifier = UUID.randomUUID().toString();
        try {
            if (multipartFile != null) {
                return awsS3Service.uploadImage(multipartFile, FilePath.SHOP_DIR.getPath() + shop.getId() + "/" + uniqueIdentifier);
            }
        }catch (IOException e) {
            throw new CustomException(FileErrorCode.FILE_UPLOAD_FAILED);
        }
        return null;
    }

    private Seller validSellerUser(User user) {
        return sellerRepository.findByUser(user).orElseThrow(() -> new CustomException(SellerErrorCode.NOT_SELLER));
    }

    private Shop validShop(Long shopId, Long sellerId) {
        Shop shop = shopRepository.findShopByShopId(shopId).orElseThrow(() -> new CustomException(ShopErrorCode.NOT_FOUND_SHOP));
        if (!Objects.equals(shop.getSeller().getId(), sellerId)) throw new CustomException(ShopErrorCode.DIFFERENT_SELLER);
        return shop;
    }



}
