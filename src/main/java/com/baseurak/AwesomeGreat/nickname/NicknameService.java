package com.baseurak.AwesomeGreat.nickname;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
/**
 * 닉네임 관련 요청을 처리하기 위해 해당 로직을 실행합니다.
 * @Author: Uju
 */
@Service
@RequiredArgsConstructor
public class NicknameService {

    private final NicknameRepository nicknameRepository;

    /**
     * 랜덤 닉네임을 생성합니다.
     */
    public String getNickname() {
        Random random = new Random();

        List<Nickname> names = nicknameRepository.findBySequence(0);
        List<Nickname> adjective = nicknameRepository.findBySequence(1);
        String result = adjective.get(random.nextInt(adjective.size())).getNickname();
        result += ' ' + names.get(random.nextInt(names.size())).getNickname();

        return result;
    }

    /**
     * 명사 닉네임을 모두 가져옵니다.
     */
    public List<Nickname> readNounNickname() {
        return nicknameRepository.findBySequence(0);
    }

    /**
     * 형용사 닉네임을 모두 가져옵니다.
     */
    public List<Nickname> readAdjectiveNickname() {
        return nicknameRepository.findBySequence(1);
    }

    /**
     * name과 sequence(명사/형용사)를 받아 닉네임을 저장합니다.
     */
    public void write(String name, int sequence) {
        Nickname nickname = new Nickname(name, sequence);
        nicknameRepository.save(nickname);
    }

    /**
     * nicknameId를 받아 해당 닉네임을 삭제합니다.
     */
    public void delete(Long nicknameId){
        Optional<Nickname> nickname = nicknameRepository.findById(nicknameId);
        if (!nickname.isEmpty()){
            nicknameRepository.delete(nickname.get());
        }
    }
}