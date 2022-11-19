package com.baseurak.AwesomeGreat.nickname;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * 닉네임 관련 요청을 데이터베이스에서 처리합니다.
 * @Author: Uju
 */
@Repository
public interface NicknameRepository  extends JpaRepository<Nickname, Long> {
    List<Nickname> findBySequence(int sequence);
}