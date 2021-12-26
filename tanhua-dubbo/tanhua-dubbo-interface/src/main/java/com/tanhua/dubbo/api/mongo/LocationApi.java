package com.tanhua.dubbo.api.mongo;

import java.util.List;

public interface LocationApi {
    void saveLocation(Double latitude, Double longitude, String addrStr, Long userId);

    List<Long> searchNear(Long userId, Long distance);
}
