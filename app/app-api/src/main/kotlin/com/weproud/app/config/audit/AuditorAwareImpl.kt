package com.weproud.app.config.audit

import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import java.util.Optional

class AuditorAwareImpl : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        return SecurityContextHolder.getContext().authentication?.name?.let { Optional.of(it) }
            ?: Optional.empty()
    }
}
