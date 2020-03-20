package it.quimatematica.sqrt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SqrtResult {

    private int sqrt;
    private int rest;

}
