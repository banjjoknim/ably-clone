package com.softsquared.template.src.deliverydestination;

import com.softsquared.template.DBmodel.DeliveryDestination;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends CrudRepository<DeliveryDestination,Integer> {
}
