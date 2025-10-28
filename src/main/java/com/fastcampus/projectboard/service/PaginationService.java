package com.fastcampus.projectboard.service;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {

    private static final int BAR_LENGTH = 5;

    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPages){
        int barSize = 5;

        // 현재에서 절반 크기만큼 빼면 시작 값을 알 수 있는 이유 : 현재 페이지를 가운데에 두기 위해서
        // Math.max(1, ...) : 시작 값이 1보다 작아지면 안되기 때문에
        // Math.min(totalPages, ...) : 끝 값이 전체 페이지 수를 넘지 않도록 하기 위해서
        int startPage = Math.max(1, currentPageNumber - (barSize / 2));
        int endPage = Math.min(totalPages, startPage + barSize - 1);

        return IntStream.rangeClosed(startPage, endPage).boxed().toList(); // rangeClosed : 시작 값과 끝 값을 포함하는 범위 생성
    }

    public int currentBarLength() {
        return BAR_LENGTH;
    }
}
