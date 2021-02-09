package com.softsquared.template.src.purchase;

import com.softsquared.template.DBmodel.Purchase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Integer> {

}
