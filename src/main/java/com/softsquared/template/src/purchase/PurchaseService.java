package com.softsquared.template.src.purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseProvider purchaseProvider;

    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseProvider purchaseProvider){
        this.purchaseRepository = purchaseRepository;
        this.purchaseProvider = purchaseProvider;

    }
}
