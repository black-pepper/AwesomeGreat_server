package com.baseurak.AwesomeGreat.nickname;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class NicknameService {

    private final NicknameRepository nicknameRepository;

    public String makeNickname() {
        Random random = new Random();

        List<Nickname> names = nicknameRepository.findBySequence(0);
        List<Nickname> adjective = nicknameRepository.findBySequence(1);
        String result = adjective.get(random.nextInt(adjective.size())).getNickname();
        result += ' ' + names.get(random.nextInt(names.size())).getNickname();

        return result;
    }
}