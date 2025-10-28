package com.fastcampus.projectboard.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("비즈니스 로직 - 페이지네이션")
// @WebMvcTest는 웹 계층만 로드하지만, @SpringBootTest는 전체 애플리케이션 컨텍스트를 로드
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = PaginationService.class)
class PaginationServiceTest {

    private final PaginationService sut;

    public PaginationServiceTest(PaginationService sut) {
        this.sut = sut;
    }

    @DisplayName("현재 페이지 번호와 전체 페이지 수를 주면, 페이징 바 번호 리스트를 반환한다.")
    @MethodSource // 외부 메서드에서 제공하는 다양한 매개변수 조합으로 테스트를 여러 번 실행할 수 있게 해줌
    @ParameterizedTest(name = "[{index}] 현재 페이지 {0} , 총 페이지 {1} => {2} ") // 테스트 메서드를 매개변수화하여 여러 입력 값에 대해 반복 실행할 수 있게 해줌
    void givenCurrentPageNumberAndTotalPages_wheCalculating_thenReturnsPaginationBarNumbers(
            int currentPageNumber,
            int totalPages,
            int expectedSize,
            List<Integer> expected

    ){
        // Given


        // When
        List<Integer> actual = sut.getPaginationBarNumbers(currentPageNumber, totalPages);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    // 테스트에 사용할 매개변수 조합을 제공하는 메서드
    // Arguments.of(...) 메서드를 사용하여 각 테스트 케이스의 매개변수 값을 정의
    // 각 Arguments.of(...) 호출은 테스트 메서드에 전달될 매개변수 집합을 나타냄
    // 예: Arguments.of(현재 페이지 번호, 전체 페이지 수, 예상되는 페이징 바 크기, 예상되는 페이징 바 번호 리스트)
    // 이 메서드는 Stream<Arguments>를 반환하여 여러 테스트 케이스를 정의
    // 테스트 메서드는 이 메서드에서 제공하는 각 Arguments 집합에 대해 반복 실행됨
    // Stream<Arguments>는 Java의 스트림 API를 사용하여 여러 Arguments 객체를 순차적으로 제공
    static Stream<Arguments> givenCurrentPageNumberAndTotalPages_wheCalculating_thenReturnsPaginationBarNumbers(){
        return Stream.of(
            arguments(1,13,List.of(0,1,2,3,4)),
            arguments(2,13,List.of(0,1,2,3,4)),
            arguments(3,13,List.of(0,1,2,3,4)),
            arguments(4,13,List.of(1,2,3,4,5)),
            arguments(5,13,List.of(2,3,4,5,6)),
            arguments(6,13,List.of(4,5,6,7,8)),
            arguments(10,13,List.of(8,9,10,11,12)),
            arguments(11,13,List.of(9,10,11,12)),
            arguments(12,13,List.of(10,11,12))
        );
    }

    @DisplayName("현재 설정되어 있는 페이지네이션 바의 길이를 알려준다.")
    @Test
    void givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnsCurrentBarLength() {
        // Given
        // When
        int barLength = sut.currentBarLength();

        // Then
        assertThat(barLength).isEqualTo(5);
    }

}