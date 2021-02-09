package com.softsquared.template.src.advertisement;

import com.softsquared.template.DBmodel.Advertisement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


public interface AdvertisementRepository extends CrudRepository<Advertisement, Integer> {

}
