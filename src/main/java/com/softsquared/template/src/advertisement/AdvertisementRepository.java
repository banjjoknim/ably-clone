package com.softsquared.template.src.advertisement;

import com.softsquared.template.DBmodel.Advertisement;
import com.softsquared.template.src.user.models.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementRepository extends CrudRepository<Advertisement, Integer> {

}
