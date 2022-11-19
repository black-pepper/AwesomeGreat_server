package com.baseurak.AwesomeGreat.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    User findByPersonalId(String personalId);
    User findByPersonalIdAndPassword(String personalId, String password);
    void deleteByPersonalId(String personalId);
    List<User> findAll();
    //void save(User user);

    //Optional<User> findByEmail(String email);
}
