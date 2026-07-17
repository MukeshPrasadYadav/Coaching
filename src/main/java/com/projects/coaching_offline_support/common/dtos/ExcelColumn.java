package com.projects.coaching_offline_support.common.dtos;

import java.util.function.Function;

public record ExcelColumn<T>(
        String header,
        Function<T, Object> extractor
) {
}
