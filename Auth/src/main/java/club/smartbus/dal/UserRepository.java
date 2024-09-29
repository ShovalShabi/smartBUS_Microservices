package club.smartbus.dal;

import club.smartbus.data.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Repository interface for performing database operations on the {@link UserEntity} table.
 * This repository is used to handle user data storage, including insert and update operations.
 */
@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, String> {

    /**
     * Inserts a new user into the database or updates the user if it already exists.
     * <p>
     * This method attempts to insert a new user, and if a conflict occurs on the
     * 'organization_email' column (i.e., the user already exists), the user is updated
     * with the new values for first name, surname, company, password, and role.
     * </p>
     *
     * @param user The {@link UserEntity} object containing the user's information.
     * @return A {@link Mono} emitting the saved or updated {@link UserEntity} object.
     */
    @Query("INSERT INTO users (organization_email, first_name, surname, company, password, role) " +
            "VALUES (:#{#user.organizationEmail}, :#{#user.firstName}, :#{#user.surname}, :#{#user.company}, :#{#user.password}, :#{#user.role}) " +
            "ON CONFLICT (organization_email) DO UPDATE " +
            "SET first_name = EXCLUDED.first_name, " +
            "surname = EXCLUDED.surname, " +
            "company = EXCLUDED.company, " +
            "password = EXCLUDED.password, " +
            "role = EXCLUDED.role " +
            "RETURNING *")
    Mono<UserEntity> insertUser(UserEntity user);
}
