package club.smartbus.dal;

import club.smartbus.data.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, String> {

    @Query("INSERT INTO users (organization_email, first_name, surname, company, password, role) " +
            "VALUES (:#{#user.organizationEmail}, :#{#user.firstName}, :#{#user.surname}, :#{#user.company}, :#{#user.password}, :#{#user.role}) " +
            "ON CONFLICT (organization_email) DO UPDATE " +
            "SET first_name = EXCLUDED.first_name, " +
            "surname = EXCLUDED.surname, " +
            "company = EXCLUDED.company, " +
            "password = EXCLUDED.password, " +
            "role = EXCLUDED.role " +
            "RETURNING *")
    Mono<UserEntity> insertUser(UserEntity user);  // Return affected rows (not the entity)
}
