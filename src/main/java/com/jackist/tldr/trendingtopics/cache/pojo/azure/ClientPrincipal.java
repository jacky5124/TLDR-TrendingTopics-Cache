package com.jackist.tldr.trendingtopics.cache.pojo.azure;

import java.util.List;

public record ClientPrincipal(String auth_typ, String name_typ, String role_typ,
                              List<ClientPrincipalClaim> claims) {
}
