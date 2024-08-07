flyway {
    url = jdbcCfg["url"]
    user = jdbcCfg["username"]
    password = jdbcCfg["password"]
    schemas = arrayOf("public")
}
