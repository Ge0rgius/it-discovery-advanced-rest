package it.discovery.version;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiVersion {

    v1(true), v2(false);

    private final boolean defaultVersion;

    public boolean match(ApiVersion version) {
        return version == null && defaultVersion || version == this;
    }
}
