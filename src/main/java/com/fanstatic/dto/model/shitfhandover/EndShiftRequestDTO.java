package com.fanstatic.dto.model.shitfhandover;

import com.google.firebase.internal.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EndShiftRequestDTO {
    @NonNull
    private Long endShiftCash;

    private String note;
}
