package com.ordenaris.internalControl

class SectionUser {

    User user
    Integer permission 
    Section section

    static mapping = {
        table name: "section_user"
        version false
        permission sqlType: "int(11)"
    }
    static constraints = {
        permission nullable: false, blank: false
    }
}
