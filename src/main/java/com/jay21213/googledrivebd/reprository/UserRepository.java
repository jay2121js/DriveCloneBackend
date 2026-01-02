package com.jay21213.googledrivebd.reprository;

import com.jay21213.googledrivebd.DTO.UserData;
import com.jay21213.googledrivebd.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends MongoRepository<User, String>  {

        User findByEmail(String email);


    @Query(
            value = "{ '_id': ?0 }",
            fields = "{ 'totalStorageLimit': 1, 'usedStorage': 1 }"
    )
    UserData findUserData(String userId);


}






