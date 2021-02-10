package com.softsquared.template.src.deliverydestination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {
    private final DeliveryProvider deliveryProvider;
    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryService(DeliveryProvider deliveryProvider , DeliveryRepository deliveryRepository){
        this.deliveryProvider = deliveryProvider;
        this.deliveryRepository = deliveryRepository;
    }

}
