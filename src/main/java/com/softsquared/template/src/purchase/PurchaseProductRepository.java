package com.softsquared.template.src.purchase;

import com.softsquared.template.DBmodel.Purchase;
import com.softsquared.template.DBmodel.PurchaseProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseProductRepository extends CrudRepository<PurchaseProduct, Integer> {

}
