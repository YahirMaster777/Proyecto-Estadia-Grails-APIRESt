def profile = Templates.findByUuid(params.uuid).collect { profile ->
    return [
        id         : profile.id,
        uuid       : profile.uuid,
        name       : profile.name,
        description: profile.description
    ]
}

println(profile)

if (!profile) {
    new Logs("Información del perfil", "No se encontró la información solicitada", logId, "INFO", false, [:])
    Utils.logger(logId, "Información del perfil", "No se encontró la información solicitada")
    return TypeError.informationNotFound(logId)
}

def permissions
try {
    permissions = TemplatePermissions.findByTemplate(profile[0]?.id?.toDouble()).collect { permission ->
        return [
            template: permission.template,
            seccion : permission.section,
            permisos: permission.permission
        ]
    }
} catch (e) {
    println("Error converting profile ID to Double: ${e.message}")
    return TypeError.informationConversionError(logId)
}

def info = [informacion: profile, permisos: permissions]
