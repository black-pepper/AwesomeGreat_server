package com.baseurak.AwesomeGreat.nickname;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NicknameRepository  extends JpaRepository<Nickname, Long> {
    List<Nickname> findBySequence(int sequence);
}