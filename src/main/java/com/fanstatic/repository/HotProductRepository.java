package com.fanstatic.repository;

import com.fanstatic.model.ComboProduct;
import com.fanstatic.model.HotProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import com.fanstatic.model.Product;
import com.google.common.base.Optional;

public interface HotProductRepository extends JpaRepository<HotProduct, Integer> {

    public Optional<HotProduct> findByProduct(Product product);

    public Optional<HotProduct> findByComboProduct(ComboProduct comboProduct);

}
