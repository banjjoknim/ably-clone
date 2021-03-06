package com.softsquared.template.src.user;

import com.softsquared.template.DBmodel.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // => JPA => Hibernate => ORM => Database 객체지향으로 접근하게 해주는 도구이다
public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
//    List<UserInfo> findByStatusAndUserIdStatus(int status, long userId);
//    List<UserInfo> findByEmailAndStatus(String email, String status);
//    List<UserInfo> findByStatusAndNicknameIsContaining(String status, String word);
}