package com.fanstatic.dto.model.shitfhandover;

import com.google.firebase.internal.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StartShiftRequestDTO {

    @NonNull
    private Long startShiftCash;

    private String note;
}
