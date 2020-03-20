package it.quimatematica.sqrt;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqrtRunner {

    private static final int NUMBER = 98765;

    public static void main(String[] args) {
        Sqrt sqrt = new Sqrt();
        SqrtResult result = sqrt.calculate(NUMBER);
        log.info("The square root of {} is {} with rest {}", NUMBER, result.getSqrt(), result.getRest());
        sqrt.dump(System.out); //NOSONAR
    }

}
