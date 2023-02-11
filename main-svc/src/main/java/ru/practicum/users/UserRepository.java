package ru.practicum.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.users.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u " +
            "from User as u " +
            "WHERE u.id in ?1")
    Page<User> findAllByIds(Long[] ids, Pageable page);
}
