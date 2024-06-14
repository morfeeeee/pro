package ru.yarsu.web

import org.http4k.template.PebbleTemplates
import org.http4k.template.TemplateRenderer



fun templateRend(bool: Boolean): TemplateRenderer {
    if(bool) return PebbleTemplates().CachingClasspath()
    return PebbleTemplates().HotReload("src/main/resources")
}


